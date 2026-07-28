# 💳 Payment API

A production-style Spring Boot based Payment Processing API that simulates real-world transaction workflows including validation layers, event-driven processing, and dead letter handling.

---

## 🚀 Project Overview

This project demonstrates a clean and structured backend architecture for processing payment transactions with multiple validation layers and event handling mechanisms.

It follows clean architecture principles and separates responsibilities across Controller, Service, Repository, Validation, and Event layers.

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Maven
- SQLite Database
- RESTful APIs

---

## 🏗️ Architecture Overview

Controller Layer  
↓  
Service Layer  
↓  
Validation Pipeline  
↓  
Repository Layer  
↓  
Database  

---

## 📦 Key Components

- TransactionController – Exposes REST endpoints
- TransactionService – Handles business logic
- ValidationPipeline – Executes layered validations
- BusinessValidation
- RiskValidation
- ComplianceValidation
- EventPublisher & EventSubscriber – Event-driven processing
- DeadLetterEvent – Stores failed transaction events
- GlobalExceptionHandler – Centralized exception management

---

## ✅ Features

- Transaction processing
- Merchant validation
- Risk validation
- Compliance validation
- Event publishing system
- Dead letter queue handling
- Centralized exception handling
- Structured API response model

---

## 📂 Project Structure

Payment_API
 ├── controller
 ├── service
 ├── repository
 ├── entity
 ├── dto
 ├── validation
 ├── event
 ├── exception
 └── config

---

## ▶️ How to Run the Application

### 1️⃣ Clone Repository

git clone https://github.com/Paranubhav-20/Payment_API.git  
cd Payment_API

### 2️⃣ Build Project

mvn clean install

### 3️⃣ Run Application

mvn spring-boot:run

Application will start at:

http://localhost:8080

---

## 📌 Sample API Endpoint

### Create Transaction

POST /transactions

### Request Body Example

{
  "merchantId": "M123",
  "amount": 1000.0,
  "customer": {
    "name": "John Doe",
    "email": "john@example.com"
  }
}

---

## 🧪 Testing

You can test the API using:

- Postman
- cURL
- Any REST client

---

## 🔐 Error Handling

- Global exception handling implemented
- Validation errors handled gracefully
- Failed events stored in Dead Letter table

---

## 📈 Future Improvements

- JWT Authentication
- Swagger/OpenAPI Documentation
- Docker containerization
- CI/CD integration
- Unit & Integration test coverage

---

## 👨‍💻 Author

Jiban Jyoti Pradhan

---

## 📜 License

This project is developed for educational and demonstration purposes.
