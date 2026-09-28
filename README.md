# MentorConnect — Alumni-Student Mentorship Matching Platform

MentorConnect is an enterprise-grade backend and web application built with **Java 21**, **Spring Boot 3.3.4**, **Spring Data JPA**, **MySQL 8.0**, and a **Pro-grade Lite Themed Dashboard**.

---

## 🚀 Live Access

- **Website Dashboard:** [http://localhost:8080/](http://localhost:8080/)
- **Swagger Interactive API Docs:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Postman Collection Download:** [http://localhost:8080/MentorConnect.postman_collection.json](http://localhost:8080/MentorConnect.postman_collection.json) or directly in project root as `MentorConnect.postman_collection.json`.

---

## 🗄️ Database Details

- **Database Engine:** MySQL 8.0
- **Database Name:** `mentorconnect_db` (Separated, isolated database)
- **Username:** `root`
- **Password:** `root`
- **Port:** `3306`
- **SQL Initialization Script:** [db_setup.sql](file:///c:/mentorconnectPROJECT/db_setup.sql)

---

## 🌟 Core Implemented Features & Business Rules

1. **Alumni Registration & Capacity Limit:**
   - Alumni register with their expertise tags (e.g., *Java, Spring Boot, System Design*) and declared **Maximum Concurrent Mentee Limit** (`maxMentees`).
2. **Student Registration & Interest Profiling:**
   - Students register and declare target interest learning tags.
3. **Smart Matching Engine (Highest Overlap First):**
   - Calculates overlapping tags between student and alumni.
   - Strictly orders recommendations by tag overlap count descending (highest first).
4. **Strict Service-Layer Capacity Rule Enforcement:**
   - When matching student to mentor (`POST /api/mentorship-pairs`), Spring Boot verifies `activePairsCount < maxMentees`.
   - If exceeded, immediately rejects request with **HTTP 400 Bad Request** and error message: `Mentor has reached their declared maximum concurrent mentee limit. Cannot accept new mentees.`
5. **Mentoring Session Activity Tracking:**
   - Track scheduled sessions with meeting links, time, and topic.
   - Update completion status (`SCHEDULED` ➔ `COMPLETED` / `CANCELLED`).
6. **Mentorship Engagement Reports:**
   - View sessions held per mentor-mentee pair, completed minutes, and completion percentage (`GET /api/reports/engagement`).

---

## 📮 How to Connect Postman

1. Open **Postman**.
2. Click **Import** (Top left).
3. Select the file: `c:\mentorconnectPROJECT\MentorConnect.postman_collection.json`.
4. Ensure the `baseUrl` collection variable is set to `http://localhost:8080`.
5. Run the requests across all 7 organized folders:
   - **1. Interest Tags**
   - **2. Alumni Mentors**
   - **3. Students**
   - **4. Smart Matching Engine**
   - **5. Mentorship Pairings** (includes positive path and capacity limit negative test)
   - **6. Mentoring Sessions**
   - **7. Mentorship Reports**

---

## 💻 Commands to Run

To run the Spring Boot server manually at any time:
```powershell
cd c:\mentorconnectPROJECT
.\mvn.cmd spring-boot:run
```
