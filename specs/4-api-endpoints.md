openapi: 3.0.3
info:
  title: Microservice Lost & Found Platform API Specification
  description: OpenAPI 3.0 specification for Lost Item Service, Found Item Service, Matching Service, and Claim Service microservices based on user stories.
  version: 1.0.1
servers:
  - url: http://localhost:8081/api
    description: Lost Item Service Local Development Server
  - url: http://localhost:8082/api
    description: Found Item Service Local Development Server
  - url: http://localhost:8083/api
    description: Matching Service Local Development Server
  - url: http://localhost:8084/api
    description: Claim Service Local Development Server

tags:
  - name: Lost Item Service
    description: Endpoints for reporting and managing lost items[cite: 1]
  - name: Found Item Service
    description: Endpoints for reporting and managing found items[cite: 1]
  - name: Matching Service
    description: Endpoints for AI-assisted potential item matches[cite: 1]
  - name: Claim Service
    description: Endpoints for submitting item ownership claims[cite: 1]

paths:
  # ==========================================
  # LOST ITEM SERVICE ENDPOINTS
  # ==========================================
  /lost-items:
    get:
      tags:
        - Lost Item Service
      summary: List all lost items
      operationId: getAllLostItems
      responses:
        '200':
          description: List of all lost items retrieved successfully
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/LostItemResponse'
    post:
      tags:
        - Lost Item Service
      summary: Report a lost item[cite: 1]
      operationId: createLostItem[cite: 1]
      requestBody:
        required: true[cite: 1]
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/LostItemRequest'
      responses:
        '201':
          description: Created successfully[cite: 1]
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/LostItemResponse'
        '400':
          $ref: '#/components/responses/BadRequest'

  /lost-items/{id}:
    get:
      tags:
        - Lost Item Service
      summary: Retrieve details of a specific lost item[cite: 1]
      operationId: getLostItemById[cite: 1]
      parameters:
        - name: id
          in: path
          required: true
          description: Unique identifier of the lost item
          schema:
            type: integer
      responses:
        '200':
          description: Lost item details retrieved successfully[cite: 1]
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/LostItemResponse'
        '404':
          $ref: '#/components/responses/NotFound'
    put:
      tags:
        - Lost Item Service
      summary: Update an existing lost item's details or status
      operationId: updateLostItem
      parameters:
        - name: id
          in: path
          required: true
          description: Unique identifier of the lost item
          schema:
            type: integer
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/LostItemRequest'
      responses:
        '200':
          description: Updated successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/LostItemResponse'
        '400':
          $ref: '#/components/responses/BadRequest'
        '404':
          $ref: '#/components/responses/NotFound'
    delete:
      tags:
        - Lost Item Service
      summary: Delete a lost item record
      operationId: deleteLostItem
      parameters:
        - name: id
          in: path
          required: true
          description: Unique identifier of the lost item
          schema:
            type: integer
      responses:
        '204':
          description: Deleted successfully (No Content)
        '404':
          $ref: '#/components/responses/NotFound'

  # ==========================================
  # FOUND ITEM SERVICE ENDPOINTS
  # ==========================================
  /found-items:
    get:
      tags:
        - Found Item Service
      summary: List all found items
      operationId: getAllFoundItems
      responses:
        '200':
          description: List of found items retrieved successfully
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/FoundItemResponse'
    post:
      tags:
        - Found Item Service
      summary: Report a new found item[cite: 1]
      operationId: createFoundItem
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/FoundItemRequest'
      responses:
        '201':
          description: Created successfully[cite: 1]
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/FoundItemResponse'
        '400':
          $ref: '#/components/responses/BadRequest'

  /found-items/{id}:
    get:
      tags:
        - Found Item Service
      summary: Retrieve details of a specific found item
      operationId: getFoundItemById
      parameters:
        - name: id
          in: path
          required: true
          description: Unique identifier of the found item
          schema:
            type: integer
      responses:
        '200':
          description: Found item details retrieved successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/FoundItemResponse'
        '404':
          $ref: '#/components/responses/NotFound'
    put:
      tags:
        - Found Item Service
      summary: Update an existing found item's details or status
      operationId: updateFoundItem
      parameters:
        - name: id
          in: path
          required: true
          description: Unique identifier of the found item
          schema:
            type: integer
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/FoundItemRequest'
      responses:
        '200':
          description: Updated successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/FoundItemResponse'
        '400':
          $ref: '#/components/responses/BadRequest'
        '404':
          $ref: '#/components/responses/NotFound'
    delete:
      tags:
        - Found Item Service
      summary: Delete a found item record
      operationId: deleteFoundItem
      parameters:
        - name: id
          in: path
          required: true
          description: Unique identifier of the found item
          schema:
            type: integer
      responses:
        '204':
          description: Deleted successfully (No Content)
        '404':
          $ref: '#/components/responses/NotFound'

  # ==========================================
  # MATCHING SERVICE ENDPOINTS
  # ==========================================
  /matches:
    get:
      tags:
        - Matching Service
      summary: Query AI-assisted potential matches[cite: 1]
      operationId: queryMatches
      responses:
        '200':
          description: List of AI-assisted potential matches retrieved successfully[cite: 1]
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/MatchResponse'

  # ==========================================
  # CLAIM SERVICE ENDPOINTS
  # ==========================================
  /claims:
    post:
      tags:
        - Claim Service
      summary: Submit an ownership claim for an item[cite: 1]
      operationId: createClaim
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/ClaimRequest'
      responses:
        '201':
          description: Created successfully[cite: 1]
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ClaimResponse'
        '400':
          $ref: '#/components/responses/BadRequest'

# ==========================================
# COMPONENTS & SCHEMAS
# ==========================================
components:
  schemas:
    LostItemRequest:
      type: object
      required:
        - userId
        - itemName
        - category
        - location
        - lostDate
      properties:
        userId:
          type: integer[cite: 1]
          example: 1[cite: 1]
        itemName:
          type: string[cite: 1]
          example: "AirPods Pro"[cite: 1]
        category:
          type: string[cite: 1]
          example: "Electronics"[cite: 1]
        description:
          type: string[cite: 1]
          example: "White AirPods Pro with a pink protective case"[cite: 1]
        location:
          type: string[cite: 1]
          example: "UOW Library"[cite: 1]
        lostDate:
          type: string[cite: 1]
          format: date[cite: 1]
          example: "2026-09-15"[cite: 1]

    LostItemResponse:
      type: object
      properties:
        id:
          type: integer
          example: 101
        userId:
          type: integer[cite: 1]
          example: 1[cite: 1]
        itemName:
          type: string[cite: 1]
          example: "AirPods Pro"[cite: 1]
        category:
          type: string[cite: 1]
          example: "Electronics"[cite: 1]
        description:
          type: string[cite: 1]
          example: "White AirPods Pro with a pink protective case"[cite: 1]
        location:
          type: string[cite: 1]
          example: "UOW Library"[cite: 1]
        lostDate:
          type: string[cite: 1]
          format: date[cite: 1]
          example: "2026-09-15"[cite: 1]
        status:
          type: string
          example: "OPEN"

    FoundItemRequest:
      type: object
      required:
        - itemName
        - category
        - location
        - foundDate
      properties:
        itemName:
          type: string
          example: "White Earbuds"
        category:
          type: string
          example: "Electronics"
        description:
          type: string
          example: "Found white wireless earbuds near study desks"
        location:
          type: string
          example: "UOW Library Floor 2"
        foundDate:
          type: string
          format: date
          example: "2026-09-16"

    FoundItemResponse:
      type: object
      properties:
        id:
          type: integer
          example: 201
        itemName:
          type: string
          example: "White Earbuds"
        category:
          type: string
          example: "Electronics"
        description:
          type: string
          example: "Found white wireless earbuds near study desks"
        location:
          type: string
          example: "UOW Library Floor 2"
        foundDate:
          type: string
          format: date
          example: "2026-09-16"
        status:
          type: string
          example: "UNCLAIMED"

    MatchResponse:
      type: object
      properties:
        matchId:
          type: integer
          example: 501
        lostItemId:
          type: integer
          example: 101
        foundItemId:
          type: integer
          example: 201
        confidenceScore:
          type: number
          format: float
          example: 0.95
        matchStatus:
          type: string
          example: "SUGGESTED"

    ClaimRequest:
      type: object
      required:
        - lostItemId
        - foundItemId
        - userId
        - proofDescription
      properties:
        lostItemId:
          type: integer
          example: 101
        foundItemId:
          type: integer
          example: 201
        userId:
          type: integer
          example: 1
        proofDescription:
          type: string
          example: "Serial number matches the box I kept at home."

    ClaimResponse:
      type: object
      properties:
        claimId:
          type: integer
          example: 901
        lostItemId:
          type: integer
          example: 101
        foundItemId:
          type: integer
          example: 201
        userId:
          type: integer
          example: 1
        status:
          type: string
          example: "SUBMITTED"
        createdAt:
          type: string
          format: date-time
          example: "2026-09-17T12:00:00Z"

    ErrorResponse:
      type: object
      properties:
        code:
          type: string
          example: "NOT_FOUND"
        message:
          type: string
          example: "Requested resource was not found"
        timestamp:
          type: string
          format: date-time
          example: "2026-09-17T12:00:00Z"

  responses:
    BadRequest:
      description: Invalid request parameters or body payload
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/ErrorResponse'
    NotFound:
      description: Requested resource was not found
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/ErrorResponse'
