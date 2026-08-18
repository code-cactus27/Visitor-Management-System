# 🏢 Visitor Management System
 
A full-stack, role-based Visitor Management System designed to streamline visitor registration, visit tracking, access management, gate-pass handling, reporting, and administrative operations.
 
Built using **Angular, Spring Boot, Spring Security, JPA/Hibernate, and MySQL**.
 
## 📌 Overview
 
Managing visitors manually can lead to inefficient registration processes, poor record management, limited access control, and difficulty tracking visitor movement.
 
The Visitor Management System provides a centralized digital platform for managing the complete visitor lifecycle—from registration and visit management to access control, gate passes, feedback, reporting, and administrative configuration.
 
The system is designed around role-based access, ensuring that different users interact with the application according to their responsibilities.
 
```
Visitor Management System
│
├── Authentication
├── Role-Based Access Control
│
├── Visitor Management
│   ├── Register Visitor
│   ├── Manage Visitor Information
│   └── Track Visitor Records
│
├── Visit Management
│   ├── Create Visits
│   ├── Track Visit Status
│   └── Manage Visit Lifecycle
│
├── Gate Pass Management
│
├── User & Role Management
│
├── Activity Logging
│
├── Feedback Management
│
├── Reports
│
└── System Configuration
```
 
## ✨ Features
 
### 🔐 Authentication & Authorization
- Secure user authentication
- Role-based access control
- Login functionality
- Forgot password flow
- Password reset functionality
- Protected application routes
- Spring Security integration
### 👤 Visitor Management
- Register and manage visitors
- Store visitor information
- View visitor records
- Track visitor-related activity
### 📅 Visit Management
- Create and manage visits
- Track visitor visits
- Maintain visit lifecycle information
- Associate visitors with visit records
### 🎫 Gate Pass Management
- Generate and manage gate-pass information
- Support controlled visitor access
- Integrate access flow with visit management
### 👥 User & Role Management
- Manage system users
- Assign roles
- Control access based on user responsibilities
### 📊 Reports
- Generate visitor and visit-related reports
- Provide administrative visibility into system activity
### 📝 Feedback
- Collect visitor feedback
- Manage feedback records
### 📜 Activity Logging
- Track important system activities
- Improve visibility and auditability
### ⚙️ System Configuration
- Manage configurable system settings
## 👥 Role-Based System
 
The application separates functionality according to different user roles.
 
```
                         ┌──────────────────┐
                         │      USER        │
                         └────────┬─────────┘
                                  │
                                  ▼
                         ┌──────────────────┐
                         │ Authentication   │
                         └────────┬─────────┘
                                  │
                 ┌────────────────┼────────────────┐
                 │                │                │
                 ▼                ▼                ▼
 
 
           Administrator      Receptionist    Security Officer
                 │                │                │
                 ▼                ▼                ▼
 
 
            Management      Visitor/Visit      Access & Gate
            Operations       Operations         Operations
```
 
The frontend source is organized into feature areas for:
 
- Admin
- Authentication
- Receptionist
- Security Officer
- System Administrator
## 🏗️ System Architecture
 
```
┌──────────────────────────────────────────────────────┐
│                    ANGULAR FRONTEND                  │
│                                                      │
│  Pages • Components • Forms • Routing • Services     │
└───────────────────────┬──────────────────────────────┘
                        │
                        │ HTTP / JSON
                        ▼
┌──────────────────────────────────────────────────────┐
│                  SPRING BOOT BACKEND                 │
│                                                      │
│  Spring Security                                     │
│        │                                             │
│        ▼                                             │
│  Controllers                                         │
│        │                                             │
│        ▼                                             │
│  Services                                            │
│        │                                             │
│        ▼                                             │
│  Repositories                                        │
│        │                                             │
│        ▼                                             │
│  JPA / Hibernate                                     │
└───────────────────────┬──────────────────────────────┘
                        │
                        ▼
                 ┌─────────────┐
                 │    MySQL    │
                 └─────────────┘
```
 
## 🔄 Request Flow
 
A typical request flows through the application like this:
 
```
User
 │
 │ Interacts with UI
 ▼
Angular Component
 │
 │ HTTP Request
 ▼
Spring Security
 │
 ▼
Controller
 │
 ▼
Service
 │
 ▼
Repository
 │
 ▼
JPA / Hibernate
 │
 ▼
MySQL Database
 │
 │
 ▼
JSON Response
 │
 ▼
Angular Service / Component
 │
 ▼
Updated User Interface
```
 
## 🛠️ Technology Stack
 
### Frontend
- Angular
- TypeScript
- HTML
- CSS
- Angular Forms
- Angular Routing
- HTTP Client
### Backend
- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Hibernate
- Maven
- Lombok
- ModelMapper
- Bean Validation
- Log4j2
- OpenAPI / Swagger
### Database
- MySQL
## 📂 Project Structure
 
```
Visitor-Management-System/
│
├── vm-frontend/
│   │
│   ├── src/
│   │   └── app/
│   │       │
│   │       ├── core/
│   │       ├── shared/
│   │       │
│   │       ├── features/
│   │       │   ├── admin/
│   │       │   ├── auth/
│   │       │   ├── receptionist/
│   │       │   ├── security-officer/
│   │       │   └── system-admin/
│   │       │
│   │       ├── app-routing.module.ts
│   │       └── app.module.ts
│   │
│   └── package.json
│
├── vm-backend/
│   │
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │
│   │           ├── controller/
│   │           ├── service/
│   │           ├── repository/
│   │           ├── entity/
│   │           ├── dto/
│   │           ├── config/
│   │           ├── security/
│   │           └── exception/
│   │
│   └── pom.xml
│
└── README.md
```
 
## 🚀 Getting Started
 
### Prerequisites
 
Make sure the following are installed:
 
- Node.js
- npm
- Angular CLI
- Java 17
- Maven
- MySQL
### 🖥️ Frontend Setup
 
Navigate to the frontend directory:
 
```bash
cd vm-frontend
```
 
Install dependencies:
 
```bash
npm install
```
 
Start the Angular development server:
 
```bash
ng serve
```
 
The application will typically be available at:
 
```
http://localhost:4200
```
 
### ⚙️ Backend Setup
 
Navigate to the backend directory:
 
```bash
cd vm-backend
```
 
Make sure Java 17 is configured:
 
```bash
java -version
```
 
Configure the MySQL database according to the backend application configuration.
 
The project is configured to use a MySQL database named:
 
```
visitor_management
```
 
Then start the Spring Boot application:
 
```bash
./mvnw spring-boot:run
```
 
On Windows:
 
```bash
mvnw.cmd spring-boot:run
```
 
The backend is configured to run on:
 
```
http://localhost:8765
```
 
## 🔌 Running the Complete Application
 
Start the system in this order:
 
```
1. Start MySQL
        │
        ▼
2. Create / configure visitor_management database
        │
        ▼
3. Start Spring Boot backend
        │
        ▼
4. Start Angular frontend
        │
        ▼
5. Open application in browser
```
 
Conceptually:
 
```
Angular
localhost:4200
       │
       │ API Requests
       ▼
Spring Boot
localhost:8765
       │
       ▼
MySQL
visitor_management
```
 
## 📚 API Documentation
 
The backend includes OpenAPI/Swagger support for API documentation.
 
Once the backend is running, refer to the configured Swagger/OpenAPI endpoint for interactive API exploration.
 
## 🔐 Security Architecture
 
The backend uses Spring Security to control access to application resources.
 
A high-level authentication flow looks like:
 
```
User enters credentials
        │
        ▼
Angular Login Component
        │
        ▼
Authentication Request
        │
        ▼
Spring Security
        │
        ▼
Authentication Logic
        │
        ├── Invalid Credentials
        │         │
        │         ▼
        │      Error Response
        │
        └── Valid Credentials
                  │
                  ▼
             Authenticated User
                  │
                  ▼
             Role-Based Access
```
 
## 🧠 Application Design
 
The backend follows a layered architecture:
 
```
Controller Layer
       │
       │ Handles HTTP requests and responses
       ▼
Service Layer
       │
       │ Contains business logic
       ▼
Repository Layer
       │
       │ Communicates with database
       ▼
Database
```
 
This separation improves:
 
- Maintainability
- Testability
- Scalability
- Separation of concerns
- Code organization
## 📸 Application Screenshots
 
Screenshots will be added as the application is run and verified.
 
Suggested structure:
 
```
docs/
└── screenshots/
    ├── login.png
    ├── dashboard.png
    ├── visitor-management.png
    ├── visit-management.png
    ├── gate-pass.png
    └── reports.png
```
 
Once available, they can be displayed here:
 
### 🔐 Login
 
![Login Page](docs/screenshots/login.png)
 
### 📊 Dashboard
 
![Dashboard](docs/screenshots/dashboard.png)
 
### 👤 Visitor Management
 
![Visitor Management](docs/screenshots/visitor-management.png)
 
## 🗺️ Future Improvements
 
Potential improvements for future versions:
 
- [ ] Dockerize frontend, backend, and database
- [ ] Add Docker Compose for one-command setup
- [ ] Add automated backend tests
- [ ] Add frontend unit tests
- [ ] Add CI/CD using GitHub Actions
- [ ] Add environment-specific configuration
- [ ] Add .env.example
- [ ] Improve centralized error handling
- [ ] Add API integration tests
- [ ] Add database migration support using Flyway or Liquibase
- [ ] Add monitoring and health checks
- [ ] Improve audit logging
- [ ] Add email notifications
- [ ] Improve mobile responsiveness
## 🎯 What This Project Demonstrates
 
This project demonstrates practical experience with:
 
- ✓ Full-Stack Development
- ✓ Angular
- ✓ TypeScript
- ✓ Java
- ✓ Spring Boot
- ✓ REST API Development
- ✓ Spring Security
- ✓ Role-Based Access Control
- ✓ JPA / Hibernate
- ✓ MySQL
- ✓ Layered Architecture
- ✓ Authentication
- ✓ Database Integration
- ✓ Enterprise Application Design
## 👨‍💻 Author
 
**Ayuva Ojha**
 
*Computer Science Engineer | Full-Stack Developer | Software Engineering Enthusiast*
 
Connect with me ⭐
GitHub: [code-cactus27](https://github.com/code-cactus27)
 
## ⭐ Support
 
If you found this project interesting or useful, consider giving the repository a star.
 
It helps the project gain visibility and motivates further development.
 
