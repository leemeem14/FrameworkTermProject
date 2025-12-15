<ENG>(kor 은 554번째 줄로 가세요.)
Assignment System - 과제 제출 시스템
A comprehensive web-based assignment submission system built with Spring Boot 3, featuring secure user authentication, role-based access control, and file management capabilities......for Assignment(yeah i made this 4 termProject assignment).

📋 Table of Contents
Project Overview

Key Features

Technology Stack

Project Structure

System Architecture

Database Schema

Installation & Setup

Configuration

Running the Application

API Endpoints

Security Implementation

Usage Guide

Troubleshooting

🎯 Project Overview
The Assignment System is a modern web application designed to streamline the assignment submission process in educational institutions. It provides a centralized platform where instructors can create and manage assignments, while students can submit their work with deadline tracking and file upload capabilities.

This project is built as part of a framework course and demonstrates best practices in Spring Boot development, including:

Enterprise-grade security with Spring Security and JWT authentication

RESTful API design with proper controller patterns

Database persistence using Spring Data JPA with MySQL

Template rendering with Thymeleaf

File management with configurable upload directories

Role-based access control for instructor and student roles

✨ Key Features
Authentication & Authorization
User Registration & Login: Secure user registration with email and username validation

JWT Token Authentication: Token-based authentication for API endpoints

Role-Based Access Control (RBAC): Support for INSTRUCTOR and STUDENT roles

Password Security: BCrypt password encoding for enhanced security

Assignment Management
Assignment Creation: Instructors can create new assignments with title, description, and deadline

Assignment Listing: View all active assignments with filtering by course

Assignment Details: Detailed view of assignment information and requirements

Deadline Tracking: Real-time deadline information for students

Submission Management
File Upload: Students can upload assignment files with size limits (up to 100MB)

Submission Tracking: Track submission status and timestamps

Multiple Submissions: Support for resubmission before deadline

Submission History: Complete history of all submissions with timestamps

File Management
Secure File Storage: Files stored in designated upload directory

File Download: Support for downloading submitted files

File Validation: File type and size validation during upload

Organized Storage: Files organized by submission and upload date

🛠 Technology Stack
Backend
Framework: Spring Boot 3.2.0

Language: Java 17

Build Tool: Maven

Authentication: JWT (JSON Web Token) with JJWT 0.12.3

ORM: Spring Data JPA with Hibernate

Database: MySQL 8.0+

Template Engine: Thymeleaf with Spring Security extras

Development Tools: Lombok, Spring DevTools

Dependencies
text
Spring Boot Starter Web (REST APIs)
Spring Boot Starter Security (Authentication & Authorization)
Spring Boot Starter Data JPA (Database persistence)
Spring Boot Starter Thymeleaf (Template rendering)
MySQL Connector Java (Database driver)
JJWT (JWT token handling)
Lombok (Boilerplate code reduction)
Spring Boot Test Suite
Tools & Environment
IDE: IntelliJ IDEA (recommended)

Version Control: Git & GitHub

Database Management: MySQL, DataGrip

📁 Project Structure

<img width="393" height="680" alt="{1BDE9624-4D68-4C4F-B6EA-419307E203FC}" src="https://github.com/user-attachments/assets/05089b75-86af-4333-949d-f002380d8a6e" />

🏗 System Architecture
Layered Architecture
The application follows a classic three-tier layered architecture:

<img width="267" height="259" alt="{CD95DDE0-9516-4B83-AAE7-60EF008ED826}" src="https://github.com/user-attachments/assets/1cf1d3cf-b901-4e74-afba-2d0212ce95a3" />


Components
Controllers: Handle HTTP requests and responses

Map endpoints to service methods

Validate input parameters

Return appropriate HTTP status codes

Services: Contain business logic

Perform CRUD operations

Implement business rules

Handle cross-entity operations

Repositories: Manage database operations

Extend Spring Data JPA repositories

Execute custom queries

Manage entity persistence

Entities: Define data models

Map to database tables

Define relationships

Include validation constraints

Security: Handle authentication and authorization

JWT token generation and validation

User authentication

Role-based access control

💾 Database Schema
User Table
sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(100) UNIQUE NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(100) NOT NULL,
  role ENUM('INSTRUCTOR', 'STUDENT') NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP,
  is_active BOOLEAN DEFAULT TRUE
);
Assignment Table
sql
CREATE TABLE assignment (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  deadline TIMESTAMP NOT NULL,
  created_by BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP,
  FOREIGN KEY (created_by) REFERENCES users(id)
);
Submission Table
sql
CREATE TABLE submission (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  assignment_id BIGINT NOT NULL,
  student_id BIGINT NOT NULL,
  submitted_at TIMESTAMP NOT NULL,
  file_path VARCHAR(255),
  status ENUM('PENDING', 'SUBMITTED', 'GRADED'),
  FOREIGN KEY (assignment_id) REFERENCES assignment(id),
  FOREIGN KEY (student_id) REFERENCES users(id)
);
🚀 Installation & Setup
Prerequisites
Java 17 or higher

Maven 3.6+

MySQL 8.0+

Git

Step 1: Clone the Repository
bash
git clone https://github.com/leemeem14/FrameworkTermProject.git
cd FrameworkTermProject/assignment-system
Step 2: Create MySQL Database
bash
mysql -u root -p

CREATE DATABASE assignment_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE assignment_db;
Step 3: Install Dependencies
bash
mvn clean install
Step 4: Create Upload Directory
bash
mkdir -p uploads
⚙️ Configuration
Database Configuration (application.properties)
text
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/assignment_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
File Upload Configuration
text
# File Upload Configuration
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
upload.dir=uploads/
Thymeleaf Configuration
text
# Thymeleaf Configuration
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.mode=HTML
▶️ Running the Application
Using Maven
bash
mvn spring-boot:run
Using IDE
Open in IntelliJ IDEA

Right-click on AssignmentSystemApplication.java

Select "Run"

Access the Application
text
Server: http://localhost:8080
Context Path: /
🔌 API Endpoints
Authentication Endpoints
POST /api/auth/register - Register new user

POST /api/auth/login - User login and get JWT token

POST /api/auth/logout - User logout

GET /api/auth/me - Get current user information

Assignment Endpoints
GET /api/assignments - List all assignments

GET /api/assignments/{id} - Get assignment details

POST /api/assignments - Create new assignment (INSTRUCTOR only)

PUT /api/assignments/{id} - Update assignment (INSTRUCTOR only)

DELETE /api/assignments/{id} - Delete assignment (INSTRUCTOR only)

Submission Endpoints
POST /api/submissions - Submit assignment

GET /api/submissions - List user submissions

GET /api/submissions/{id} - Get submission details

GET /api/submissions/{id}/download - Download submission file

PUT /api/submissions/{id} - Update submission (before deadline)

File Endpoints
POST /api/files/upload - Upload file

GET /api/files/{fileId} - Download file

DELETE /api/files/{fileId} - Delete file (INSTRUCTOR only)

🔒 Security Implementation
Spring Security Configuration
The application uses Spring Security 6 with the following features:

JWT Authentication

Token generation on login

Token validation on each request

Configurable token expiration (default: 24 hours)

Password Encoding

BCrypt password encoding with strength 10

Secure password comparison

Authorization

Role-based access control (INSTRUCTOR, STUDENT)

Method-level security with @PreAuthorize

URL pattern-based security

CORS & CSRF

CSRF protection enabled

CORS configuration for API endpoints

Secure cookie settings

Security Headers
X-Frame-Options: DENY

X-Content-Type-Options: nosniff

X-XSS-Protection: enabled

📖 Usage Guide
For Instructors
Create Assignment

Navigate to "Create Assignment"

Fill in title, description, and deadline

Click "Create" to publish

View Submissions

Go to "Submissions" section

View all student submissions

Download files for grading

Grade Assignments

Review submitted files

Provide feedback

Mark as graded

For Students
View Assignments

Navigate to "Assignments"

View all available assignments

Check deadlines and requirements

Submit Assignment

Click on assignment

Upload file (PDF, DOC, ZIP, etc.)

Confirm submission

Track Status

View submission history

Check submission dates

Download your submissions

🐛 Troubleshooting
Database Connection Issues
Error: java.sql.SQLException: Access denied for user 'root'@'localhost'

Solution:

Verify MySQL is running

Check username and password in application.properties

Ensure database exists: CREATE DATABASE assignment_db;

File Upload Issues
Error: FileNotFoundException or permission denied

Solution:

Create upload directory: mkdir -p uploads

Check directory permissions: chmod 755 uploads

Verify upload path in application.properties

Port Already in Use
Error: Address already in use: bind

Solution:

bash
# Change port in application.properties
server.port=8081
JWT Token Issues
Error: JWT signature verification failed

Solution:

Regenerate JWT token

Ensure token hasn't expired

Check JWT secret key configuration

📝 Development Notes
Adding New Features
Create Entity

Add new entity in entity/ package

Define relationships and constraints

Create Repository

Extend JpaRepository<Entity, ID>

Add custom query methods if needed

Create Service

Implement business logic

Use @Service and @Transactional annotations

Create Controller

Map endpoints with @RequestMapping

Use appropriate HTTP methods

Add security annotations

Testing
bash
mvn test
🤝 Contributing
To contribute to this project:

Fork the repository

Create a feature branch

Commit your changes

Push to the branch

Create a Pull Request


✉️ Support
For issues or questions:

Create an issue on GitHub

Contact the project maintainer

Check existing documentation

Last Updated: December 2025

<KOR>
과제 제출 시스템 (Assignment System)
Spring Boot 3을 기반으로 구축한 포괄적인 웹 기반 과제 제출 시스템입니다. 안전한 사용자 인증, 역할 기반 접근 제어, 파일 관리 기능을 갖추고 있습니다.(텀프로젝트 과제 제출용)

📋 목차
프로젝트 개요

주요 기능

기술 스택

프로젝트 구조

시스템 아키텍처

데이터베이스 스키마

설치 및 설정

환경 설정

애플리케이션 실행

API 엔드포인트

보안 구현

사용 가이드

문제 해결

🎯 프로젝트 개요
과제 제출 시스템은 교육 기관의 과제 제출 과정을 간소화하기 위해 설계된 현대적인 웹 애플리케이션입니다. 교사가 과제를 생성하고 관리할 수 있는 중앙화된 플랫폼을 제공하며, 학생은 마감일 추적 및 파일 업로드 기능과 함께 자신의 과제를 제출할 수 있습니다.

이 프로젝트는 프레임워크 강좌의 일부로 개발되었으며, Spring Boot 개발의 모범 사례를 보여줍니다:

엔터프라이즈급 보안: Spring Security와 JWT 인증

RESTful API 설계: 적절한 컨트롤러 패턴

데이터베이스 영속성: Spring Data JPA를 사용한 MySQL 연동

템플릿 렌더링: Thymeleaf를 이용한 동적 화면 구성

파일 관리: 설정 가능한 업로드 디렉토리

역할 기반 접근 제어: 교사와 학생 역할 지원

✨ 주요 기능
인증 및 권한 관리
사용자 등록 및 로그인: 이메일 및 사용자명 검증을 포함한 안전한 사용자 등록

JWT 토큰 인증: API 엔드포인트를 위한 토큰 기반 인증

역할 기반 접근 제어(RBAC): 교사(INSTRUCTOR)와 학생(STUDENT) 역할 지원

비밀번호 보안: BCrypt 암호화를 이용한 강화된 보안

과제 관리
과제 생성: 교사가 제목, 설명, 마감일을 포함한 새로운 과제 생성 가능

과제 목록: 모든 활성 과제를 필터링하여 조회

과제 상세 정보: 과제 정보 및 요구사항의 상세 조회

마감일 추적: 학생을 위한 실시간 마감일 정보 제공

제출 관리
파일 업로드: 학생이 과제 파일 업로드 (최대 100MB)

제출 추적: 제출 상태 및 타임스탬프 추적

재제출 지원: 마감일 이전의 재제출 지원

제출 이력: 모든 제출의 타임스탬프를 포함한 완전한 이력

파일 관리
안전한 파일 저장: 지정된 업로드 디렉토리에 파일 저장

파일 다운로드: 제출된 파일 다운로드 지원

파일 검증: 업로드 중 파일 유형 및 크기 검증

조직화된 저장소: 제출 및 업로드 날짜별로 정렬된 파일 저장

🛠 기술 스택
백엔드
프레임워크: Spring Boot 3.2.0

언어: Java 17

빌드 도구: Maven

인증: JWT (JSON Web Token) - JJWT 0.12.3

ORM: Spring Data JPA with Hibernate

데이터베이스: MySQL 8.0+

템플릿 엔진: Thymeleaf with Spring Security extras

개발 도구: Lombok, Spring DevTools

의존성
text
Spring Boot Starter Web (REST API)
Spring Boot Starter Security (인증 및 권한 관리)
Spring Boot Starter Data JPA (데이터베이스 영속성)
Spring Boot Starter Thymeleaf (템플릿 렌더링)
MySQL Connector Java (데이터베이스 드라이버)
JJWT (JWT 토큰 처리)
Lombok (보일러플레이트 코드 감소)
Spring Boot Test Suite (테스트)
도구 및 환경
IDE: IntelliJ IDEA (권장)

버전 관리: Git & GitHub

데이터베이스 관리: MySQL, DataGrip

📁 프로젝트 구조

<img width="359" height="680" alt="{27B85F96-AE68-4798-A41B-903B86D58356}" src="https://github.com/user-attachments/assets/78eb160a-5d73-4d0e-9001-acd3f473039f" />

🏗 시스템 아키텍처
레이어드 아키텍처
이 애플리케이션은 고전적인 3계층 레이어드 아키텍처를 따릅니다:

<img width="276" height="258" alt="{EE8F0122-D45E-4E10-AE03-D3472B5CA9AC}" src="https://github.com/user-attachments/assets/6b87ea46-96a1-4450-93fa-c1e4305ac9fe" />


컴포넌트
컨트롤러(Controllers): HTTP 요청 및 응답 처리

엔드포인트를 서비스 메서드에 매핑

입력 파라미터 검증

적절한 HTTP 상태 코드 반환

서비스(Services): 비즈니스 로직 포함

CRUD 작업 수행

비즈니스 규칙 구현

엔티티 간 작업 처리

저장소(Repositories): 데이터베이스 작업 관리

Spring Data JPA 저장소 확장

사용자 정의 쿼리 실행

엔티티 영속성 관리

엔티티(Entities): 데이터 모델 정의

데이터베이스 테이블에 매핑

관계 정의

검증 제약 조건 포함

보안(Security): 인증 및 권한 관리

JWT 토큰 생성 및 검증

사용자 인증

역할 기반 접근 제어

💾 데이터베이스 스키마
사용자 테이블 (User Table)
sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(100) UNIQUE NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(100) NOT NULL,
  role ENUM('INSTRUCTOR', 'STUDENT') NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP,
  is_active BOOLEAN DEFAULT TRUE
);
과제 테이블 (Assignment Table)
sql
CREATE TABLE assignment (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  deadline TIMESTAMP NOT NULL,
  created_by BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP,
  FOREIGN KEY (created_by) REFERENCES users(id)
);
제출 테이블 (Submission Table)
sql
CREATE TABLE submission (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  assignment_id BIGINT NOT NULL,
  student_id BIGINT NOT NULL,
  submitted_at TIMESTAMP NOT NULL,
  file_path VARCHAR(255),
  status ENUM('PENDING', 'SUBMITTED', 'GRADED'),
  FOREIGN KEY (assignment_id) REFERENCES assignment(id),
  FOREIGN KEY (student_id) REFERENCES users(id)
);
🚀 설치 및 설정
필수 사항
Java 17 이상

Maven 3.6+

MySQL 8.0+

Git

1단계: 저장소 클론
bash
git clone https://github.com/leemeem14/FrameworkTermProject.git
cd FrameworkTermProject/assignment-system
2단계: MySQL 데이터베이스 생성
bash
mysql -u root -p

CREATE DATABASE assignment_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE assignment_db;
3단계: 의존성 설치
bash
mvn clean install
4단계: 업로드 디렉토리 생성
bash
mkdir -p uploads
⚙️ 환경 설정
데이터베이스 설정 (application.properties)
text
# MySQL 설정
spring.datasource.url=jdbc:mysql://localhost:3306/assignment_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA 설정
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
파일 업로드 설정
text
# 파일 업로드 설정
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
upload.dir=uploads/
Thymeleaf 설정
text
# Thymeleaf 설정
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.mode=HTML
▶️ 애플리케이션 실행
Maven을 사용한 실행
bash
mvn spring-boot:run
IDE를 사용한 실행
IntelliJ IDEA에서 프로젝트 열기

AssignmentSystemApplication.java에서 마우스 우클릭

"Run" 선택

애플리케이션 접속
text
서버: http://localhost:8080
컨텍스트 경로: /
🔌 API 엔드포인트
인증 엔드포인트
POST /api/auth/register - 새 사용자 등록

POST /api/auth/login - 사용자 로그인 및 JWT 토큰 획득

POST /api/auth/logout - 사용자 로그아웃

GET /api/auth/me - 현재 사용자 정보 조회

과제 엔드포인트
GET /api/assignments - 모든 과제 목록 조회

GET /api/assignments/{id} - 과제 상세 정보 조회

POST /api/assignments - 새 과제 생성 (교사만)

PUT /api/assignments/{id} - 과제 수정 (교사만)

DELETE /api/assignments/{id} - 과제 삭제 (교사만)

제출 엔드포인트
POST /api/submissions - 과제 제출

GET /api/submissions - 사용자의 제출 목록 조회

GET /api/submissions/{id} - 제출 상세 정보 조회

GET /api/submissions/{id}/download - 제출 파일 다운로드

PUT /api/submissions/{id} - 제출 수정 (마감일 이전)

파일 엔드포인트
POST /api/files/upload - 파일 업로드

GET /api/files/{fileId} - 파일 다운로드

DELETE /api/files/{fileId} - 파일 삭제 (교사만)

🔒 보안 구현
Spring Security 설정
이 애플리케이션은 다음과 같은 기능을 포함한 Spring Security 6을 사용합니다:

JWT 인증

로그인 시 토큰 생성

각 요청에서 토큰 검증

설정 가능한 토큰 만료 시간 (기본값: 24시간)

비밀번호 암호화

강도 10의 BCrypt 암호화

안전한 비밀번호 비교

권한 관리

역할 기반 접근 제어 (교사, 학생)

@PreAuthorize를 이용한 메서드 레벨 보안

URL 패턴 기반 보안

CORS & CSRF

CSRF 보호 활성화

API 엔드포인트에 대한 CORS 설정

안전한 쿠키 설정

보안 헤더
X-Frame-Options: DENY

X-Content-Type-Options: nosniff

X-XSS-Protection: 활성화

📖 사용 가이드
교사용 가이드
과제 생성

"과제 생성" 메뉴로 이동

제목, 설명, 마감일 입력

"생성" 클릭하여 공개

제출 현황 조회

"제출" 섹션으로 이동

모든 학생의 제출 현황 확인

평가를 위해 파일 다운로드

과제 평가

제출된 파일 검토

피드백 제공

평가 완료로 표시

학생용 가이드
과제 조회

"과제" 메뉴로 이동

모든 활성 과제 확인

마감일 및 요구사항 확인

과제 제출

과제 클릭

파일 업로드 (PDF, DOC, ZIP 등)

제출 확인

상태 추적

제출 이력 확인

제출 날짜 확인

자신의 제출 파일 다운로드

🐛 문제 해결
데이터베이스 연결 문제
오류: java.sql.SQLException: Access denied for user 'root'@'localhost'

해결 방법:

MySQL이 실행 중인지 확인

application.properties에서 사용자명과 비밀번호 확인

데이터베이스가 존재하는지 확인: CREATE DATABASE assignment_db;

파일 업로드 문제
오류: FileNotFoundException 또는 권한 거부

해결 방법:

업로드 디렉토리 생성: mkdir -p uploads

디렉토리 권한 확인: chmod 755 uploads

application.properties에서 업로드 경로 확인

포트가 이미 사용 중인 경우
오류: Address already in use: bind

해결 방법:

bash
# application.properties에서 포트 변경
server.port=8081
JWT 토큰 문제
오류: JWT signature verification failed

해결 방법:

JWT 토큰 재생성

토큰 만료 확인

JWT 시크릿 키 설정 확인

📝 개발 노트
새로운 기능 추가
엔티티 생성

entity/ 패키지에 새 엔티티 추가

관계 및 제약 조건 정의

저장소 생성

JpaRepository<Entity, ID> 확장

필요하면 사용자 정의 쿼리 메서드 추가

서비스 생성

비즈니스 로직 구현

@Service와 @Transactional 애노테이션 사용

컨트롤러 생성

@RequestMapping으로 엔드포인트 매핑

적절한 HTTP 메서드 사용

보안 애노테이션 추가

테스트
bash
mvn test
🤝 기여 방법
이 프로젝트에 기여하려면:

저장소를 Fork

기능 브랜치 생성

변경 사항 커밋

브랜치에 Push

Pull Request 생성


✉️ 지원
문제가 있거나 질문이 있는 경우:

GitHub에서 이슈 생성

프로젝트 관리자에게 연락

기존 문서 확인

최종 업데이트: 2025년 12월
