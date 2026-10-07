# User Stories & Specifications

## User Story Overview

| Service | User Story ID | User Story Name | Role(s) |
| :--- | :--- | :--- | :--- |
| Lost Service | B1 | Create, Read, Update, or Delete a lost item request | User, Admin |
| Lost Service | B2 | List all registered lost items | User, Admin |
| Found Service | L1 | Create, Read, Update, or Delete a found item registry | User, Admin |
| Found Service | L2 | List all registered found items | User, Admin |

---

## Item Domain Schema
All item records share the following core attributes across respective services:
- **`id`** (Long): Unique identifier for the item record.
- **`itemName`** (String): Name/title of the item (e.g., "AirPods Pro", "Blue Backpack")[cite: 1].
- **`category`** (String): Category of the item (e.g., "Electronics", "Bags", "Keys")[cite: 1].
- **`description`** (String): Detailed distinguishing features.
- **`location`** (String): Location where the item was lost or found[cite: 1].
- **`status`** (Enum): State of the item record (`OPEN`, `MATCHED`, `RECOVERED`, `CLOSED` for lost items; `UNCLAIMED`, `PENDING_VERIFICATION`, `CLAIMED`, `ARCHIVED` for found items).

---

## Lost Service (`lost-service`)

### User Story B1: Create / Update / Delete a lost item request
- **As a User**, I want to create a new lost item record with details such as `itemName`, `category`, `location`, `lostDate`, and initial `status` set to `OPEN`.
- **As a User/Admin**, I want to retrieve a specific lost item's information by its unique identifier (`id`).
- **As a User/Admin**, I want to update an existing lost item's details (such as `location`, `description`, or `status`) by its `id`.
- **As a User/Admin**, I want to delete a lost item record by its `id`.

### User Story B2: List all lost items
- **As a User/Admin**, I want to retrieve a comprehensive list of all registered lost item requests for browsing and matching.

---

## Found Service (`found-service`)

### User Story L1: Create / Update / Delete a found item registry
- **As a Finder/Admin**, I want to create a new found item record with details such as `itemName`, `category`, `location`, `foundDate`, and initial `status` set to `UNCLAIMED`.
- **As a User/Admin**, I want to retrieve a specific found item's information by its unique identifier (`id`).
- **As a User/Admin**, I want to update an existing found item's details or `status` by its `id`.
- **As a User/Admin**, I want to delete a found item record by its `id`.

### User Story L2: List all found items
- **As a User/Admin**, I want to retrieve a list of all logged found items to evaluate potential matches against lost requests.
