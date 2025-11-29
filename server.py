import os
import glob
import logging
from datetime import datetime
from flask import Flask, request, jsonify
from werkzeug.utils import secure_filename
from flask_cors import CORS
from dotenv import load_dotenv
from langchain_community.document_loaders import PyMuPDFLoader
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain_openai import OpenAIEmbeddings, ChatOpenAI
from langchain_community.vectorstores import Chroma
from langchain.memory import ConversationBufferMemory
from langchain.chains import ConversationalRetrievalChain
from langchain.prompts import PromptTemplate, SystemMessagePromptTemplate, HumanMessagePromptTemplate, ChatPromptTemplate

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('app.log'),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)

# Load environment variables
load_dotenv()

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})

# Configuration
MODEL = "gpt-4o-mini"
DB_NAME = "vector_db"
CHUNK_SIZE = 1500
CHUNK_OVERLAP = 300
TEMPERATURE = 0.3  # Lower temperature for more consistent, focused answers
MAX_HISTORY = 10  # Limit conversation history
UPLOAD_FOLDER = 'docs'  # Folder to store uploaded documents
ALLOWED_EXTENSIONS = {'pdf'}

# Ensure upload directory exists
os.makedirs(UPLOAD_FOLDER, exist_ok=True)

def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

# Global variables
conversation_chain = None
vectorstore = None

def validate_environment():
    """Validate that all required environment variables are set."""
    api_key = os.getenv('OPENAI_API_KEY')
    if not api_key:
        logger.error("OPENAI_API_KEY not found in environment variables")
        raise ValueError("OPENAI_API_KEY must be set in .env file")
    logger.info("Environment variables validated successfully")
    return True

def load_documents():
    """Load all PDF documents from the project root."""
    pdf_files = glob.glob("*.pdf") + glob.glob(os.path.join(UPLOAD_FOLDER, "*.pdf"))
    
    if not pdf_files:
        logger.warning("No PDF files found in the project root or docs folder")
        return []
    
    logger.info(f"Found {len(pdf_files)} PDF file(s): {pdf_files}")
    
    all_documents = []
    
    for pdf_path in pdf_files:
        try:
            logger.info(f"Loading: {pdf_path}")
            loader = PyMuPDFLoader(pdf_path)
            docs = loader.load()
            
            # Add enhanced metadata
            for doc in docs:
                doc.metadata.update({
                    "source": os.path.basename(pdf_path),
                    "doc_type": "PDF",
                    "loaded_at": datetime.now().isoformat()
                })
            
            all_documents.extend(docs)
            logger.info(f"Successfully loaded {len(docs)} pages from {pdf_path}")
            
        except Exception as e:
            logger.error(f"Error loading {pdf_path}: {str(e)}", exc_info=True)
            continue
    
    logger.info(f"Total documents loaded: {len(all_documents)} pages")
    return all_documents

def create_vectorstore(documents):
    """Create or update the vector store."""
    global vectorstore
    
    if not documents:
        logger.error("No documents provided for vectorstore creation")
        return None
    
    try:
        # Use RecursiveCharacterTextSplitter for better semantic chunking
        text_splitter = RecursiveCharacterTextSplitter(
            chunk_size=CHUNK_SIZE,
            chunk_overlap=CHUNK_OVERLAP,
            length_function=len,
            separators=["\n\n", "\n", ". ", " ", ""]
        )
        
        chunks = text_splitter.split_documents(documents)
        logger.info(f"Created {len(chunks)} text chunks")
        
        # Create embeddings
        embeddings = OpenAIEmbeddings()
        
        # Clean up existing vectorstore if needed
        if os.path.exists(DB_NAME):
            try:
                logger.info("Removing existing vectorstore")
                Chroma(persist_directory=DB_NAME, embedding_function=embeddings).delete_collection()
            except Exception as e:
                logger.warning(f"Could not delete existing collection: {e}")
        
        # Create new vectorstore
        vectorstore = Chroma.from_documents(
            documents=chunks,
            embedding=embeddings,
            persist_directory=DB_NAME
        )
        
        logger.info("Vectorstore created successfully")
        return vectorstore
        
    except Exception as e:
        logger.error(f"Error creating vectorstore: {str(e)}", exc_info=True)
        return None

def create_enhanced_prompt():
    """Cria e retorna um SystemMessagePromptTemplate pronto para usar."""
    system_template = """Você é o Assistente Virtual Inteligente da Universidade Eduardo Mondlane (UEM). 
O seu objetivo é responder perguntas usando **apenas os documentos fornecidos** no contexto.

IDENTIDADE
- Nome: Assistente UEM
- Estilo: profissional, educado, claro e útil
- Nunca invente informação. Nunca utilize conhecimento externo.

REGRAS PRINCIPAIS
1. Toda a resposta deve basear-se exclusivamente nos documentos disponíveis.
2. Se a informação não existir no contexto, responda exatamente:
   "Desculpe, não encontrei esta informação nos documentos disponíveis."
3. Não escreva títulos como “Fonte da Informação”, “Resposta Direta”, “Contexto”, etc.
4. Organize a resposta de forma natural, seguindo esta ordem:
   - Primeiro: indique a localização da informação (documento, secção, artigo, página...), se disponível.
   - Depois: responda diretamente à pergunta de forma clara e objetiva.
   - Em seguida: ofereça explicação adicional ou contexto útil, se relevante.
   - Finalmente: inclua uma citação curta do documento quando apropriado.
5. Se a pergunta for apenas uma saudação, cumprimente e ofereça ajuda.
6. Se a pergunta for vaga, peça esclarecimento de forma educada.
7. Nunca inclua títulos de secções do prompt na resposta.


7. COERÊNCIA ENTRE DOCUMENTO E TEMA:
   - Antes de responder, verifique se a informação retirada do documento 
     corresponde exatamente ao tema perguntado pelo usuário.
   - Se um documento falar sobre "bolsas" e a pergunta for sobre "exames de admissão",
     você NÃO pode usar esse documento como fonte.
   - Se encontrar informação parecida mas em documentos de temas diferentes,
     diga claramente que esse documento NÃO é sobre o tema da pergunta.

   Regra de Ouro:
   **Nunca responda usando documentos que tratam de outro assunto.**

8. LINKS:
   - Você só pode fornecer um link se ELE APARECER EXACTAMENTE nos documentos fornecidos.
   - Nunca assuma nem deduza links.
   - Se o link não existir literalmente no documento, responda:
     "Desculpe, o link específico não aparece nos documentos disponíveis."
   - Se houver múltiplos links, coloque cada um em uma nova linha ou use uma lista.

FORMATO A SEGUIR (SEM TÍTULOS, APENAS O CONTEÚDO):
1. Localização da informação (se existir)
2. Resposta clara e objetiva
3. Explicação adicional
4. Citação curta (se relevante)

Impoortante:
AO responder as perguntas nao coloque os titulos acima, apenas a respostas.

DOCUMENTOS DISPONÍVEIS:
{context}

HISTÓRICO DA CONVERSA:
{chat_history}

PERGUNTA:
{question}

RESPOSTA:
"""
    return system_template

def initialize_chain():
    """Initialize the conversational chain with enhanced prompts."""
    global conversation_chain, vectorstore
    
    try:
        # Validate environment
        validate_environment()
        
        # Load documents
        documents = load_documents()
        if not documents:
            logger.error("No documents loaded - cannot initialize chain")
            return None
        
        # Create vectorstore
        vectorstore = create_vectorstore(documents)
        if not vectorstore:
            logger.error("Failed to create vectorstore")
            return None
        
        # Initialize LLM with optimized parameters
        llm = ChatOpenAI(
            temperature=TEMPERATURE,
            model_name=MODEL,
            max_tokens=1000,
            request_timeout=30
        )
        
        # Create memory with limited history
        memory = ConversationBufferMemory(
            memory_key='chat_history',
            return_messages=True,
            output_key="answer",
            max_token_limit=2000
        )
        
        # Create retriever with optimized parameters
        retriever = vectorstore.as_retriever(
            search_type="mmr",
            search_kwargs={
                "k": 6,
                "fetch_k": 20,
                "lambda_mult": 0.7
            }
        )
        
        # Create custom prompt template
        template = create_enhanced_prompt()
        
        qa_prompt = PromptTemplate(
            template=template,
            input_variables=["context", "chat_history", "question"]
        )
        
        # Create the conversational chain with custom prompt
        conversation_chain = ConversationalRetrievalChain.from_llm(
            llm=llm,
            retriever=retriever,
            memory=memory,
            return_source_documents=True,
            verbose=False,
            combine_docs_chain_kwargs={"prompt": qa_prompt}
        )
        
        logger.info("Conversational chain initialized successfully")
        return conversation_chain
        
    except Exception as e:
        logger.error(f"Error initializing chain: {str(e)}", exc_info=True)
        return None

# Initialize on startup
logger.info("Starting UEM Intelligent Assistant API")
initialize_chain()

@app.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint."""
    return jsonify({
        "status": "healthy",
        "chain_initialized": conversation_chain is not None,
        "vectorstore_ready": vectorstore is not None,
        "timestamp": datetime.now().isoformat()
    })

@app.route('/chat', methods=['POST'])
def chat():
    """Handle chat requests with enhanced error handling."""
    global conversation_chain
    
    try:
        # Validate chain initialization
        if not conversation_chain:
            logger.warning("Chain not initialized, attempting to initialize")
            if initialize_chain() is None:
                return jsonify({
                    "error": "Sistema não inicializado. Verifique se os arquivos PDF estão disponíveis."
                }), 500
        
        # Validate request
        if not request.json:
            return jsonify({"error": "Requisição inválida - JSON esperado"}), 400
        
        user_message = request.json.get('message', '').strip()
        
        if not user_message:
            return jsonify({"error": "Mensagem vazia"}), 400
        
        if len(user_message) > 1000:
            return jsonify({"error": "Mensagem muito longa (máximo 1000 caracteres)"}), 400
        
        logger.info(f"Processing message: {user_message[:100]}...")
        
        # Process the message
        result = conversation_chain.invoke({"question": user_message})
        
        # Extract response and sources
        response = result.get("answer", "Desculpe, não consegui processar sua pergunta.")
        sources = result.get("source_documents", [])
        
        # Log successful response
        logger.info(f"Response generated successfully (length: {len(response)})")
        
        return jsonify({
            "response": response,
            "sources": len(sources),
            "timestamp": datetime.now().isoformat()
        })
        
    except Exception as e:
        logger.error(f"Error processing chat request: {str(e)}", exc_info=True)
        return jsonify({
            "error": "Erro ao processar solicitação. Por favor, tente novamente.",
            "details": str(e) if app.debug else None
        }), 500

@app.route('/documents', methods=['GET'])
def get_documents():
    """Get list of available PDF documents."""
    try:
        pdf_files = glob.glob("*.pdf")
        docs = []
        
        for f in pdf_files:
            try:
                size_bytes = os.path.getsize(f)
                size_mb = size_bytes / (1024 * 1024)
                
                docs.append({
                    "name": f,
                    "size": f"{size_mb:.2f} MB",
                    "size_bytes": size_bytes,
                    "type": "PDF",
                    "modified": datetime.fromtimestamp(os.path.getmtime(f)).isoformat()
                })
            except Exception as e:
                logger.error(f"Error getting info for {f}: {e}")
                continue
        
        return jsonify({
            "documents": docs,
            "count": len(docs),
            "timestamp": datetime.now().isoformat()
        })
        
    except Exception as e:
        logger.error(f"Error listing documents: {str(e)}", exc_info=True)
        return jsonify({"error": "Erro ao listar documentos"}), 500

@app.route('/upload', methods=['POST'])
def upload_file():
    """Handle file upload and re-indexing."""
    if 'file' not in request.files:
        return jsonify({"error": "Nenhum arquivo enviado"}), 400
    
    file = request.files['file']
    
    if file.filename == '':
        return jsonify({"error": "Nenhum arquivo selecionado"}), 400
        
    if file and allowed_file(file.filename):
        try:
            filename = secure_filename(file.filename)
            file_path = os.path.join(UPLOAD_FOLDER, filename)
            file.save(file_path)
            logger.info(f"File saved to {file_path}")
            
            # Re-initialize chain to include new document
            initialize_chain()
            
            return jsonify({
                "message": "Arquivo enviado e processado com sucesso",
                "filename": filename,
                "timestamp": datetime.now().isoformat()
            })
        except Exception as e:
            logger.error(f"Error saving file: {str(e)}", exc_info=True)
            return jsonify({"error": f"Erro ao salvar arquivo: {str(e)}"}), 500
            
    return jsonify({"error": "Tipo de arquivo não permitido (apenas PDF)"}), 400

@app.route('/reset', methods=['POST'])
def reset_conversation():
    """Reset the conversation history."""
    global conversation_chain
    
    try:
        if conversation_chain and hasattr(conversation_chain, 'memory'):
            conversation_chain.memory.clear()
            logger.info("Conversation history cleared")
            return jsonify({
                "message": "Histórico de conversa limpo com sucesso",
                "timestamp": datetime.now().isoformat()
            })
        else:
            return jsonify({"error": "Chain não inicializado"}), 500
            
    except Exception as e:
        logger.error(f"Error resetting conversation: {str(e)}", exc_info=True)
        return jsonify({"error": "Erro ao resetar conversa"}), 500

@app.errorhandler(404)
def not_found(error):
    return jsonify({"error": "Endpoint não encontrado"}), 404

@app.errorhandler(500)
def internal_error(error):
    logger.error(f"Internal server error: {error}", exc_info=True)
    return jsonify({"error": "Erro interno do servidor"}), 500

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
