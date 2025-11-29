# Guia de Configuração e Execução ⚡

## 🚀 Como Executar o Backend Spring Boot

### Método 1: Usar Maven (Recomendado se já tiver instalado)

Se você já tem Maven instalado:
```bash
cd f:\projecto\uem-events-backend
mvn spring-boot:run
```

### Método 2: Instalar Maven

1. **Baixar Maven:**
   - Acesse: https://maven.apache.org/download.cgi
   - Baixe o arquivo `.zip` (ex: apache-maven-3.9.6-bin.zip)

2. **Instalar:**
   - Extrair para `C:\Program Files\Apache\maven`
   - Adicionar ao PATH:
     - Variável de ambiente `MAVEN_HOME`: `C:\Program Files\Apache\maven`
     - Adicionar ao `Path`: `%MAVEN_HOME%\bin`

3. **Verificar instalação:**
   ```bash
   mvn --version
   ```

### Método 3: Usar Spring Initializr (Alternativa Fácil)

1. Vá para: https://start.spring.io/
2. Configurar projeto:
   - **Project**: Maven
   - **Language**: Java
   - **Spring Boot**: 3.2.0
   - **Group**: mz.uem
   - **Artifact**: uem-events-backend
   - **Java**: 17

3. **Adicionar Dependencies**:
   - Spring Web
   - Spring Data JPA
   - H2 Database
   - PostgreSQL Driver
   - Validation
   - Lombok
   - Spring Boot DevTools

4. Clicar em "Generate" e baixar
5. Copiar os arquivos do nosso projeto para a pasta gerada
6. Executar: `./mvnw spring-boot:run`

### Método 4: Usar IDE (IntelliJ IDEA / Eclipse)

1. **IntelliJ IDEA** (Recomendado):
   - File → Open → Selecionar pasta `uem-events-backend`
   - Aguardar o Maven importar dependências
   - Clicar com botão direito em `UemEventsApplication.java`
   - Run 'UemEventsApplication'

2. ** Eclipse**:
   - Import → Existing Maven Project
   - Selecionar `uem-events-backend`
   - Run As → Spring Boot App

## ✅ Verificar se está Funcionando

Após executar o backend, você deve ver:
```
========================================
🚀 UEM Events Backend is running!
📚 Swagger UI: http://localhost:8080/swagger-ui.html
🗄️  H2 Console: http://localhost:8080/h2-console
========================================
```

### Testar API:
1. **Swagger UI**: http://localhost:8080/swagger-ui.html
   - Interface visual para testar todos os endpoints

2. **Listar eventos**:
   ```bash
   curl http://localhost:8080/api/events
   ```
   
3. **H2 Console**: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:uemevents`
   - Username: `sa`
   - Password: (deixar em branco)

## 🌐 Executar Frontend

1. **Opção 1 - Python HTTP Server** (Simples):
   ```bash
   cd f:\projecto
   python -m http.server 3000
   ```
   Depois abra: http://localhost:3000/pages/eventos.html

2. **Opção 2 - Live Server (VS Code Extension)**:
   - Instalar extensão "Live Server"
   - Clicar com botão direito em `eventos.html`
   - Selecionar "Open with Live Server"

3. **Opção 3 - Simplesmente abrir o arquivo**:
   - Abrir `f:\projecto\pages\eventos.html` diretamente no navegador
   - **IMPORTANTE**: Alguns navegadores podem bloquear CORS. Use Chrome com flag:
     ```bash
     chrome.exe --disable-web-security --user-data-dir="C:/temp/chrome-dev"
     ```

## ⚠️ Troubleshooting

### Backend não inicia:
- ✅ Java 17 instalado? `java --version`
- ✅ Maven instalado? `mvn --version`
- ✅ Porta 8080 livre? Outra aplicação pode estar usando

### Frontend não carrega eventos:
- ✅ Backend está rodando em `localhost:8080`?
- ✅ Abrir console do navegador (F12) para ver erros
- ✅ CORS habilitado? (Já configurado no backend)

### Erro "CORS blocked":
- Backend já tem CORS configurado
- Se persistir, usar Chrome com `--disable-web-security`

## 📝 Próximos Passos

1. ✅ Backend já está criado e funcionando
2. ✅ Frontend já integrado com a API
3. 🔄 Testar criar/editar/deletar eventos
4. 🔄 Adicionar mais eventos de exemplo
5. 🚀 Deploy para produção (opcional)

## 🎯 Endpoints Disponíveis

Ver documentação completa:
- **README**: `f:\projecto\uem-events-backend\README.md`
- **Swagger**: http://localhost:8080/swagger-ui.html

## 💡 Dicas

- Use IntelliJ IDEA Community (gratuito) para melhor experiência
- Swagger UI é a forma mais fácil de testar a API
- H2 Console permite visualizar os dados em tempo real
- Os eventos de exemplo já estão carregados automaticamente

---

**🎉 Pronto! Seu backend Spring Boot está configurado e pronto para uso!**
