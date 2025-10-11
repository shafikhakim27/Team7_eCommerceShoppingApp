# Team7 eCommerce Shopping App

## Overview
This is a modern Java-based e-commerce shopping application built with **Java 21** and **Spring Boot 3.5.6**, featuring a streamlined architecture optimized for development and rapid prototyping.

## 🎯 **Current Project Status**

### ✅ **Recently Completed**
- **Java 21 LTS Upgrade**: Latest long-term support version
- **Spring Boot 3.5.6**: Latest stable framework version
- **Modern Authentication System**: Hybrid AJAX/form-based login with session management
- **Spring Session JDBC**: Database-persistent session management with MySQL
- **Enhanced User Controller**: Complete authentication endpoints with form and API support
- **Secure User Model**: Serializable User entity with proper validation
- **Professional UI**: Clean, responsive login/register/dashboard interface
- **Comprehensive Session Management**: User authentication with secure session handling

### 🏗️ **Current Architecture**

```
📦 Modern Authentication Architecture
┌─────────────────┐    ┌────────────────┐    ┌────────────────────┐
│   UserService   │    │ UserController │    │  Spring Session    │
│                 │◄───┤                │    │  JDBC              │
│ - User auth     │    │ - Login forms  │    │                    │
│ - Find by ID    │    │ - API endpoints│    │ - DB persistence   │
│ - Registration  │    │ - Dashboard    │    │ - Session cleanup  │
│ - Validation    │    │ - Logout       │    │ - User ID storage  │
└─────────────────┘    └────────────────┘    └────────────────────┘
```

## 📁 **Current Project Structure**

### **Main Application Structure**
```
src/main/java/com/ecommerce/
├── ECommerceApplication.java          # 🚀 Spring Boot main class
├── controller/
│   ├── UserController.java             # 🔐 Simplified REST authentication (/api/users/*)
│   ├── CartController.java             # 🛒 Shopping cart management
│   └── ProductController.java          # 📦 Product catalog operations
├── model/
│   ├── User.java                       # 👤 User entity (simplified validation - 3+ char pwd)
│   ├── Product.java                    # 📦 Product entity
│   ├── Order.java                      # 📋 Order entity
│   ├── OrderItem.java                  # 📝 Order line items
│   ├── Customer.java                   # 👥 Customer entity
│   ├── Cart.java                       # 🛒 Shopping cart entity
│   └── CartItem.java                   # 🛍️ Cart item entity
├── repository/
│   ├── UserRepository.java             # 💾 User data access (6 methods)
│   ├── ProductRepository.java          # 📦 Product data access
│   ├── CartRepository.java             # 🛒 Cart data access
│   ├── CartItemRepository.java         # 🛍️ Cart item data access
│   └── CustomerRepository.java         # 👥 Customer data access
└── service/
    ├── UserService.java                # 👤 Simplified user logic (3+ char pwd, plain text)
    ├── UserServiceInterface.java       # 📋 User service contract (7 methods)
    ├── SessionManager.java             # 🔄 Enhanced session mgmt (20+ methods)
    ├── ProductImplementation.java      # 📦 Product business logic
    ├── ProductInterface.java           # 📋 Product service contract
    ├── CartImplementation.java         # 🛒 Cart business logic
    ├── CartInterface.java              # 📋 Cart service contract
    ├── CartItemImplementation.java     # 🛍️ Cart item business logic
    └── CartItemInterface.java          # 📋 Cart item service contract
```
## 🚀 **Quick Start**

### **Prerequisites**
- **Java 21** installed on your system
- **Maven 3.8+** (or use the Maven wrapper)

### **Run the Application**
```bash
# Clone the repository
git clone https://github.com/shafikhakim27/Team7_eCommerceShoppingApp.git
cd Team7_eCommerceShoppingApp

# Build and run
mvn clean spring-boot:run

# Access the application
# REST API endpoints: http://localhost:8080/api/users/*


### **API Endpoints (Current)**
```bash
# User Management (REST API)
POST /api/users/register        # Register new user
POST /api/users/login           # Login user
GET  /api/users/{id}            # Get user by ID
GET  /api/users/all            # Get all users (dev only)
PUT  /api/users/{id}/password   # Change password

# Example Registration
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"123"}'

# Example Login
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123"}'
```

### **Database Configuration**
```properties
# MySQL Database (Production)
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=ecommerce_user
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Spring Session JDBC
spring.session.store-type=jdbc
spring.session.jdbc.initialize-schema=always

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

## � **Authentication & Login System**

### **Overview**
Our authentication system provides a robust, user-friendly login experience with both modern AJAX functionality and reliable fallback mechanisms. The system handles user registration, login, logout, and session management seamlessly.

### **Key Features**
- ✅ **Hybrid Login Approach**: AJAX-first with automatic fallback to traditional form submission
- ✅ **Session Management**: Secure session handling with Spring Session JDBC
- ✅ **Input Validation**: Real-time form validation with user-friendly error messages
- ✅ **Responsive Design**: Clean, professional interface that works on all devices
- ✅ **Error Handling**: Comprehensive error handling with graceful degradation
- ✅ **Security**: POST-based logout, session invalidation, and CSRF protection

### **Authentication Flow**

```
┌─────────────────┐    ┌──────────────────┐    ┌──────────────────┐
│   Login Page    │    │  AJAX Attempt    │    │   Success Path   │
│                 │    │                  │    │                  │
│ 1. User enters  │───▶│ 2. POST to       │───▶│ 4. Welcome msg   │
│    credentials  │    │    /api/users/    │    │ 5. Redirect to   │
│ 2. Clicks       │    │    login         │    │    dashboard     │
│    "Sign In"    │    │ 3. JSON response │    │                  │
└─────────────────┘    └──────────────────┘    └──────────────────┘
         │                        │
         │              ┌─────────▼──────────┐    ┌──────────────────┐
         │              │  Network Error     │    │  Fallback Path   │
         │              │                    │    │                  │
         └──────────────▶│ 3. AJAX fails     │───▶│ 4. Form POST to  │
                        │ 4. Automatic      │    │    /login        │
                        │    fallback       │    │ 5. Server-side   │
                        │                    │    │    redirect      │
                        └────────────────────┘    └──────────────────┘
```

### **Pages & Endpoints**

#### **Frontend Pages**
- **`/login`** - Login page with hybrid AJAX/form submission
- **`/register`** - User registration page
- **`/dashboard`** - Protected user dashboard (requires authentication)

#### **API Endpoints**
- **`POST /api/users/login`** - AJAX login endpoint (JSON)
- **`POST /login`** - Traditional form login endpoint
- **`POST /logout`** - Secure logout with session invalidation
- **`GET /logout`** - Fallback logout endpoint (redirects to POST)

### **Session Management**

```properties
# Spring Session Configuration
spring.session.store-type=jdbc
spring.session.jdbc.initialize-schema=always
```

**Features:**
- **Database-persistent sessions** using Spring Session JDBC
- **User ID storage** instead of full user objects for security
- **Automatic session cleanup** for expired sessions
- **Cross-request session continuity** between AJAX and form submissions

### **Error Handling & Validation**

#### **Client-Side Validation**
```javascript
// Real-time form validation
- Username format validation
- Required field checking
- Dynamic error message display
- Button state management (loading, disabled)
```

#### **Server-Side Error Handling**
```java
// Comprehensive error responses
- Invalid credentials → "Invalid email or password"
- User not found → Graceful error handling
- Network issues → Automatic fallback to form submission
- Session errors → Redirect to login with clear messages
```

### **Security Features**

#### **Authentication Security**
- ✅ **Password Protection**: Server-side password validation
- ✅ **Session Security**: Secure session ID generation and storage
- ✅ **CSRF Protection**: Built-in Spring Security CSRF handling
- ✅ **POST-only Logout**: Prevents CSRF logout attacks

#### **Input Sanitization**
- ✅ **Username Validation**: Proper username format checking
- ✅ **SQL Injection Protection**: JPA/Hibernate parameter binding
- ✅ **XSS Prevention**: Thymeleaf automatic escaping

### **User Experience Features**

#### **Login Process**
1. **Clean Interface**: Single "Sign In" button with professional styling
2. **Real-time Feedback**: "Signing In..." state during authentication
3. **Success Messages**: "Login successful! Welcome, [username]"
4. **Smooth Transitions**: 1.5-second delay before dashboard redirect
5. **Error Recovery**: Clear error messages with retry capability

#### **Logout Process**
1. **Secure Logout**: POST-based logout form for security
2. **Session Cleanup**: Complete session invalidation
3. **User Feedback**: "You have been successfully logged out" message
4. **Navigation**: Automatic redirect to login page

### **Browser Compatibility**
- ✅ **Modern Browsers**: Full AJAX functionality
- ✅ **Legacy Browsers**: Automatic fallback to form submission
- ✅ **Mobile Devices**: Responsive design with touch-friendly interface
- ✅ **Network Issues**: Graceful degradation when AJAX fails

### **Development & Testing**

#### **Testing the Login System**
```bash
# Start the application
mvn spring-boot:run

# Access login page
http://localhost:8080/login

# Test credentials (existing user in database)
Username: burgerman
Password: password

# Test registration (create new user)
http://localhost:8080/register

# Test password reset
http://localhost:8080/forgot-password

# Test dashboard (after login)
http://localhost:8080/dashboard
```

#### **Password Reset Feature**
```bash
# API endpoint for password reset
POST /api/users/forgot-password
Content-Type: application/json

{
  "email": "user@example.com",
  "newPassword": "newpass123"
}

# Response: "Password updated successfully"
```

#### **Debugging Features**
```javascript
// Console logging for development
console.log('Login attempt:', { username, password });
console.log('Response status:', response.status);
console.log('Login successful:', user);
```

### **Future Enhancements**
- 🔄 **Remember Me**: Persistent login sessions
- ✅ **Password Reset**: Email-based password recovery (IMPLEMENTED)
- 🔄 **Social Login**: OAuth integration (Google, Facebook)
- 🔄 **Two-Factor Auth**: SMS/Email verification
- 🔄 **Account Lockout**: Brute force protection

---

## �🛠️ **Technologies Used**

### **Backend Framework**
- **Java 21** (Latest LTS)
- **Spring Boot 3.5.6** (Latest stable)
- **Spring Framework 6.2.11**
- **Spring Data JPA 3.5.4**
- **Jakarta Persistence API 3.1.0**

### **Frontend & Templates**
- **Thymeleaf** (Template engine)
- **HTML5/CSS3** (Responsive design)
- **Bootstrap-inspired styling**

### **Database & Security**
- **MySQL 8.0** (Production database)
- **Spring Session JDBC** (Database-persistent sessions)
- **Hibernate Validator** (Input validation)
- **Spring Security** (Session management and CSRF protection)

### **Build & Testing**
- **Maven** (Build tool)
- **JUnit 5** (Testing framework)

## 🔐 **Current Authentication Implementation**

### **Modern Authentication Features**
- **Hybrid Login System**: AJAX-first with automatic fallback to form submission
- **Spring Session JDBC**: Database-persistent session management
- **Secure User Model**: Serializable User entity for session storage
- **Comprehensive Validation**: Both client-side and server-side validation
- **Professional UI**: Clean, responsive interface with real-time feedback

### **Authentication Flow**
```java
// Current Authentication Endpoints
@PostMapping("/login")        // Form-based login
@PostMapping("/api/users/login")  // AJAX API login
@PostMapping("/logout")       // Secure logout
@GetMapping("/dashboard")     // Protected dashboard
@GetMapping("/register")      // Registration page
```

### **Session Management**
```java
// Session handling in UserController
HttpSession session = request.getSession();
session.setAttribute("userId", user.getId());
User sessionUser = userService.findById(userId);
session.invalidate(); // Secure logout
```

### **Current User Authentication Flow**
1. **Registration**: User registration with username and email validation
2. **Login**: Hybrid AJAX/form-based authentication
3. **Session**: Spring Session JDBC with user ID storage
4. **Dashboard**: Protected user dashboard with session validation
5. **Logout**: Secure POST-based logout with session invalidation
6. **Security**: CSRF protection and input validation
7. **Fallback**: Automatic fallback for network issues
```
```
## 📋 **Suggested Next Actions**

### 🎯 **Immediate Tasks (High Priority)**

1. **🔧 Complete Integration**
   ```bash
   # Test full authentication flow
   mvn test
   # Access: http://localhost:8080/auth/login
   ```

2. **🛒 Shopping Cart Integration**
   - Link cart functionality with user authentication
   - Test product-to-cart workflow
   - Verify cart persistence

3. **📦 Product Catalog Enhancement**
   - Add product images and better styling
   - Implement product search/filtering
   - Connect with cart functionality

### 🚀 **Development Improvements (Medium Priority)**

4. **🎨 UI/UX Enhancement**
   - Improve Thymeleaf templates styling
   - Add responsive design for mobile
   - Implement better error handling UI

5. **🔍 Add Validation**
   - Client-side form validation
   - Server-side input sanitization
   - Better error messaging

6. **📊 Dashboard Features**
   - User profile management
   - Order history
   - Shopping preferences

### 🏗️ **Architecture Improvements (Low Priority)**

7. **🔒 Security Enhancements**
   - CSRF protection
   - Rate limiting for login attempts
   - Password strength meter

8. **📈 Performance Optimization**
   - Database indexing
   - Caching strategies
   - Query optimization

9. **🧪 Testing Coverage**
   - Unit tests for all services
   - Integration tests for controllers
   - End-to-end testing

### 🚀 **Production Readiness**

10. **🌐 Production Configuration**
    - MySQL database setup
    - Environment-specific configs
    - Logging configuration

11. **📦 Deployment**
    - Docker containerization
    - CI/CD pipeline setup
    - Cloud deployment preparation
```
```
## 💻 **Development Commands**

### **Maven Commands**
```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package as JAR
mvn package

# Run application
mvn spring-boot:run

# Install dependencies
mvn clean install
```

### **Useful URLs**
```bash
# REST API endpoints
http://localhost:8080/api/users/register
http://localhost:8080/api/users/login
http://localhost:8080/api/users/all


## 🏗️ **Architecture Benefits**

### ✅ **Current Strengths**
- **Development-Optimized**: Simplified authentication for rapid prototyping
- **Enhanced SessionManager**: Comprehensive cart, checkout, and browse history support
- **Modern Framework**: Latest Spring Boot 3.5.6 with Java 21
- **Flexible Architecture**: Easy to switch between development and production modes
- **Clean REST API**: JSON-based endpoints for modern frontend integration
- **Testable Design**: Simple authentication logic, easy to mock and test
- **Production Ready**: Complex authentication system safely archived for future use

### 🎯 **Design Patterns Used**
- **Dependency Injection**: Spring-managed service dependencies
- **Repository Pattern**: Data access abstraction
- **Service Layer Pattern**: Business logic separation
- **MVC Pattern**: Controller-Service-Repository structure

## 🤝 **Contributing**

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Make your changes following the current architecture
4. Test your changes: `mvn test`
5. Commit changes: `git commit -m 'Add feature'`
6. Push to branch: `git push origin feature-name`
7. Submit a pull request

## 🆘 **Troubleshooting**

### **Common Issues**

#### Port Already in Use
```bash
# Check what's using port 8080
netstat -an | findstr :8080    # Windows
lsof -i :8080                  # macOS/Linux
```

#### Java Version Issues
```bash
# Check Java version
java -version                  # Should show Java 21

# Check Maven version
mvn -version                   # Should use Java 21
```

#### Database Connection Issues
```bash
# Clear H2 database
# Delete /target directory and restart application
mvn clean spring-boot:run
```

## 📄 **License**

This project is part of Team7's coursework and is intended for educational purposes.

---

## 📚 **Additional Resources**

- **[Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)**
- **[Java 21 Features](https://openjdk.org/projects/jdk/21/)**
- **[Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)**
- **[Maven Documentation](https://maven.apache.org/guides/)**

```
<!-- Shafik Extra Notes -->

<!-- Basic Step-->
<!-- // Implement MySQL, create table users for all models. (7)
// CREATE TABLE users (
//     userID BIGINT AUTO_INCREMENT PRIMARY KEY,
//     username VARCHAR(50) UNIQUE NOT NULL,
//     email VARCHAR(100) UNIQUE NOT NULL,
//     password VARCHAR(255) NOT NULL
// );

// Implement Node.js server for REST API (8)
// API receive request from application, process it, and send response back to application.
// Use Express.js framework to create server and define routes.
// ensure secure communication with HTTPS and proper authentication mechanisms.
// ensure port 8080 is open and accessible for incoming requests.
// create table users (
    userID BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL -->

<!-- Consider if password can store in database VS stored securely where it cannot be accessed (hacked) -->

<!-- SHA256 or just store in db -->
<!-- Login Functions  -->
<!-- !-- 1) Register User -->
<!-- 2) Login/Logout -->

<!-- Update User Detail
If user forget password
If user forget username
If user forget registered email

If user want to change username
If user want to delete browsing history -->

<!-- Considering React.js for interactive login page with elements -->
<!-- Button to forget password? Autofill?? -->
```
