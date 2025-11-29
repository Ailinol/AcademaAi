# 🎯 Como Executar - Guia Rápido

## ✅ Passo a Passo

### 1. Executar o Backend Spring Boot

Abra PowerShell na pasta do backend e execute:
```powershell
cd f:\projecto\uem-events-backend
.\run-backend.ps1
```

**OU** se estiver usando IntelliJ IDEA:
- Abrir a pasta `uem-events-backend`
- Run `UemEventsApplication.java`

O backend deve iniciar e mostrar:
```
========================================
🚀 UEM Events Backend is running!
📚 Swagger UI: http://localhost:8080/swagger-ui.html
🗄️  H2 Console: http://localhost:8080/h2-console
========================================
```

### 2. Verificar que está Funcionando

Abra no navegador:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:uemevents`, username: `sa`)

Ou teste via curl:
```powershell
curl http://localhost:8080/api/events
```

### 3. Abrir o Frontend

Simplesmente abra no navegador:
```
f:\projecto\pages\eventos.html
```

**OU** use um servidor HTTP:
```powershell
cd f:\projecto
python -m http.server 3000
```
Depois acesse: http://localhost:3000/pages/eventos.html

## 🎨 O Que Você Vai Ver

### Backend (Swagger UI):
- ✅ 13 endpoints documentados
- ✅ Interface para testar cada endpoint
- ✅ Exemplos de requisições e respostas

### Frontend (Eventos):
- ✅ 8 eventos pré-carregados
- ✅ Cards com imagens, badges, tags
- ✅ Filtros por categoria e status
- ✅ Barra de progresso de vagas
- ✅ Modal com detalhes completos
- ✅ Botão de inscrição funcional

## 🔧 Troubleshooting

**Backend não inicia?**
- `java --version` (deve ser Java 17+)
- Porta 8080 já em uso? Feche outros aplicativos

**Frontend não carrega eventos?**
- Backend está rodando? Verifique http://localhost:8080/swagger-ui.html
- Abra F12 no navegador e verifique o Console para erros

**Maven não funciona?**
- O script `run-backend.ps1` baixa tudo automaticamente
- Ou instale Maven: https://maven.apache.org/download.cgi

## 📱 Endpoints Principais

```
GET    /api/events              - Listar todos os eventos
GET    /api/events/{id}         - Buscar por ID
GET    /api/events/upcoming     - Próximos eventos
POST   /api/events/{id}/register - Registrar participante
POST   /api/events              - Criar novo evento
```

**Exemplo de criação de evento (Swagger ou curl):**
```json
{
  "title": "Novo Workshop",
  "description": "Descrição do workshop",
  "eventDate": "2025-12-20T14:00:00",
  "location": "Sala 101",
  "organizer": "Departamento de TI",
  "category": "WORKSHOP",
  "maxCapacity": 30,
  "tags": ["Workshop", "Tecnologia"]
}
```

## 🚀 Pronto!

- Backend: http://localhost:8080/swagger-ui.html
- Frontend: f:\projecto\pages\eventos.html
- H2 Console: http://localhost:8080/h2-console

**Aproveite seu backend Spring Boot profissional! 🎉**
