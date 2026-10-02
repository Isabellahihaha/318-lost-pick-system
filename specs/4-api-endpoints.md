# API Endpoints Summary: Lost & Found Platform

This document exposes the microservice user stories as external REST API endpoints, designed for human comprehension and integration test generation.

---

## 1. Summary of API Endpoints

| Method | Endpoint Path | Microservice | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/lost-items` | Lost Item Service | Report a new lost item |
| `GET` | `/api/lost-items/{id}` | Lost Item Service | Retrieve details of a specific lost item |
| `POST` | `/api/found-items` | Found Item Service | Report a new found item |
| `GET` | `/api/matches` | Matching Service | Query AI-assisted potential matches |
| `POST` | `/api/claims` | Claim Service | Submit an ownership claim for an item |

---

## 2. Detailed Endpoint Descriptions & OpenAPI Definition

### Report Lost Item
* **Path:** `/api/lost-items`
* **Method:** `POST`
* **Description:** Allows a user to submit a lost item report with descriptive attributes.

#### OpenAPI Spec (`yaml`)
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
