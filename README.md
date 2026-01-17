# 📓 Smart Journal App (Backend)

> A robust, secure, and feature-rich REST API for journaling, built with Spring Boot, MongoDB, and Redis.

![Java](https://img.shields.io/badge/Java-17+-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0+-green)
![MongoDB](https://img.shields.io/badge/Database-MongoDB-leaf)
![Redis](https://img.shields.io/badge/Cache-Redis-red)
![Security](https://img.shields.io/badge/Security-JWT-blue)

## 📖 Overview
This is a backend application designed to help users track their thoughts and mood. It goes beyond simple CRUD operations by integrating **external APIs** for context (Weather & Location), **Redis Caching** for performance, and **Email Schedulers** to re-engage inactive users.

## 🚀 Key Features
- **🔐 Secure Authentication:** JWT-based stateless authentication with BCrypt password hashing.
- **⚡ High Performance:** Redis caching implemented for Weather and Location API calls to reduce latency and API costs.
- **🤖 Automation:** Scheduled jobs (Cron) to analyze user activity and send weekly summaries or "We miss you" emails.
- **☁️ External Integrations:** Fetches real-time weather and timezone data to add context to journal entries.
- **🛡️ Admin Controls:** dedicated Admin APIs for user management.
- **📝 API Documentation:** Fully documented with Swagger UI (OpenAPI).

## 🛠️ Tech Stack
* **Framework:** Spring Boot 3
* **Database:** MongoDB (Atlas/Local)
* **Caching:** Redis
* **Security:** Spring Security & JWT
* **Documentation:** SpringDoc (Swagger UI)
* **Utilities:** Lombok, Java Mail Sender

## ⚙️ Configuration & Environment Variables
This project uses `application.yml` for configuration. To run this locally, you must set up the following environment variables or update the file:

```yaml
MONGO_URI=mongodb+srv://<your-db-user>:<password>@cluster0.mongodb.net/journaldb
REDIS_HOST=localhost
REDIS_PORT=6379
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
JWT_SECRET=your-very-secure-secret-key
WEATHER_API_KEY=your-weather-api-key