# Simple Task Organizer - Backend API

Environment variables required (set via .env by orchestrator):
- MONGODB_URL: Mongo connection string, e.g., mongodb://user:pass@host:port
- MONGODB_DB: Database name, e.g., tasks

Run:
- ./gradlew bootRun

Swagger:
- /swagger-ui.html
- /api-docs

Auth (session-based):
- POST /api/auth/signup {email, password}
- POST /api/auth/login {email, password}
- GET /api/auth/me
- POST /api/auth/logout

Tasks (requires login):
- GET /api/tasks
- POST /api/tasks {title, description?, dueDate?}
- GET /api/tasks/{id}
- PATCH /api/tasks/{id} {title?, description?, completed?, dueDate?}
- DELETE /api/tasks/{id}
