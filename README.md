Membership Management Service
A RESTful API built with Spring Boot 3.2 for managing memberships with JWT authentication, role-based access control, and bilingual support (English & Arabic).

Tech Stack

Java 21 + Spring Boot 3.5.8
Spring Security + JWT (jjwt 0.12.5)
Spring Data JPA + H2 In-Memory Database
SpringDoc OpenAPI 2.8.6 (Swagger UI)
Lombok + Maven


Getting Started
Prerequisites

Java 21
Maven

Run the application
bashgit clone https://github.com/maiqamaj/Simple-Membership-Management-Service.git
cd Simple-Membership-Management-Service
mvn spring-boot:run
The app will start on http://localhost:8080

Default Admin Credentials
Email:    admin@appswave.com
Password: Admin@2026

API Documentation
ToolURLSwagger UIhttp://localhost:8080/swagger-ui/index.html

Authentication
All protected endpoints require a JWT token in the request header:
Authorization: Bearer <token>
For localization, add:
Accept-Language: en   # English (default)
Accept-Language: ar   # Arabic


Example Requests
 -------  -------  -------  -------  -------  -------  -------  -------  ------- 

Register

curl -X POST http://localhost:8080/api/v1/authentication/register \
  -H "Content-Type: application/json" \
  -H "Accept-Language: en" \
  -d '{
    "firstName": "Mai",
    "lastName": "Qamaj",
    "email": "maiQamaj@gmail.com",
    "password": "Password@2026"
  }'
  
-------  -------  -------  -------  -------  -------  -------  -------  ------- 

Login

curl -X POST http://localhost:8080/api/v1/authentication/login \
  -H "Content-Type: application/json" \
  -H "Accept-Language: en" \
  -d '{
    "email": "admin@appswave.com",
    "password": "Admin@2026"
  }'

 -------  -------  -------  -------  -------  -------  -------  -------  ------- 
Create Member (Admin)

curl -X POST http://localhost:8080/api/v1/members \
  -H "Content-Type: application/json" \
  -H "Accept-Language: en" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "firstName": "mai",
    "lastName": "qamaj",
    "email": "mai@gmail.com",
    "mobileNumber": "+962791234567",
    "gender": "Female",
    "membershipType": "INTERNAL_MEMBER",
    "persona": "Individual"
  }'
  
-------  -------  -------  -------  -------  -------  -------  -------  ------- 

Get All Members with Filters

curl -X GET "http://localhost:8080/api/v1/members?page=0&size=10&search=mai&gender=FEMAIL" \
  -H "Accept-Language: en" \
  -H "Authorization: Bearer YOUR_TOKEN"
  
-------  -------  -------  -------  -------  -------  -------  -------  ------- 


{
  "responseCode": 200,
  "success": true,
  "currentDate": "2026-02-23T00:00:00",
  "message": "Operation successful"
}


Error Response Format
json{
  "success": false,
  "message": "Localized error message"
}

-------  -------  -------  -------  -------  -------  -------  -------  ------- 

