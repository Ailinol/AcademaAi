# AcademaAi - Assistente Inteligente UEM

Portal web inteligente da Universidade Eduardo Mondlane que integra um chatbot baseado em IA para responder perguntas sobre regulamentos e documentos, e um sistema de gestão de eventos universitários.

## 🚀 Funcionalidades

- **Assistente IA**: Responde perguntas com base em documentos PDF (Regulamentos, Editais, etc.) usando GPT-4 e RAG.
- **Gestão de Eventos**: Backend robusto em Spring Boot para listar, criar e gerenciar eventos da UEM.
- **Interface Moderna**: Frontend responsivo com identidade visual da UEM.

## 🛠 Tecnologias

### Frontend
- HTML5, CSS3, JavaScript (Vanilla)
- Design responsivo
- Integração com APIs REST

### Backend (IA & Chat)
- **Linguagem**: Python 3.11+
- **Framework**: Flask
- **IA**: LangChain, OpenAI GPT-4
- **Vector DB**: ChromaDB

### Backend (Eventos)
- **Linguagem**: Java 17+
- **Framework**: Spring Boot 3
- **Banco de Dados**: H2 (Memória) / Configurável para outros
- **Documentação**: Swagger UI

## 📋 Pré-requisitos

- **Java 17** ou superior (para o backend de eventos)
- **Python 3.11** ou superior (para o assistente IA)
- **Chave API da OpenAI**
- **Git**

## 🔧 Instalação e Execução

### 1. Configuração do Repositório

Clone o projeto:
```bash
git clone https://github.com/Ailinol/AcademaAi.git
cd AcademaAi
```

### 2. Backend de Eventos (Java)

Navegue até a pasta do backend e execute:

**Windows (PowerShell):**
```powershell
cd uem-events-backend
.\run-backend.ps1
```

**Linux/Mac:**
```bash
cd uem-events-backend
./mvnw spring-boot:run
```

O backend iniciará em `http://localhost:8080`.
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **H2 Console**: `http://localhost:8080/h2-console`

### 3. Backend de IA (Python)

Volte para a raiz do projeto e configure o ambiente Python:

```bash
# Criar ambiente virtual
python -m venv venv

# Ativar ambiente (Windows)
venv\Scripts\activate

# Ativar ambiente (Linux/Mac)
source venv/bin/activate

# Instalar dependências
pip install -r requirements.txt
```

Crie um arquivo `.env` na raiz com sua chave da OpenAI:
```
OPENAI_API_KEY=sk-sua-chave-aqui
```

Inicie o servidor Python:
```bash
python server.py
```
O servidor de IA rodará em `http://localhost:5000`.

### 4. Frontend

Para visualizar a aplicação, você pode usar um servidor HTTP simples na raiz do projeto:

```bash
# Em um novo terminal na raiz do projeto
python -m http.server 3000
```

Acesse em `http://localhost:3000`.

## 📁 Estrutura do Projeto

```
projecto/
├── uem-events-backend/    # Backend Java Spring Boot (Eventos)
├── css/                   # Estilos do Frontend
├── js/                    # Scripts do Frontend
├── pages/                 # Páginas HTML (eventos.html, assistente.html, etc.)
├── uploaded_pdfs/         # Documentos para a IA
├── vector_db/             # Banco de dados vetorial da IA
├── server.py              # Servidor Flask para a IA
├── requirements.txt       # Dependências Python
├── index.html             # Página Principal
└── README.md              # Documentação
```

## 🤝 Contribuindo

1. Faça um Fork do projeto
2. Crie uma Branch para sua Feature (`git checkout -b feature/NovaFeature`)
3. Faça o Commit (`git commit -m 'Adicionando nova feature'`)
4. Faça o Push (`git push origin feature/NovaFeature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto é desenvolvido no contexto da Universidade Eduardo Mondlane.
