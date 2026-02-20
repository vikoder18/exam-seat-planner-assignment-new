# 🎓 College Exam Seat Planner

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-green?style=for-the-badge&logo=springboot)
![Angular](https://img.shields.io/badge/Angular-17-red?style=for-the-badge&logo=angular)
![H2](https://img.shields.io/badge/Database-H2_In--Memory-blue?style=for-the-badge)
![Maven](https://img.shields.io/badge/Build-Maven-yellow?style=for-the-badge&logo=apachemaven)

> **A full-stack exam seat allocation system that intelligently assigns classrooms using a two-phase greedy algorithm — minimising rooms used while always preferring lower-floor accessibility.**

</div>

---

## 📌 Table of Contents

- [Overview](#-overview)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Algorithm Deep Dive](#-algorithm-deep-dive)
- [API Reference](#-api-reference)
- [Data Model](#-data-model)
- [Getting Started](#-getting-started)
- [Features](#-features)
- [Screenshots](#-screenshots)
- [Error Handling](#-error-handling)

---

## 🧭 Overview

The College Exam Seat Planner solves a real-world resource allocation problem:

> *"Given a set of classrooms across multiple floors and a number of students, find the minimum number of rooms needed — always preferring rooms on lower floors for accessibility."*

This is solved using a **two-phase greedy algorithm** built in Java Spring Boot, exposed via a clean REST API, and visualised through an Angular frontend with a live stats dashboard.

---

## 🛠 Tech Stack

| Layer | Technology | Purpose |
|---|---|---|
| **Backend** | Java 17 + Spring Boot 3.2 | REST API, business logic |
| **ORM** | Spring Data JPA + Hibernate | Database abstraction |
| **Database** | H2 In-Memory | Zero-setup persistent store |
| **Validation** | Jakarta Bean Validation | Input validation layer |
| **Frontend** | Angular 17 | Single-page application |
| **Styling** | SCSS + Inter Font | Responsive UI |
| **Build** | Maven | Dependency management |

---

## 📁 Project Structure

```
📦 exam-seat-planner
 ┣ 📂 src/main/java/com/examplanner
 ┃ ┣ 📂 controller
 ┃ ┃ ┗ 📄 ClassroomController.java     ← REST endpoints
 ┃ ┣ 📂 model
 ┃ ┃ ┣ 📄 Classroom.java               ← JPA entity
 ┃ ┃ ┗ 📄 AllocationResult.java        ← Response DTO
 ┃ ┣ 📂 repository
 ┃ ┃ ┗ 📄 ClassroomRepository.java     ← Data access + custom queries
 ┃ ┣ 📂 service
 ┃ ┃ ┗ 📄 ClassroomService.java        ← Greedy algorithm lives here
 ┃ ┣ 📂 exception
 ┃ ┃ ┣ 📄 GlobalExceptionHandler.java  ← Centralised error handling
 ┃ ┃ ┣ 📄 DuplicateRoomException.java
 ┃ ┃ ┗ 📄 InsufficientSeatsException.java
 ┃ ┗ 📄 ExamSeatPlannerApplication.java
 ┗ 📄 pom.xml

📦 exam-seat-planner-frontend
 ┣ 📂 src/app
 ┃ ┣ 📂 components
 ┃ ┃ ┣ 📂 add-classroom
 ┃ ┃ ┣ 📂 view-classrooms
 ┃ ┃ ┗ 📂 allocate-exam
 ┃ ┣ 📂 services
 ┃ ┃ ┗ 📄 classroom.service.ts         ← HTTP calls to backend
 ┃ ┣ 📄 app.module.ts
 ┃ ┣ 📄 app-routing.module.ts
 ┃ ┗ 📄 app.component.ts
 ┗ 📄 src/styles.scss
```

---

## 🧠 Algorithm Deep Dive

The allocation logic in `ClassroomService.java` runs in **two phases** to satisfy both requirements simultaneously.

### The Problem

Two requirements that can conflict:
```
✅ Prefer lower floors     →  may need MORE rooms
✅ Use minimum rooms       →  may use HIGHER floors
```

A naive single-pass greedy can't satisfy both. The two-phase approach solves this.

---

### Phase 1 — Floor-Priority Greedy

Rooms are pre-sorted by the repository: **floor ASC → capacity DESC**

```
Iterate through the sorted list, accumulate seats, stop the moment
seats collected ≥ totalStudents.
```

This guarantees lower floors are always exhausted first.

```
Example: 280 students, rooms available:
┌──────┬──────────┬───────┐
│ Room │ Capacity │ Floor │
├──────┼──────────┼───────┤
│ 005  │    60    │   0   │  ← picked (running: 60)
│ 002  │    59    │   0   │  ← picked (running: 119)
│ 001  │    38    │   0   │  ← picked (running: 157)
│ 102  │    71    │   1   │  ← picked (running: 228)
│ 103  │    82    │   1   │  ← picked (running: 310 ✅)
└──────┴──────────┴───────┘
Phase 1 result: 5 rooms, minimum floor = Ground (0)
```

---

### Phase 2 — Minimum Room Optimisation

> *"Can we do better with fewer rooms, while still including the lowest floor?"*

```
1. Lock in the LARGEST room from the minimum floor (anchor — guarantees
   floor priority is honoured)
2. Fill remaining seats by picking largest-capacity rooms from anywhere
3. If Phase 2 uses fewer rooms than Phase 1 → use Phase 2
   Otherwise → keep Phase 1 (it already has strict floor ordering)
```

```
Phase 2 with same example:
Anchor: 005 (60 seats, Ground) — floor priority locked in ✅
Need 220 more seats, pick by capacity DESC:
  + 103 (82) → 142 ❌
  + 104 (80) → 222 ❌
  + 105 (75) → 297 ✅

Phase 2 result: 4 rooms (005, 103, 104, 105)
4 < 5 → Phase 2 wins ✅
```

**Final result sorted by floor ASC for display:**

```
┌──────┬──────────┬─────────┐
│ 005  │    60    │ Ground  │  ← floor priority honoured ✅
│ 103  │    82    │ Floor 1 │
│ 104  │    80    │ Floor 1 │
│ 105  │    75    │ Floor 1 │
└──────┴──────────┴─────────┘
Minimum rooms: 4 ✅   Lower floor included: ✅
```

---

## 📡 API Reference

Base URL: `http://localhost:8080/api`

### Add Classroom
```http
POST /api/classrooms
Content-Type: application/json

{
  "roomId": "A101",
  "capacity": 60,
  "floorNo": 0,
  "nearWashroom": true
}
```
**Response:** `201 Created` → returns saved classroom object

---

### Get All Classrooms
```http
GET /api/classrooms
```
**Response:** `200 OK` → array of all classrooms

---

### Allocate Exam Seats
```http
POST /api/classrooms/allocate
Content-Type: application/json

{
  "totalStudents": 280
}
```
**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Allocation successful. 4 room(s) assigned.",
  "totalStudents": 280,
  "totalSeatsAllocated": 297,
  "roomsUsed": 4,
  "allocatedClassrooms": [ ... ]
}
```

---

## 📊 Data Model

```
Classroom
─────────────────────────────────────
  id            Long        Auto-generated primary key
  roomId        String      Unique room identifier (e.g. "A101")
  capacity      Integer     Number of seats (min: 1)
  floorNo       Integer     Floor number (min: 0 = ground)
  nearWashroom  Boolean     Proximity to washroom
```

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- Node.js 18+
- Angular CLI

---

### Backend Setup

```bash
# Clone / navigate into backend folder
cd exam-seat-planner-backend

# Run the application
./mvnw spring-boot:run
```

> API is live at `http://localhost:8080`
> H2 Console available at `http://localhost:8080/h2-console`

---

### Frontend Setup

```bash
# Navigate into frontend folder
cd exam-seat-planner-frontend

# Install dependencies (uses public npm registry)
npm install --registry https://registry.npmjs.org/

# Start the dev server
ng serve
```

> App is live at `http://localhost:4200`

---

### Project-level `.npmrc` (if on a corporate network)

If your machine uses a private npm registry, create `.npmrc` inside the frontend folder:

```
registry=https://registry.npmjs.org/
```

This scopes the public registry to this project only without affecting your global config.

---

## ✨ Features

| Feature | Description |
|---|---|
| ➕ **Add Classroom** | Form with live validation — Room ID, Capacity, Floor, Washroom proximity |
| 📋 **View Classrooms** | Full table with refresh, empty state, and live seat totals |
| 🎯 **Allocate Exam** | Two-phase greedy allocation with detailed output panel |
| 📊 **Live Stats Bar** | Always-visible room count and total seats in the navbar |
| ❌ **Error Handling** | Structured JSON error responses for all edge cases |
| 🔁 **Duplicate Guard** | Rejects duplicate Room IDs with a clear message |

---

## 🛡 Error Handling

All errors return structured JSON — never raw Spring error pages.

| Scenario | HTTP Status | Message |
|---|---|---|
| Duplicate Room ID | `409 Conflict` | `Room ID 'X' already exists.` |
| Not enough seats | `422 Unprocessable` | `Not enough seats available. Required: X, Available: Y` |
| Invalid input fields | `400 Bad Request` | Field-level validation messages |
| Missing request body fields | `400 Bad Request` | `'totalStudents' field is required` |
| Zero or negative students | `400 Bad Request` | `Total students must be a positive number` |

---

## 🏗 Design Decisions

**Why H2?** Zero configuration, resets cleanly between runs — perfect for a demo/assignment context without needing a MySQL/Postgres install.

**Why constructor injection over `@Autowired`?** Constructor injection is the Spring-recommended approach — it makes dependencies explicit, supports immutability, and makes unit testing easier.

**Why two-phase over a single sort?** A single sort by capacity DESC gives minimum rooms but ignores floors. A single sort by floor ASC gives floor preference but may waste rooms. The two-phase approach satisfies both requirements provably.

**Why `@RestControllerAdvice`?** Centralising error handling in one class keeps controllers clean — they only deal with the happy path.

---

<div align="center">

Built with ☕ Java, 🍃 Spring Boot and 🅰️ Angular

</div>
