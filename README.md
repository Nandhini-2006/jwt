# Security Sample — Spring Boot + JWT + React

A full-stack authentication sample:
- **Backend**: Spring Boot 3, Spring Security, JWT (HMAC-SHA256), Spring Data JPA, MySQL
- **Frontend**: React (Vite), tabbed login/register/dashboard UI, session persisted in `localStorage`

## Project layout

```
security_sample/
├── backend/    Spring Boot API (port 8080)
└── frontend/   React + Vite app (port 5173)
```

## Prerequisites

Install these before you start:

| Tool | Version | Check with |
|---|---|---|
| Java JDK | 17+ | `java -version` |
| Maven | 3.8+ | `mvn -version` |
| Node.js | 18+ | `node -v` |
| npm | 9+ | `npm -v` |
| MySQL | 8.x, running locally on port 3306 | `mysql --version` |

## 1. Configure the database

Open `backend/src/main/resources/application.properties` and set your real MySQL password:

```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

Everything else (URL, username `root`, JWT secret) is already filled in with working defaults.
The database `auth_db` and the `users` table are created automatically on first run
(`createDatabaseIfNotExist=true` + `spring.jpa.hibernate.ddl-auto=update`) — you don't need to
create anything by hand.

## 2. Run the backend

```bash
cd security_sample/backend
mvn spring-boot:run
```

Wait for `Started SecuritySampleApplication` in the logs. The API is now live at
`http://localhost:8080`.

Quick sanity check in a second terminal:

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"secret123"}'
```

You should get back `{"message":"User registered successfully!"}`.

## 3. Run the frontend

Open a **new** terminal (leave the backend running):

```bash
cd security_sample/frontend
npm install
npm run dev
```

Vite will print a local URL — open **http://localhost:5173** in your browser.

## 4. Use the app

1. Go to the **Register** tab, create an account.
2. Switch to **Login**, sign in with the same credentials.
3. You'll land on the **dashboard**, which calls the protected `GET /api/home` endpoint
   using the JWT stored in `localStorage` (sent as `Authorization: Bearer <token>`).
4. Refresh the page — you stay logged in, since the token persists in `localStorage`.
5. Click **Logout** to clear the session.

## API reference

| Method | Endpoint | Auth required | Description |
|---|---|---|---|
| POST | `/api/auth/register` | No | Creates a new user (`ROLE_USER`), password hashed with BCrypt |
| POST | `/api/auth/login` | No | Validates credentials, returns a signed JWT |
| GET | `/api/home` | Yes (`Authorization: Bearer <token>`) | Protected dashboard data |

## Troubleshooting

- **`mvn` command not found** — install Maven, or use the wrapper if you add one (`./mvnw`).
- **Backend fails to start with a MySQL connection error** — confirm MySQL is running
  (`mysql.server start` / `sudo service mysql start`) and that the password in
  `application.properties` matches your local `root` password.
- **CORS errors in the browser console** — make sure the frontend is running on
  `http://localhost:5173` (the only origin currently allowed in `SecurityConfig.java`).
- **401 Unauthorized on `/api/home`** — your token may have expired (default: 24h) or you're
  not logged in; log out and log back in.
- **Port already in use** — change `server.port` in `application.properties` (backend) or run
  `npm run dev -- --port 5174` (frontend), and update the `BASE_URL` in
  `frontend/src/api.js` accordingly.
