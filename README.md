# 🚀 EventFlow Commerce Engine

A distributed, event-driven order processing system built using **Spring Boot, Kafka, Redis, and PostgreSQL**.

This project demonstrates how modern backend systems handle **asynchronous workflows, fault tolerance, and scalability** using real-world patterns like **Saga orchestration, idempotency, retries, and caching**.

---

## 🧠 Architecture Overview

```text
Client → Order Service → Kafka → Inventory Service → Kafka → Payment Service → Kafka → Order Service
```

* Services communicate via **Kafka events**
* **Redis** used for caching, idempotency, and rate limiting
* **PostgreSQL** is the source of truth

---

## ⚙️ Tech Stack

* Java + Spring Boot
* Apache Kafka
* PostgreSQL
* Redis
* Docker

---

## 🔥 Key Features

### ✅ Event-Driven Architecture

* Asynchronous communication using Kafka
* Decoupled microservices

### ✅ Saga Pattern (Choreography)

* Order → Inventory → Payment → Final Status
* Handles success and failure flows

### ✅ Idempotency (Redis)

* Prevents duplicate event processing
* Uses Redis `SETNX` strategy

### ✅ Retry + Dead Letter Queue (DLQ)

* Automatic retries on failure
* Failed messages routed to DLQ topics

### ✅ Caching (Redis)

* Cache-aside pattern for order retrieval
* Reduces database load

### ✅ Rate Limiting

* Redis-based request throttling
* Protects APIs from abuse

---

## 🧩 Services

### 🛒 Order Service

* Creates orders
* Publishes `order-created` events
* Updates final order status

### 📦 Inventory Service

* Consumes `order-created`
* Publishes `inventory-success` / `inventory-failed`

### 💳 Payment Service

* Consumes `inventory-success`
* Publishes `payment-success` / `payment-failed`

---

## 📡 Event Flow

```text
order-created
   ↓
inventory-success / inventory-failed
   ↓
payment-success / payment-failed
   ↓
order-completed / order-cancelled
```

---

## 🐳 Running the Project

### 1. Start Infrastructure

```bash
docker compose up -d
```

Services:

* Kafka
* Zookeeper
* PostgreSQL
* Redis

---

### 2. Run Microservices

Run each service:

```bash
mvn spring-boot:run
```

---

### 3. Test API

```bash
POST /orders
```

---

## ⚡ Redis Usage

* **Idempotency:** Prevent duplicate event processing
* **Caching:** Order read optimization
* **Rate Limiting:** API protection

---

## 🧪 Failure Handling

* Retries with backoff
* Dead Letter Queue (DLQ) for failed events
* Logging for debugging

---

## 🧠 Key Concepts Demonstrated

* Event-driven microservices
* Kafka-based messaging
* Saga pattern (choreography)
* Idempotent consumers
* Cache-aside strategy
* Distributed system fault tolerance

---

## 🚀 Future Improvements

* Schema registry for event versioning
* Observability (Prometheus + Grafana)
* Distributed tracing
* Authentication & authorization
* Kubernetes deployment

---

## 📌 Author

Rushikesh

---

## ⭐ If you found this useful, consider giving it a star!
