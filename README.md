# Merchant Activation Tracker (Backend)

## Overview

The Merchant Activation Tracker is a backend service for managing merchant onboarding. It models onboarding as a step-based workflow and provides APIs for tracking progress, identifying stalled merchants, and enabling intervention.

This system is designed primarily for **internal operations teams**, while also exposing APIs that can support lightweight merchant-facing experiences.

## Note
## the frontend is embedded in the backend and can be accessed via `http://localhost:8080`

---

## Tech Stack

* Java 21
* Spring Boot
* Spring Data JPA (Hibernate)
* PostgreSQL
* Lombok
* Spring Validation

---

## Running the Application

### Prerequisites

* Java 21+
* Gradle

---

### Run

**Gradle**

```bash
./gradlew bootRun
```
**DB Configuration
** Create a database then go to application properties on the app and change these 3 fields according to your setup 
* spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name 
* spring.datasource.username=${DB_USERNAME} 
* spring.datasource.password=${DB_PASSWORD} 
---

### Access

* Base API:
  `http://localhost:8080`


---

## Core Features

* Step-based onboarding workflow
* Activation progress tracking
* Stuck merchant detection (48h inactivity)
* Intervention system (notes, escalation)
* Pagination & filtering
* Next-step guidance

---

## API Endpoints

### Create Merchant

**POST** `/api/merchants`

```json
{
  "name": "Shop Alpha"
}
```

---

### Get Merchants

**GET** `/api/merchants`

Query params:

* `page`
* `size`
* `status`
* `name`

---

### Get Merchant Steps

**GET** `/api/merchants/{id}/steps`

---

### Complete Step

**POST** `/api/merchants/{merchantId}/steps/{stepId}/complete`

---

### Get Next Step

**GET** `/api/merchants/{id}/next-step`

---

### Add Note

**POST** `/api/merchants/{id}/notes`

```json
{
  "note": "Called merchant",
  "type": "CONTACTED"
}
```

---

### Get Notes

**GET** `/api/merchants/{id}/notes`

---

## Data Model

### Merchant

* id
* name
* status
* createdAt
* lastUpdatedAt

---

### OnboardingStep

* id
* name
* stepOrder

---

### MerchantStepProgress

* merchantId
* stepId
* status
* updatedAt

---

### MerchantNote

* merchantId
* note
* type
* createdAt

---

## Seed Data

The system initializes realistic onboarding scenarios:

* NOT_STARTED
* IN_PROGRESS
* ACTIVATED
* STUCK
* STUCK with intervention

This allows immediate testing via Swagger.

---

## Sample Workflow

1. Create merchant
2. Fetch steps
3. Complete steps
4. Observe status changes
5. Detect stuck merchants
6. Add intervention notes

---

## Error Handling

Example:

```json
{
  "error": "Merchant not found"
}
```

---

## Notes

* Authentication is intentionally excluded
* Merchant UI is not implemented but supported via APIs
* System is designed for extensibility
