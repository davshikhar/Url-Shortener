# 🔗 URL Shortener

A Spring Boot web application that shortens long URLs into clean, shareable links.

## 🚀 Live Demo
[your-app.onrender.com](https://short-url-p5b3.onrender.com)

## 🛠️ Tech Stack
- **Backend:** Java, Spring Boot, Spring MVC
- **Database:** PostgreSQL (Neon)
- **Frontend:** Thymeleaf, HTML, CSS
- **Deployment:** Render (Docker)

## ✨ Features
- Shorten any long URL into a compact link
- Redirect short URLs back to the original
- Duplicate URL detection (same URL returns same short code)
- Expiry support for short URLs
- Clean, responsive dark UI

## ⚙️ Running Locally

### Prerequisites
- Java 17
- Maven
- PostgreSQL database

## 🔧 Environment Variables
| Variable | Description |
|---|---|
| `NEON_DB_URL` | PostgreSQL connection string |
