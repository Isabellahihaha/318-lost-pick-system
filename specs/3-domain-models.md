# Domain Models: AI-Powered Lost & Found Recovery Platform

This document outlines the core domain entities, attributes, and cross-service relationships for the AI-Powered Lost & Found Recovery Platform, aligned with its microservices architecture and DDD bounded contexts.

---

## 1. User Management Context
Handles user identity, registration, login, and contact information.

### User Entity
* **Attributes:**
  * `id` (Long / UUID): Unique identifier for the user.
  * `name` (String): Full name of the user.
  * `email` (String): Unique email address for notifications and login.
  * `role` (Enum): System role (`STUDENT`, `ADMIN`).
  * `created_at` (Timestamp): Account registration timestamp.

---

## 2. Lost Item Management Context
Manages user-submitted lost item reports, item descriptions, and current status.

### LostItem Entity
* **Attributes:**
  * `id` (Long / UUID): Unique identifier for the lost report.
  * `userId` (Long): Reference to the user who lost the item.
  * `itemName` (String): Name or short title of the item (e.g., "AirPods Pro").
  * `category` (String): Classification category (e.g., "Electronics").
  * `description` (String): Detailed description, features, or distinguishing marks (e.g., "White AirPods Pro with a pink protective case").
  * `location` (String): Last known location where it was lost (e.g., "UOW Library").
  * `lostDate` (Date): Date when the item was lost.
  * `status` (Enum): Current state of the report (`OPEN`, `MATCHED`, `RECOVERED`, `CLOSED`).

---

## 3. Found Item Management Context
Manages found item reports registered by finders or security personnel.

### FoundItem Entity
* **Attributes:**
  * `id` (Long / UUID): Unique identifier for the found report.
  * `finderId` (Long): Reference to the user or finder who logged the item.
  * `itemName` (String): Name or title of the found item.
  * `category` (String): Classification category.
  * `description` (String): Detailed description or physical condition.
  * `location` (String): Location where the item was found.
  * `foundDate` (Date): Date when the item was found.
  * `status` (Enum): Current state (`UNCLAIMED`, `PENDING_VERIFICATION`, `CLAIMED`, `ARCHIVED`).

---

## 4. Matching Service Context
Handles AI-assisted search, filtering, and intelligent cross-referencing between lost and found items.

### PotentialMatch Entity
* **Attributes:**
  * `id` (Long / UUID): Unique identifier for the match record.
  * `lostItemId` (Long): Reference to the `LostItem`.
  * `foundItemId` (Long): Reference to the `FoundItem`.
  * `matchScore` (Float): AI-calculated ranking or confidence score.
  * `matchStatus` (Enum): State of the match suggestion (`SUGGESTED`, `REVIEWED`, `REJECTED`, `LINKED`).

---

## 5. Claim & Recovery Management Context
Handles ownership claims, verification workflows, and final item recovery.

### OwnershipClaim Entity
* **Attributes:**
  * `id` (Long / UUID): Unique identifier for the claim.
  * `lostItemId` (Long): Reference to the lost item report.
  * `foundItemId` (Long): Reference to the found item report.
  * `claimantId` (Long): Reference to the user submitting the claim.
  * `proofDescription` (String): Evidence or detailed proof provided to verify ownership.
  * `claimStatus` (Enum): Workflow state (`SUBMITTED`, `PENDING_FINDER_VERIFICATION`, `VERIFIED`, `REJECTED`, `RECOVERED`).

---

## Summary of Relationships
* **User to LostItem / FoundItem:** One-to-Many (A user can report multiple lost or found items).
* **LostItem to FoundItem (via PotentialMatch):** Many-to-Many (AI links potential corresponding items based on descriptions and locations).
* **LostItem / FoundItem to OwnershipClaim:** One-to-One / One-to-Many (A specific item pair undergoes a verification and claim recovery lifecycle managed with human-in-the-loop verification by the finder).
