<!-- .github/copilot-instructions.md - guidance for AI coding agents working on DevLog -->
# DevLog — Copilot / Agent Instructions

Purpose: short, actionable guidance to help AI agents be productive in this repository.

1) Big picture
- Backend: Spring Boot (Java 17) API in `backend/`. Application entry: `DevLogApplication.java`. The server runs on port 8080 and mounts all controllers under the context-path `/api` (see `application.yml`).
- Frontend: React app in `frontend/` (dev server on port 3000). The frontend uses `axios` with baseURL `http://localhost:8080/api` (see `frontend/src/api/axios.js`).
- Database: PostgreSQL. Schema and seed SQL are in `database/schema.sql` and `database/seed.sql`.

2) Key patterns & conventions (do not invent alternatives)
- Persistence: MyBatis XML mappers live in `backend/src/main/resources/mapper/` (see `DevLogMapper.xml`, `ProjectMapper.xml`). MyBatis config sets `type-aliases-package` to `com.vibecoding.devlog.domain` and enables underscore-to-camel-case.
- Domain objects: simple POJOs annotated via Lombok (`@Data`) in `backend/src/main/java/com/vibecoding/devlog/domain/`.
- Controllers: Spring `@RestController` classes are under `controller/`. Example health endpoint: [backend/src/main/java/com/vibecoding/devlog/controller/HealthCheckController.java](backend/src/main/java/com/vibecoding/devlog/controller/HealthCheckController.java#L1).
- CORS: `WebConfig` explicitly allows `http://localhost:3000` and credentials — respect this when changing CORS behavior ([backend/src/main/java/com/vibecoding/devlog/config/WebConfig.java](backend/src/main/java/com/vibecoding/devlog/config/WebConfig.java#L1)).

3) Build / run / debug (explicit commands)
- Setup DB: create DB `devlog` and apply schema: `psql -U postgres -d devlog -f database/schema.sql` (credentials default to `postgres:postgres` in `application.yml`).
- Run backend (dev):
  - `cd backend`
  - `mvn spring-boot:run`
- Run backend tests: `mvn test` from `backend/`.
- Run frontend (dev):
  - `cd frontend`
  - `npm install`
  - `npm start`
- Build frontend for production: `npm run build` in `frontend/`.

4) Agent coding guidance (how to propose changes)
- Small, focused PRs only: modify a single layer at a time (controller, service, or mapper).
- When adding an API: add a controller method, corresponding MyBatis mapper XML, and a domain/DTO change if required. Update mapper XML under `resources/mapper/` and ensure aliases/package names match.
- Use existing naming: table/column names follow snake_case; MyBatis `map-underscore-to-camel-case` is enabled so Java fields are camelCase.
- Avoid introducing new frameworks or global config changes without human sign-off.

5) Tests & validation the agent should run before suggesting changes
- Run `mvn test` in `backend/` and ensure no new failures.
- Start backend and frontend locally and verify the health endpoint: `curl http://localhost:8080/api/health`.
- Lint/build frontend with `npm run build` if changing UI assets.

6) Files to inspect for context examples
- `backend/pom.xml` — dependency and plugin decisions.
- `backend/src/main/resources/application.yml` — DB, server context-path `/api`, and logging levels.
- `backend/src/main/java/com/vibecoding/devlog/config/WebConfig.java` — CORS rules.
- `backend/src/main/resources/mapper/*.xml` — how SQL is organized; prefer following those examples.
- `frontend/src/api/axios.js` — base API integration and error handling.

7) When uncertain, ask the maintainer for:
- DB migration or credential changes.
- Permission to modify global logging, security, or CORS policies.

Feedback: after applying suggested code, run the two-step local smoke test: backend + `curl /api/health`, and frontend UI smoke via `npm start`.

If anything in this file is unclear or you want more examples (controller → mapper → SQL), ask and I will expand with concrete, minimal change examples.
