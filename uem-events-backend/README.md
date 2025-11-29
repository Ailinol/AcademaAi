# UEM Events Backend

Backend robusto e profissional para gerenciamento de eventos da **Universidade Eduardo Mondlane (UEM)** desenvolvido com **Spring Boot 3** e **Java 17**.

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.2.0**
  - Spring Web
  - Spring Data JPA
  - Spring Validation
- **H2 Database** (desenvolvimento)
- **PostgreSQL** (produção)
- **Lombok** (redução de boilerplate)
- **Springdoc OpenAPI** (Swagger UI)
- **Maven**

## 📋 Funcionalidades

✅ **API REST completa** para gerenciamento de eventos  
✅ **CRUD** de eventos com validação robusta  
✅ **Filtros avançados** (categoria, status, vagas disponíveis)  
✅ **Paginação e ordenação** de resultados  
✅ **Sistema de inscrições** com controle de vagas  
✅ **Tratamento de exceções** global e padronizado  
✅ **Documentação automática** com Swagger/OpenAPI  
✅ **Dados de exemplo** pré-carregados  

## 🗂️ Estrutura do Projeto

```
uem-events-backend/
├── src/main/java/mz/uem/events/
│   ├── entity/           # Entidades JPA
│   │   ├── Event.java
│   │   ├── EventCategory.java
│   │   └── EventStatus.java
│   ├── repository/       # Repositórios JPA
│   │   └── EventRepository.java
│   ├── dto/              # Data Transfer Objects
│   │   ├── EventDTO.java
│   │   ├── EventCardDTO.java
│   │   └── CreateEventRequest.java
│   ├── service/          # Lógica de negócio
│   │   └── EventService.java
│   ├── controller/       # REST Controllers
│   │   └── EventController.java
│   ├── exception/        # Tratamento de exceções
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ErrorResponse.java
│   │   ├── ResourceNotFoundException.java
│   │   └── BusinessException.java
│   ├── config/           # Configurações
│   │   ├── CorsConfig.java
│   │   └── DataInitializer.java
│   └── UemEventsApplication.java
└── src/main/resources/
    └── application.yml   # Configuração da aplicação
```

## 🔧 Como Executar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+

### Executar em modo desenvolvimento

```bash
cd uem-events-backend
mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

### Acessar Swagger UI
📚 Documentação interativa da API: http://localhost:8080/swagger-ui.html

### Acessar H2 Console
🗄️ Console do banco de dados H2: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:uemevents`
- **Username**: `sa`
- **Password**: (deixar em branco)

## 📡 Endpoints da API

### Eventos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/events` | Listar todos os eventos (paginado) |
| GET | `/api/events/{id}` | Buscar evento por ID |
| GET | `/api/events/category/{category}` | Filtrar por categoria |
| GET | `/api/events/status/{status}` | Filtrar por status |
| GET | `/api/events/upcoming` | Eventos próximos |
| GET | `/api/events/available` | Eventos com vagas |
| POST | `/api/events` | Criar novo evento |
| PUT | `/api/events/{id}` | Atualizar evento |
| DELETE | `/api/events/{id}` | Deletar evento |
| POST | `/api/events/{id}/register` | Registrar participante |

### Exemplos de Uso

#### Listar eventos com paginação
```bash
GET http://localhost:8080/api/events?page=0&size=10&sortBy=eventDate&sortDirection=ASC
```

#### Criar novo evento
```bash
POST http://localhost:8080/api/events
Content-Type: application/json

{
  "title": "Workshop de Spring Boot",
  "description": "Aprenda a criar APIs REST profissionais",
  "eventDate": "2025-12-15T14:00:00",
  "location": "Sala 301",
  "organizer": "Departamento de TI",
  "category": "TECH",
  "maxCapacity": 50,
  "imageUrl": "https://example.com/image.jpg",
  "tags": ["Java", "Spring Boot", "API"]
}
```

#### Filtrar por categoria
```bash
GET http://localhost:8080/api/events/category/TECH
```

## 🏗️ Modelo de Dados

### Event Entity

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long | ID único do evento |
| title | String | Título do evento |
| description | String | Descrição detalhada |
| eventDate | LocalDateTime | Data e hora do evento |
| location | String | Local do evento |
| organizer | String | Organizador do evento |
| category | EventCategory | Categoria (TECH, ACADEMIC, etc.) |
| maxCapacity | Integer | Capacidade máxima |
| currentRegistrations | Integer | Inscrições atuais |
| status | EventStatus | Status (UPCOMING, ONGOING, etc.) |
| imageUrl | String | URL da imagem |
| tags | List\<String\> | Tags do evento |
| createdAt | LocalDateTime | Data de criação |
| updatedAt | LocalDateTime | Data de atualização |

### Categorias Disponíveis
- ACADEMIC - Académico
- CULTURAL - Cultural
- SPORTS - Desporto
- TECH - Tecnologia
- WORKSHOP - Workshop
- SEMINAR - Seminário
- CONFERENCE - Conferência
- GRADUATION - Graduação
- CAREER - Carreira
- OTHER - Outro

### Status Disponíveis
- UPCOMING - Próximo
- ONGOING - Em Curso
- COMPLETED - Concluído
- CANCELLED - Cancelado

## 🔒 Validações

O sistema inclui validações robustas:
- Título: 3-200 caracteres
- Descrição: 10-2000 caracteres
- Data do evento: deve ser futura
- Capacidade máxima: mínimo 1
- Inscrições: não podem exceder a capacidade

## 🌐 CORS

CORS configurado para aceitar requisições de qualquer origem (`*`), facilitando integração com frontends.

## 📊 Perfis de Configuração

### Development (default)
- Banco H2 em memória
- Logs detalhados
- H2 Console habilitado

### Production
- PostgreSQL
- Logs otimizados
- H2 Console desabilitado

Para executar em produção:
```bash
mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## 👥 Autor

**UEM Development Team**

## 📄 Licença

© 2025 Universidade Eduardo Mondlane
