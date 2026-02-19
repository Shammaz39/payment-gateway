# ✅ Payment Gateway System — Architecture & Flow

## 1. Overview

This project implements a **distributed payment gateway platform** designed to simulate real-world payment processing workflows.

The system allows merchants to:

* Register and obtain API keys
* Initiate payments via the gateway
* Process payments asynchronously
* Receive payment status updates
* Integrate with an external bank (mock implementation)

The architecture follows a **microservices + event-driven model** using Kafka for decoupled processing.

---

## 2. Architecture

The system is composed of multiple independent services:

```
Merchant → Payment Service → Kafka → Payment Processor → Mock Bank
                                     ↓
                               Notification Service
```

### Architecture Style

* Microservices
* Event-driven (Kafka)
* REST-based external APIs
* Asynchronous payment processing
* Callback-driven bank integration

### Core Layers

| Layer             | Responsibility                     |
| ----------------- | ---------------------------------- |
| API Layer         | Merchant & payment REST endpoints  |
| Business Layer    | Payment orchestration & validation |
| Event Layer       | Kafka-based async processing       |
| Integration Layer | Bank & merchant callbacks          |
| Persistence Layer | Transaction & payment logs         |

---

## 3. Services & Responsibilities

### Merchant Service

**Purpose:** Merchant onboarding & authentication

**Responsibilities:**

* Merchant registration
* API key generation & validation
* Merchant lookup
* API key rotation

---

### Payment Service

**Purpose:** Entry point for payment initiation

**Responsibilities:**

* Merchant authentication
* Payment validation
* Transaction creation
* Kafka event publishing
* Payment status APIs

---

### Payment Processor Service

**Purpose:** Core payment orchestration engine

**Responsibilities:**

* Consume payment initiated events
* Call bank integration
* Process bank callbacks
* Update transaction status
* Publish payment processed events

---

### Mock Bank

**Purpose:** Simulated external banking system

**Responsibilities:**

* Accept payment requests
* Simulate payment outcome
* Trigger callback to processor

---

### Notification Service

**Purpose:** Merchant communication layer

**Responsibilities:**

* Consume payment processed events
* Notify merchants of payment status
* Merchant service integration for contact details

---

## 4. API Endpoints

### Merchant Service

| Endpoint             | Method | Purpose                   |
| -------------------- | ------ | ------------------------- |
| `/merchant/register` | POST   | Register merchant         |
| `/merchant/validate` | POST   | Validate merchant API key |

---

### Payment Service

| Endpoint               | Method | Purpose                 |
| ---------------------- | ------ | ----------------------- |
| `/auth/login`          | POST   | Merchant authentication |
| `/payment/initiate`    | POST   | Initiate payment        |
| `/payment/status/{id}` | GET    | Fetch payment status    |

---

### Processor Service

| Endpoint         | Method | Purpose                |
| ---------------- | ------ | ---------------------- |
| `/bank/callback` | POST   | Bank callback receiver |

---

## 5. Payment Flow

### Step-by-Step Flow

1. Merchant registers via Merchant Service
2. Merchant receives API key
3. Merchant initiates payment via Payment Service
4. Payment Service:

    * Validates merchant
    * Creates transaction
    * Publishes Kafka event
5. Payment Processor consumes event
6. Processor calls Mock Bank
7. Mock Bank processes request
8. Mock Bank sends callback to Processor
9. Processor updates transaction status
10. Processor publishes processed event
11. Notification Service consumes event
12. Merchant receives payment outcome

This design ensures **decoupled, scalable processing**.

---

## 6. Resilience & Design Considerations

### Idempotency

* Transaction ID used to avoid duplicates

### Asynchronous Processing

* Kafka ensures decoupled execution
* Prevents API latency

### Retry Capability

* Kafka consumer retry support
* Bank integration can be retried safely

### Event-Driven Notifications

* No tight coupling between processor and notification service

### Auditability

* PaymentLog & Transaction entities provide traceability

---

## 7. Technology Stack

| Component        | Technology  |
| ---------------- | ----------- |
| Language         | Java        |
| Framework        | Spring Boot |
| Messaging        | Kafka       |
| Database         | JPA / RDBMS |
| Security         | JWT         |
| Cache            | Redis       |
| Containerization | Docker      |

---

# ⭐ Final Summary

> “This is a microservice-based payment gateway where merchant onboarding, payment initiation, processing, and notifications are separated into independent services. Payments are initiated via a Payment Service, asynchronously processed via Kafka by a Payment Processor, integrated with a simulated bank, and final status updates are delivered via a Notification Service. This design improves scalability, resilience, and observability.”


