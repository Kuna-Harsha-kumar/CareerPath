# Career Path

[![Repository](https://img.shields.io/badge/GitHub-CareerPath-181717?logo=github)](https://github.com/Kuna-Harsha-kumar/CareerPath)
[![Live application](https://img.shields.io/badge/Live%20app-Render-46E3B7?logo=render)](https://career-growth-1.onrender.com/)

Career Path is a full-stack career guidance platform that connects students with professional counselors. Students can manage their profile, upload a resume, discover counselors, schedule appointments, and complete payments. Counselors can manage availability, meetings, student relationships, and their professional profile.

## Live project

- **Application:** [career-growth-1.onrender.com](https://career-growth-1.onrender.com/)
- **Source code:** [github.com/Kuna-Harsha-kumar/CareerPath](https://github.com/Kuna-Harsha-kumar/CareerPath)

The deployment may sleep on Render's free tier. If the first request does not respond, retry after the service wakes up.

## Architecture

```text
React/Vite frontend ── Axios/JWT ── Spring Boot REST API ── MyBatis ── H2 or MySQL/TiDB
											 │
											 ├── SMTP email and OTP recovery
											 ├── Stripe checkout
											 └── Apache Tika/OpenNLP resume analysis
```

## Features

### Student experience

- Student registration and login
- JWT bearer-token authentication and protected API requests
- OTP-based password recovery and password reset
- Student profile viewing and editing
- Resume upload and parsing workflow
- Counselor directory and counselor detail pages
- Appointment scheduling, modification, cancellation, and history
- Stripe checkout flow for paid services

### Counselor experience

- Counselor registration and login
- OTP-based counselor password recovery and reset
- Counselor profile management
- Availability and meeting-slot creation
- Student appointment management
- Student list and counselor analytics support

### Platform capabilities

- React single-page application with React Router
- Spring Boot REST API with Spring Security and JWT filtering
- H2 in-memory database for local/demo use, with MySQL/TiDB configuration support
- Configurable CORS origins and feature flags
- Email integration for OTP delivery
- Apache Tika and OpenNLP dependencies for resume analysis
- Docker multi-stage build for serving the frontend through Spring Boot
- Cypress end-to-end tests for authentication, dashboard, appointments, and payment flows

## Technology stack

| Layer | Technologies |
| --- | --- |
| Frontend | React 18, Vite, React Router, Axios, Tailwind CSS |
| Backend | Java 17+, Spring Boot 3.4, Spring Security, JWT, MyBatis |
| Data | H2 for local development; MySQL/TiDB-compatible configuration |
| Integrations | SMTP email, Stripe, Apache Tika, OpenNLP |
| Testing | Spring Boot tests and Cypress |
| Deployment | Docker and Render |

## Repository structure

```text
career_growth/
├── backend/                  # Spring Boot API and business logic
│   ├── src/main/java/        # Controllers, services, entities, mappers, config
│   └── src/main/resources/   # Application configuration and schema
├── frontend/                 # React/Vite client application
│   ├── src/pages/             # Student and counselor screens
│   └── cypress/               # End-to-end tests
├── Dockerfile                # Combined frontend/backend production image
└── .env.example              # Environment-variable template
```

## Prerequisites

- Java 17 or newer
- Node.js 20 or newer and npm
- Maven 3.9+ (or the included Maven wrapper)
- Docker, optional for the production-style build

## Local setup

1. Clone the repository and enter the project directory.
2. Copy `.env.example` to `.env` or export the variables in your shell. Do not commit `.env`.
3. Start the backend:

   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

   The API runs at `http://localhost:8080` by default.

4. In another terminal, start the frontend:

   ```bash
   cd frontend
   npm ci
   npm run dev
   ```

   The frontend runs at `http://localhost:5173`.

### Environment variables

`.env.example` documents the supported settings. The most important values are:

| Variable | Purpose | Local default |
| --- | --- | --- |
| `FRONTEND_URL` | Allowed browser origin for CORS | `http://localhost:5173` |
| `BACKEND_URL` | Backend base URL | `http://localhost:8080` |
| `DB_URL` | JDBC connection string | H2 in-memory database |
| `MAIL_USERNAME` / `MAIL_PASSWORD` | SMTP account for OTP email | Empty |
| `STRIPE_SECRET_KEY` | Stripe server-side key | Empty |

Keep real credentials in environment variables or the deployment provider's secret store.

### MySQL/TiDB configuration

The default local profile uses H2 in-memory storage. To use MySQL or TiDB, set `DB_URL`, `DB_USER`, `DB_PASSWORD`, and `DB_DRIVER=com.mysql.cj.jdbc.Driver` before starting the backend. Keep all credentials in your environment or deployment provider's secret store.

## Testing

Run backend tests from `backend`:

```bash
./mvnw test
```

Run the frontend build and lint checks from `frontend`:

```bash
npm ci
npm run lint
npm run build
```

Cypress tests can be run interactively with `npm run test:e2e` or headlessly with `npm run test:e2e:ci`. Some end-to-end scenarios expect the backend to be available at `http://localhost:8080`.

## Deployment

The root `Dockerfile` builds the Vite frontend, copies its static output into the Spring Boot application, and runs the resulting application on port `8080`. Configure production secrets as Render environment variables rather than committing them to the repository.

## Security note

Never commit database passwords, SMTP passwords, Stripe keys, JWT secrets, or other credentials. If a credential has ever been committed, rotate it at the provider before deploying this project.

## Project status

This project was developed as part of ACS 567 Professional Growth Systems coursework.