# 과제 제출 시스템 (Assignment Submission System)

Spring Boot 3 기반의 과제 관리 및 제출 시스템입니다.

## 🎯 주요 기능

### 인증 및 권한 관리
- ✅ 회원가입 (학생/선생님)
- ✅ 로그인/로그아웃
- ✅ Spring Security 기반 역할 기반 접근 제어 (RBAC)

### 과제 관리
- ✅ 과제 생성 (선생님만)
- ✅ 과제 수정 (선생님만)
- ✅ 과제 삭제 (선생님만)
- ✅ 과제 목록 조회 (모든 사용자)
- ✅ 과제 상세보기

### 파일 관리
- ✅ 파일 업로드 (학생)
- ✅ 파일 다운로드 (모든 사용자)
- ✅ 파일 삭제 (업로드자)

### 기한 관리
- ✅ 과제 기한 설정
- ✅ 기한 초과 표시
- ✅ 기한별 정렬

## 🛠 기술 스택

- **Framework**: Spring Boot 3.2.0
- **Security**: Spring Security 6
- **Database**: MySQL 8
- **ORM**: Spring Data JPA
- **Template**: Thymeleaf
- **Build**: Maven
- **Language**: Java 17

## 📋 필수 사항

- Java 17 이상
- MySQL 8 이상
- Maven 3.6 이상

## 🚀 설치 및 실행

### 1. 데이터베이스 설정

MySQL에서 다음을 실행하세요:

\`\`\`sql
CREATE DATABASE assignment_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE assignment_system;

-- src/main/resources/db/schema.sql 파일 실행
\`\`\`

### 2. 애플리케이션 설정

\`src/main/resources/application.properties\` 파일을 수정하세요:

\`\`\`properties
# 데이터베이스 연결 정보
spring.datasource.url=jdbc:mysql://localhost:3306/assignment_system?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password

# 파일 업로드 경로
upload.dir=uploads/
\`\`\`

### 3. 실행

**IntelliJ IDEA에서:**
1. 프로젝트 열기
2. `AssignmentSystemApplication.java` 파일 우클릭
3. "Run" 클릭

**Maven 커맨드:**
\`\`\`bash
mvn spring-boot:run
\`\`\`

**JAR 파일로 실행:**
\`\`\`bash
mvn clean package
java -jar target/assignment-system-1.0.0.jar
\`\`\`

### 4. 접속

브라우저에서 `http://localhost:8080`으로 접속하세요.

## 👥 테스트 계정

### 선생님 계정
- **사용자명**: admin
- **비밀번호**: password (기본값: SHA256 해시)

### 학생 계정
- **사용자명**: student1
- **비밀번호**: password (기본값: SHA256 해시)

> ⚠️ **주의**: 프로덕션 환경에서는 반드시 비밀번호를 변경하세요!

## 📁 프로젝트 구조

\`\`\`
assignment-system/
├── src/
│   ├── main/
│   │   ├── java/com/assignment/
│   │   │   ├── config/          # Spring Configuration
│   │   │   ├── controller/      # REST/MVC Controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # JPA Entities
│   │   │   ├── repository/      # JPA Repositories
│   │   │   ├── service/         # Business Logic
│   │   │   └── security/        # Security Configuration
│   │   └── resources/
│   │       ├── templates/       # Thymeleaf Templates
│   │       ├── static/          # CSS, JS, Images
│   │       ├── db/              # Database Scripts
│   │       └── application.properties
│   └── test/                    # Test Classes
├── pom.xml                      # Maven Configuration
└── README.md                    # This File
\`\`\`

## 🔐 보안 기능

- BCrypt 비밀번호 암호화
- Spring Security CSRF 보호
- 역할 기반 접근 제어 (RBAC)
- JWT 토큰 지원 (확장 가능)
- 사용자 활성화/비활성화 관리

## 📝 API 엔드포인트

### 인증
- `POST /signup` - 회원가입
- `POST /login` - 로그인
- `POST /logout` - 로그아웃

### 과제
- `GET /assignment/list` - 과제 목록
- `GET /assignment/{id}` - 과제 상세보기
- `GET /assignment/create` - 과제 생성 페이지
- `POST /assignment/create` - 과제 생성
- `GET /assignment/{id}/edit` - 과제 수정 페이지
- `POST /assignment/{id}/edit` - 과제 수정
- `POST /assignment/{id}/delete` - 과제 삭제

### 파일
- `POST /file/upload` - 파일 업로드
- `GET /file/download/{filename}` - 파일 다운로드
- `POST /file/delete/{filename}` - 파일 삭제

## 🐛 트러블슈팅

### MySQL 연결 실패
- MySQL 서버 실행 여부 확인
- 데이터베이스 URL, 사용자명, 비밀번호 확인
- `allowPublicKeyRetrieval=true` 추가 (로컬 개발 환경)

### 파일 업로드 실패
- `uploads/` 디렉토리 생성 확인
- 디렉토리 쓰기 권한 확인
- `spring.servlet.multipart.max-file-size` 설정 확인

### 포트 충돌
\`\`\`properties
server.port=8081
\`\`\`

## 📚 추가 학습 자료

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Thymeleaf](https://www.thymeleaf.org/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

## 📄 라이선스

MIT License

## 👨‍💻 개발자

Assignment System Team

## 📞 연락처

이슈는 GitHub Issues를 통해 보고해주세요.

---

**마지막 업데이트**: 2024년 12월 14일
