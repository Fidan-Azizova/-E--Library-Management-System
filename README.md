# E-Library Management System

Final Project – Java Spring Boot Backend Development.
Kitab və kateqoriyaların idarə edilməsi üçün REST API. JWT əsaslı autentifikasiya, rol-əsaslı icazələndirmə (RBAC), MapStruct mapping, validasiya, Swagger sənədləşdirmə və mərkəzləşdirilmiş xəta idarəetməsi ilə.

## Texnologiyalar

- Java 17
- Spring Boot 3.3.4 (Web, Data JPA, Security, Validation)
- MySQL 8
- Spring Security + JWT (jjwt 0.11.5)
- MapStruct 1.5.5
- Lombok
- springdoc-openapi (Swagger UI) 2.6.0
- Maven

## Layihə Strukturu

```
src/main/java/com/elibrary/
 ├── config/          -> SecurityConfig, SwaggerConfig
 ├── security/         -> JwtUtil, JwtAuthFilter, CustomUserDetailsService
 ├── entity/            -> User, Role, Category, Book
 ├── dto/               -> auth/, category/, book/
 ├── mapper/            -> CategoryMapper, BookMapper (MapStruct)
 ├── repository/        -> UserRepository, CategoryRepository, BookRepository
 ├── service/           -> AuthService, CategoryService, BookService
 ├── controller/        -> AuthController, CategoryController, BookController
 └── exception/         -> GlobalExceptionHandler, custom exceptions, ErrorResponse
```

## Qurulum (Setup)

### 1. MySQL

MySQL server-in 3306 portunda işlədiyinə əmin olun. Verilənlər bazası avtomatik yaradılacaq
(`createDatabaseIfNotExist=true`), ayrıca CREATE DATABASE etməyə ehtiyac yoxdur.

`src/main/resources/application.properties` faylında qoşulma məlumatları:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/elibrary_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}
```

> ⚠️ **Şifrə faylda saxlanmır.** Şifrə `DB_PASSWORD` environment variable-dan oxunur, ona görə
> layihəni işə salmazdan əvvəl onu özünüz təyin etməlisiniz (aşağıya bax). Bu sayədə GitHub-a
> push edəndə real şifrə heç yerdə görünmür.

**Şifrəni necə təyin etmək olar:**

*Terminal (Linux/macOS):*
```bash
export DB_PASSWORD=your_mysql_password
mvn spring-boot:run
```

*Terminal (Windows PowerShell):*
```powershell
$env:DB_PASSWORD="your_mysql_password"
mvn spring-boot:run
```

*IntelliJ IDEA:*
`Run` → `Edit Configurations` → `Environment variables` sahəsinə `DB_PASSWORD=your_mysql_password` yazın.

*VS Code (launch.json):*
```json
"env": { "DB_PASSWORD": "your_mysql_password" }
```

Environment variable təyin edilməzsə, tətbiq `DB_PASSWORD` boş olduğu üçün MySQL-ə qoşula
bilməyəcək və xəta verəcək — bu, şifrənin təsadüfən boş/yanlış qalmasının qarşısını alır.

### 2. Layihəni işə salmaq

```bash
mvn clean install
mvn spring-boot:run
```

Tətbiq `http://localhost:8080` ünvanında işə düşəcək.

### 3. Swagger UI

```
http://localhost:8080/swagger-ui.html
```

## Swagger-də JWT Token Necə Əlavə Edilir?

1. Swagger UI-ni açın: `http://localhost:8080/swagger-ui.html`
2. **POST /api/auth/register** ilə istifadəçi yaradın (aşağıya bax – rol necə əlavə olunur).
3. **POST /api/auth/login** çağırın, cavabda gələn `token` sahəsini kopyalayın.
4. Səhifənin yuxarı sağ küncündəki yaşıl **"Authorize"** düyməsinə klikləyin.
5. Açılan pəncərədə (`bearerAuth` sxemi) **YALNIZ tokenin özünü** yapışdırın – `Bearer` sözünü
   yazmağa ehtiyac yoxdur, Swagger onu avtomatik əlavə edir.
6. **Authorize**, sonra **Close** düyməsinə basın. Bundan sonra bütün qorunan sorğulara
   avtomatik olaraq `Authorization: Bearer <token>` header-i əlavə olunacaq.

Postman/insomnia kimi alətlərdə isə manual olaraq header əlavə edilməlidir:
```
Authorization: Bearer <token>
```

## Rol-Əsaslı İcazələndirmə (Role-Based Access Control)

Sistemdə iki rol var: `ROLE_ADMIN` və `ROLE_USER`.

**Yeni rol necə əlavə etmək olar:**
1. `com.elibrary.entity.Role` enum-una yeni dəyər əlavə edin, məs: `ROLE_LIBRARIAN`.
2. `SecurityConfig.securityFilterChain()` metodunda `hasRole("LIBRARIAN")` şəklində müvafiq
   endpoint-lərə icazə qaydası yazın (Spring Security `hasRole` avtomatik `ROLE_` prefiksini
   axtarır, ona görə metodda prefikssiz yazılır).
3. Controller-lərdə metod səviyyəsində icazə lazımdırsa `@PreAuthorize("hasRole('LIBRARIAN')")`
   annotasiyasından istifadə edə bilərsiniz (`@EnableMethodSecurity` artıq aktivdir).

**İstifadəçiyə rol necə mənimsədilir:**
- `POST /api/auth/register` sorğusunda `"roles": ["ADMIN"]` göndərməklə admin istifadəçi
  yaratmaq olar. Heç bir rol göndərilməzsə, avtomatik olaraq `ROLE_USER` verilir.

**Cari icazə qaydaları:**
| Endpoint | Metod | İcazə |
|---|---|---|
| /api/auth/register, /api/auth/login | POST | Hamıya açıq |
| /api/categories, /api/books | GET | Hamıya açıq |
| /api/categories, /api/books | POST/PUT/DELETE | Yalnız ROLE_ADMIN |

## Endpoint-lər

### Authentication
| Metod | URL | Açıqlama |
|---|---|---|
| POST | /api/auth/register | Yeni istifadəçi qeydiyyatı |
| POST | /api/auth/login | Login, JWT token qaytarır |

### Category
| Metod | URL | Açıqlama |
|---|---|---|
| POST | /api/categories | Yeni kateqoriya (ADMIN) |
| GET | /api/categories | Bütün kateqoriyalar |
| GET | /api/categories/{id} | ID-yə görə kateqoriya |
| PUT | /api/categories/{id} | Kateqoriyanı yenilə (ADMIN) |
| DELETE | /api/categories/{id} | Kateqoriyanı sil (ADMIN) |

### Book
| Metod | URL | Açıqlama |
|---|---|---|
| POST | /api/books | Yeni kitab (ADMIN) |
| GET | /api/books?page=0&size=10&sort=title,asc | Pagination və sorting ilə bütün kitablar |
| GET | /api/books/{id} | ID-yə görə kitab |
| PUT | /api/books/{id} | Kitabı yenilə (ADMIN) |
| DELETE | /api/books/{id} | Kitabı sil (ADMIN) |

## Nümunə Sorğular

**Register (Admin):**
```json
POST /api/auth/register
{
  "username": "admin1",
  "email": "admin1@elibrary.com",
  "password": "Admin123!",
  "roles": ["ADMIN"]
}
```

**Login:**
```json
POST /api/auth/login
{
  "username": "admin1",
  "password": "Admin123!"
}
```

**Create Category:**
```json
POST /api/categories
{
  "name": "Elmi"
}
```

**Create Book:**
```json
POST /api/books
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "9780132350884",
  "price": 25.99,
  "categoryId": 1
}
```

## Xəta İdarəetməsi

Bütün xətalar `GlobalExceptionHandler` (`@RestControllerAdvice`) tərəfindən tutulur və vahid
formatda qaytarılır:

```json
{
  "timestamp": "2026-07-15 12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Book not found with id: 5",
  "path": "/api/books/5"
}
```

Validasiya xətalarında əlavə olaraq `validationErrors` obyekti hər sahə üzrə mesajları göstərir.

## GitHub-a yerləşdirmək

```bash
cd e-library-management
git init
git add .
git commit -m "Initial commit: E-Library Management System"
git branch -M main
git remote add origin <your-repo-url>
git push -u origin main
```

`.gitignore` faylı `target/`, IDE fayllarını və s. artıq istisna edir. Şifrənizi commit
etməmək üçün yuxarıdakı environment variable qeydinə baxın.
