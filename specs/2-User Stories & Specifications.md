# User Stories & Specifications

## User Story Overview

| Service | User Story ID | User Story Name | Role(s) |
| :--- | :--- | :--- | :--- |
| Lost Service | B1 | Create / Update / Delete a lost item request | User, Admin |
| Lost Service | B2 | List all lost items | User, Admin |
| Found Service | L1 | Create / Update / Delete a lost item registry | User, Admin |
| Found Service | L2 | List all lost requests | User, Admin |

---

## Item Domain Schema
All item records (lost or found) share the following core attributes:
- **`itemId`** (Long): Unique identifier for the item record.
- **`name`** (String): Name/title of the item (e.g., "iPhone 15 Pro", "Blue Backpack").
- **`category`** (String): Category of the item (e.g., "Electronics", "Bags", "Keys", "Documents").
- **`location`** (String): Location where the item was lost or found.
- **`status`** (Enum): Status of the item record:
  - `LOST`: Item has been reported lost. (e.g., has not been found)
  - `FOUND`: Item has been reported found. (e.g., has been found, the owner has contacted the finder and proven ownership)

---

## Lost Service

### User Story B1: Create / Update / Delete a lost item request (this is for user posting request, and looking at registry to find possible lost item)
- **As a User**, I want to create a new lost item record with details such as `name`, `category`, `location`, and initial `status` set to `LOST`.
- **As a User**, I want to update the lost item's 'status' to 'FOUND ' or 'LOST'
- **As a User/Admin**, I want to retrieve a lost item's information by its `itemId` or `name`.
- **As a User/Admin**, I want to update an existing lost item's details (such as `location`, `status`, or `color`) by its `itemId`.
- **As a User/Admin**, I want to delete a lost item record by its `itemId`.
- 

### User Story B2: List all lost items
- **As a User/Admin**, I want to retrieve a list of all registered lost items.

---

## Found Service

### User Story L1: Create / Update / Delete a lost item registry (this is for user posting lost item registry, and looking at request to find possible original owner)
- **As a User**, I want to create a new found item record with details such as `name`, `category`, `location`, and initial `status` set to `FOUND`.
- **As a User**, I want to update the lost item's 'status' to 'FOUND ' or 'LOST' (most likely will not be used)
- **As a User/Admin**, I want to retrieve a lost item's information by its `itemId` or `name`.
- **As a User/Admin**, I want to update an existing lost item's details (such as `location`, `status`, or `color`) by its `itemId`.
- **As a User/Admin**, I want to delete a lost item record by its `itemId`.

### User Story L2: List all lost items
- **As a User/Admin**, I want to retrieve a list of all lost item request.
