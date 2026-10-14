OpenAPI 3.0.3 · Version 1.0.1

Microservice APIs for the Lost Item, Found Item, Matching, and Claim services.
The machine-readable spec is in `openapi.yaml`.

## Services

| Service | Base URL (local) |
|---|---|
| Lost Item Service | `http://localhost:8081/api` |
| Found Item Service | `http://localhost:8082/api` |
| Matching Service | `http://localhost:8083/api` |
| Claim Service | `http://localhost:8084/api` |

## Endpoint Summary

| Service | Method | Path | Description | Success | Errors |
|---|---|---|---|---|---|
| Lost Item | GET | `/lost-items` | List all lost items | 200 | – |
| Lost Item | POST | `/lost-items` | Report a lost item | 201 | 400 |
| Lost Item | GET | `/lost-items/{id}` | Get a lost item | 200 | 404 |
| Lost Item | PUT | `/lost-items/{id}` | Update a lost item's details or status | 200 | 400, 404 |
| Lost Item | DELETE | `/lost-items/{id}` | Delete a lost item | 204 | 404 |
| Found Item | GET | `/found-items` | List all found items | 200 | – |
| Found Item | POST | `/found-items` | Report a found item | 201 | 400 |
| Found Item | GET | `/found-items/{id}` | Get a found item | 200 | 404 |
| Found Item | PUT | `/found-items/{id}` | Update a found item's details or status | 200 | 400, 404 |
| Found Item | DELETE | `/found-items/{id}` | Delete a found item | 204 | 404 |
| Matching | GET | `/matches` | Query AI-assisted potential matches | 200 | – |
| Claim | POST | `/claims` | Submit an ownership claim | 201 | 400 |

---

## Lost Item Service

Base URL: `http://localhost:8081/api`

### `GET /lost-items`
Returns an array of [LostItemResponse](#lostitemresponse).

### `POST /lost-items`
Report a lost item. Body: [LostItemRequest](#lostitemrequest). Returns `201` with [LostItemResponse](#lostitemresponse).

### `GET /lost-items/{id}`
| Parameter | In | Type | Required | Description |
|---|---|---|---|---|
| `id` | path | integer | yes | Lost item ID |

Returns `200` with [LostItemResponse](#lostitemresponse), or `404`.

### `PUT /lost-items/{id}`
Same path parameter as above. Body: [LostItemRequest](#lostitemrequest). Returns `200` with [LostItemResponse](#lostitemresponse), `400`, or `404`.

### `DELETE /lost-items/{id}`
Returns `204 No Content`, or `404`.

---

## Found Item Service

Base URL: `http://localhost:8082/api`

### `GET /found-items`
Returns an array of [FoundItemResponse](#founditemresponse).

### `POST /found-items`
Report a found item. Body: [FoundItemRequest](#founditemrequest). Returns `201` with [FoundItemResponse](#founditemresponse).

### `GET /found-items/{id}`
| Parameter | In | Type | Required | Description |
|---|---|---|---|---|
| `id` | path | integer | yes | Found item ID |

Returns `200` with [FoundItemResponse](#founditemresponse), or `404`.

### `PUT /found-items/{id}`
Same path parameter as above. Body: [FoundItemRequest](#founditemrequest). Returns `200` with [FoundItemResponse](#founditemresponse), `400`, or `404`.

### `DELETE /found-items/{id}`
Returns `204 No Content`, or `404`.

---

## Matching Service

Base URL: `http://localhost:8083/api`

### `GET /matches`
Query AI-assisted potential matches. Returns an array of [MatchResponse](#matchresponse).

---

## Claim Service

Base URL: `http://localhost:8084/api`

### `POST /claims`
Submit an ownership claim. Body: [ClaimRequest](#claimrequest). Returns `201` with [ClaimResponse](#claimresponse), or `400`.

---

## Schemas

### LostItemRequest
| Field | Type | Required | Example |
|---|---|---|---|
| `userId` | integer | yes | `1` |
| `itemName` | string | yes | `"AirPods Pro"` |
| `category` | string | yes | `"Electronics"` |
| `description` | string | no | `"White AirPods Pro with a pink protective case"` |
| `location` | string | yes | `"UOW Library"` |
| `lostDate` | string (date) | yes | `"2026-09-15"` |

### LostItemResponse
All fields from LostItemRequest, plus:

| Field | Type | Example |
|---|---|---|
| `id` | integer | `101` |
| `status` | string | `"OPEN"` |

### FoundItemRequest
| Field | Type | Required | Example |
|---|---|---|---|
| `itemName` | string | yes | `"White Earbuds"` |
| `category` | string | yes | `"Electronics"` |
| `description` | string | no | `"Found white wireless earbuds near study desks"` |
| `location` | string | yes | `"UOW Library Floor 2"` |
| `foundDate` | string (date) | yes | `"2026-09-16"` |

### FoundItemResponse
All fields from FoundItemRequest, plus:

| Field | Type | Example |
|---|---|---|
| `id` | integer | `201` |
| `status` | string | `"UNCLAIMED"` |

### MatchResponse
| Field | Type | Example |
|---|---|---|
| `matchId` | integer | `501` |
| `lostItemId` | integer | `101` |
| `foundItemId` | integer | `201` |
| `confidenceScore` | number (float) | `0.95` |
| `matchStatus` | string | `"SUGGESTED"` |

### ClaimRequest
| Field | Type | Required | Example |
|---|---|---|---|
| `lostItemId` | integer | yes | `101` |
| `foundItemId` | integer | yes | `201` |
| `userId` | integer | yes | `1` |
| `proofDescription` | string | yes | `"Serial number matches the box I kept at home."` |

### ClaimResponse
| Field | Type | Example |
|---|---|---|
| `claimId` | integer | `901` |
| `lostItemId` | integer | `101` |
| `foundItemId` | integer | `201` |
| `userId` | integer | `1` |
| `status` | string | `"SUBMITTED"` |
| `createdAt` | string (date-time) | `"2026-09-17T12:00:00Z"` |

### ErrorResponse
Returned for `400 Bad Request` and `404 Not Found`.

| Field | Type | Example |
|---|---|---|
| `code` | string | `"NOT_FOUND"` |
| `message` | string | `"Requested resource was not found"` |
| `timestamp` | string (date-time) | `"2026-09-17T12:00:00Z"` |
