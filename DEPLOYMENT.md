# Guia de Deploy - Assistente Inteligente UEM

## Arquitetura do Projeto

Este projeto possui duas partes:
- **Frontend**: HTML/CSS/JavaScript puro (páginas estáticas)
- **Backend**: Python Flask com LangChain e OpenAI

## Opções de Deploy

### Opção 1: Deploy Completo com Render (Recomendado para Testes)

**Vantagens:**
- Gratuito para começar
- Suporta Python backend e arquivos estáticos
- HTTPS automático
- Fácil configuração

**Passos:**

1. **Criar conta no Render**: https://render.com

2. **Preparar o projeto** (já feito):
   - `requirements.txt` ✓
   - `server.py` ✓
   - `.env` com `OPENAI_API_KEY` ✓

3. **Criar Web Service no Render**:
   - Conectar seu repositório Git
   - Configurar:
     - **Build Command**: `pip install -r requirements.txt`
     - **Start Command**: `gunicorn server:app`
     - **Environment Variables**: Adicionar `OPENAI_API_KEY`

4. **Adicionar Gunicorn** (necessário para produção):
   ```bash
   pip install gunicorn
   echo "gunicorn==21.2.0" >> requirements.txt
   ```

### Opção 2: Frontend (Vercel/Netlify) + Backend (Render)

**Frontend no Vercel/Netlify:**
- Deploy apenas dos arquivos HTML/CSS/JS
- Muito rápido e gratuito
- CDN global

**Backend no Render/Railway:**
- API Python separada
- Atualizar URLs do frontend para apontar para o backend

### Opção 3: Deploy Local/Servidor Próprio

Se você tem um servidor ou VPS:

```bash
# Instalar dependências
pip install -r requirements.txt

# Rodar com Gunicorn (produção)
gunicorn -w 4 -b 0.0.0.0:5000 server:app

# Frontend pode ser servido pelo Nginx
```

## Configuração Necessária

### 1. Adicionar Gunicorn ao projeto

Criar arquivo `requirements.txt` atualizado:
```
Flask==3.0.0
flask-cors==4.0.0
langchain==0.1.0
langchain-openai==0.0.2
langchain-community==0.0.10
chromadb==0.4.22
pypdf==3.17.4
python-dotenv==1.0.0
gunicorn==21.2.0
```

### 2. Variáveis de Ambiente

No serviço de hosting, configurar:
- `OPENAI_API_KEY`: Sua chave da OpenAI

### 3. Atualizar CORS (se necessário)

Em `server.py`, atualizar a origem permitida:
```python
CORS(app, origins=["https://seu-dominio.com"])
```

## Checklist de Deploy

- [ ] Repositório Git criado e sincronizado
- [ ] `OPENAI_API_KEY` configurada no serviço de hosting
- [ ] `gunicorn` adicionado ao `requirements.txt`
- [ ] CORS configurado para o domínio correto
- [ ] PDFs incluídos no repositório (na pasta raiz)
- [ ] Frontend testado localmente
- [ ] Backend testado localmente
- [ ] Deploy realizado e testado

## URLs Após Deploy

Depois do deploy, você terá:
- **Frontend**: `https://seu-app.vercel.app` ou `https://seu-app.onrender.com`
- **Backend API**: `https://seu-backend.onrender.com`

## Próximos Passos

1. Escolher a opção de deploy
2. Criar repositório Git (se ainda não tiver)
3. Seguir os passos da opção escolhida
4. Testar a aplicação em produção

## Suporte

Para problemas de deploy:
- Render: https://docs.render.com
- Vercel: https://vercel.com/docs
- Netlify: https://docs.netlify.com
