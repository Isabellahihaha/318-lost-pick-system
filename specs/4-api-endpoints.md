# API Endpoints Summary: Lost & Found Platform

This document exposes the microservice user stories as external REST API endpoints, designed for human comprehension and integration test generation.

---

## 1. Lost Item Service (`http://localhost:8081/api`)

| Feature | Method | Endpoint Path | Request Body | Response (Success) | Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **LI1** | `POST` | `/lost-items` | `LostItemRequest` | `201 Created` (`LostItemResponse`) | Report a new lost item |
| **LI2** | `GET` | `/lost-items/{id}` | *None* | `200 OK` (`LostItemResponse`) | Retrieve details of a specific lost item |

---

## 2. Found Item Service (`http://localhost:8082/api`)

| Feature | Method | Endpoint Path | Request Body | Response (Success) | Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **FI1** | `POST` | `/found-items` | `FoundItemRequest` | `201 Created` (`FoundItemResponse`) | Report a new found item |

---

## 3. Matching Service (`http://localhost:8083/api`)

| Feature | Method | Endpoint Path | Request Body | Response (Success) | Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **MS1** | `GET` | `/matches` | *None* | `200 OK` (`Array<MatchResponse>`) | Query AI-assisted potential matches |

---

## 4. Claim Service (`http://localhost:8084/api`)

| Feature | Method | Endpoint Path | Request Body | Response (Success) | Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **CS1** | `POST` | `/claims` | `ClaimRequest` | `201 Created` (`ClaimResponse`) | Submit an ownership claim for an item |

---

## 5. Detailed OpenAPI Definition (`/api/lost-items`)

```yaml
paths:
  /api/lost-items:
    post:
      summary: Report a lost item
      operationId: createLostItem
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              properties:
                userId:
                  type: integer
                  example: 1
                itemName:
                  type: string
                  example: "AirPods Pro"
                category:
                  type: string
                  example: "Electronics"
                description:
                  type: string
                  example: "White AirPods Pro with a pink protective case"
                location:
                  type: string
                  example: "UOW Library"
                lostDate:
                  type: string
                  format: date
                  example: "2026-09-15"
      responses:
        '201':
          description: Created successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/LostItemResponse'
          description: Created successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/LostItemResponse'
