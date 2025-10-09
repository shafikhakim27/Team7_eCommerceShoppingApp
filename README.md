# Team7 eCommerce Shopping App

## Overview
This is a modern Java-based e-commerce shopping application built with **Java 21** and **Spring Boot 3.5.6**, featuring a streamlined architecture optimized for development and rapid prototyping.

## 🎯 **Current Project Status**

### ✅ **Recently Completed**
- **Java 21 LTS Upgrade**: Latest long-term support version
- **Spring Boot 3.5.6**: Latest stable framework version  
- **Simplified Authentication System**: Development-focused user management
- **Enhanced SessionManager**: Comprehensive cart, checkout, and browse history support
- **Streamlined User Model**: Reduced password requirements for easier development
- **Clean Architecture**: Moved complex login features to `target/old_login/` for future production use

### 🏗️ **Current Architecture**

```
📦 Simplified Development Architecture
┌─────────────────┐    ┌────────────────┐    ┌────────────────────┐
│   UserService   │    │ UserController │    │  SessionManager    │
│ (Simplified)    │◄───┤ (REST API)     │    │ (Enhanced)         │
│ - Find users    │    │ - Registration │    │ - Login/logout     │
│ - 3+ char pwd   │    │ - Login        │    │ - Cart management  │
│ - Basic auth    │    │ - Password chg │    │ - Checkout data    │
│ - Email unique  │    │ - User mgmt    │    │ - Browse history   │
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

### **Resources Structure**
```
src/main/resources/
├── application.properties             # ⚙️ Application configuration
├── data.sql                          # 📊 Sample data script
└── templates/                         # 🎨 Thymeleaf HTML templates (legacy)
    ├── login.html                     # 🔐 Login page (moved to old_login)
    ├── register.html                  # 📝 Registration page (moved to old_login)
    └── dashboard.html                 # 📊 User dashboard

target/old_login/                      # 📁 Previous complex login system (archived)
├── service/
│   ├── PasswordServiceInterface.java  # 🔐 Complex password validation
│   ├── PasswordServiceImpl.java       # 🔐 SHA-256 hashing + validation
│   └── SessionManager.java            # 🔄 Basic session management
├── controller/
│   └── AuthController.java            # 🎨 MVC-based authentication
├── test/
│   └── LoginTest.java                  # 🧪 Comprehensive login tests (11 tests)
└── README.md                          # 📚 Documentation of moved features
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
# H2 Database console: http://localhost:8080/h2-console
```

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
  -d '{"name":"testuser","email":"test@example.com","password":"123"}'

# Example Login
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"123"}'
```

### **Database Configuration**
```properties
# H2 Database (Default - Development)
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: password
```

## 🛠️ **Technologies Used**

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
- **H2 Database** (Development/Testing)
- **MySQL** (Production ready)
- **Plain Text Passwords** (Development phase - simplified)
- **Session-based** authentication with cart/checkout support

### **Build & Testing**
- **Maven** (Build tool)
- **JUnit 5** (Testing framework)

## 🔐 **Authentication & Session Features**

### **Simplified Development Authentication**
- **Minimal Password Requirements**: 3+ characters (easy testing)
- **Plain Text Storage**: No encryption during development phase
- **REST API**: JSON-based login/registration endpoints
- **Session Management**: Enhanced with cart and eCommerce features

### **Enhanced SessionManager Capabilities**
```java
// Authentication
sessionManager.login(user, request);
sessionManager.logout(request);
boolean isLoggedIn = sessionManager.isLoggedIn(request);

// Shopping Cart Management
sessionManager.addToCart(request, productId, "Product Name", 29.99, 2);
sessionManager.updateCartItemQuantity(request, productId, 5);
Double total = sessionManager.getCartTotal(request);
Integer itemCount = sessionManager.getCartItemCount(request);

// Checkout Process
sessionManager.saveCheckoutData(request, checkoutDetails);
Map<String, Object> checkoutData = sessionManager.getCheckoutData(request);

// Browse History
sessionManager.addToBrowseHistory(request, productId, "Product Name");
List<Map<String, Object>> history = sessionManager.getBrowseHistory(request);

// User Preferences
sessionManager.saveUserPreference(request, "favoriteCategory", "Electronics");
```

### **Development-Focused User Flow**
1. **Registration**: Simple 3+ character password requirement
2. **Login**: REST API with JSON response
3. **Session**: Automatic cart initialization and activity tracking
4. **Cart Operations**: Full session-based cart management
5. **Checkout**: Session-stored checkout data
6. **Browse History**: Automatic product view tracking
7. **Logout**: Complete session cleanup

### **Production Migration Path**
```bash
# Complex authentication system archived in:
target/old_login/
├── PasswordServiceInterface.java    # SHA-256 hashing
├── PasswordServiceImpl.java         # Complex validation  
├── AuthController.java              # MVC endpoints
└── LoginTest.java                   # 11 comprehensive tests

# To restore for production:
# 1. Copy files back to src/
# 2. Update UserService dependencies
# 3. Enable complex password validation
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

# Database console
http://localhost:8080/h2-console

# Legacy MVC endpoints (archived in target/old_login/)
# http://localhost:8080/auth/login
# http://localhost:8080/auth/register  
# http://localhost:8080/auth/logout
```

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

# Kill the process or change port in application.properties
server.port=8081
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

