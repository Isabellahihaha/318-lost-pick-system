# User Stories & Specifications

## User Story Overview

| Service Module | User Story ID | User Story Name | Role(s) |
| :--- | :--- | :--- | :--- |
| User Service | U1 | Register and manage user profiles | Student, Admin |
| Lost Service | B1 | Create / Update / Delete a lost item request | User, Admin |
| Lost Service | B2 | List all lost items | User, Admin |
| Found Service | L1 | Create / Update / Delete a found item registry | User, Finder, Admin |
| Found Service | L2 | List all registered found items | User, Admin |
| Matching Service | M1 | AI-powered text matching & similarity scoring | User, System |
| Claim Service | C1 | Submit and verify ownership claims | User, Finder, Admin |

---

## Core Item Domain Attributes
All item records (lost or found) share the following core attributes across respective services:
- **`id`** (Long): Unique identifier for the item record.
- **`itemName`** (String): Title or name of the item (e.g., "AirPods Pro", "Blue Backpack").
- **`category`** (String): Classification category (e.g., "Electronics", "Bags", "Keys").
- **`description`** (String): Detailed distinguishing marks, color, or condition.
- **`location`** (String): Last known loss location or discovery site.
- **`status`** (Enum): State of the record (`OPEN`, `MATCHED`, `RECOVERED` for lost items; `UNCLAIMED`, `PENDING_VERIFICATION`, `CLAIMED` for found items).

---

## 1. User Management Service (`user-service`)
### User Story U1: User Profile & Identity Management
- **As a User**, I want to register an account with my name, email, and university role so I can report lost items and submit ownership claims.
- **As an Admin**, I want to manage user accounts and system permissions.

---

## 2. Lost Item Service (`lost-service`)
### User Story B1: Create / Update / Delete a lost item request
- **As a User**, I want to create a new lost item record with details (`itemName`, `category`, `description`, `location`, `lostDate`) with status initialized to `OPEN`.
- **As a User/Admin**, I want to retrieve, update, or delete an existing lost item request by its unique identifier (`id`).

### User Story B2: List all lost items
- **As a User/Admin**, I want to query a complete list of registered lost items to cross-reference with found item registries.

---

## 3. Found Item Service (`found-service`)
### User Story L1: Create / Update / Delete a found item registry
- **As a Finder/Admin**, I want to create a found item record when an item is discovered, setting status to `UNCLAIMED`.
- **As a User/Admin**, I want to update or remove found item registry entries.

### User Story L2: List all found items
- **As a User/Admin**, I want to retrieve a list of all logged found items.

---

## 4. Matching Service (`matching-service`)
### User Story M1: AI-Powered Text Matching & Ranking
- **As a User**, I want the system to leverage AI (LangChain4j) to analyze natural language descriptions, handle vague inputs, and calculate confidence similarity scores between lost and found items.
- **As a System**, I want to query AI-assisted potential matches (`/matches`) to surface the most relevant pairings automatically.

---

## 5. Claim & Recovery Service (`claim-service`)
### User Story C1: Ownership Claim & Human-in-the-Loop Verification
- **As a User**, I want to submit an ownership claim (`/claims`) by providing proof descriptions or serial numbers linking a lost item report to a found item entry.
- **As a Finder/Admin**, I want to review submitted claims and make a final verification decision (human-in-the-loop) to transition the item to `RECOVERED`.
