# Team7 eCommerce Shopping App

## Overview
This is a modern Java-based e-commerce shopping application built with **Java 21** and **Spring Boot 3.5.6**, featuring a clean architecture with separated concerns for user management, authentication, and business logic.

## 🎯 **Current Project Status**

### ✅ **Recently Completed**
- **Java 21 LTS Upgrade**: Latest long-term support version
- **Spring Boot 3.5.6**: Latest stable framework version
- **Clean Architecture Implementation**: Separated password/authentication logic
- **Service Layer Optimization**: Simplified and streamlined service classes
- **Repository Layer**: Clean data access with custom queries
- **Modern Security**: SHA-256 password hashing with proper verification

### 🏗️ **Current Architecture**

```
📦 Clean Service Architecture
┌─────────────────┐    ┌──────────────────┐    ┌────────────────┐
│   UserService   │    │PasswordService   │    │ SessionManager │
│ (User CRUD)     │◄───┤ (Security +      │    │ (Session Mgmt) │
│ - Find users    │    │  Authentication) │    │ - Login/logout │
│ - Check exists  │    │ - Validate       │    │ - Current user │
│ - Save/register │    │ - Encrypt        │    │ - Status check │
│                 │    │ - Authenticate   │    │                │
└─────────────────┘    └──────────────────┘    └────────────────┘
```

## 📁 **Current Project Structure**

### **Main Application Structure**
```
src/main/java/com/ecommerce/
├── ECommerceApplication.java          # 🚀 Spring Boot main class
├── controller/
│   ├── AuthController.java            # 🔐 Authentication endpoints (/auth/*)
│   ├── CartController.java            # 🛒 Shopping cart management
│   └── ProductController.java         # 📦 Product catalog operations
├── model/
│   ├── User.java                      # 👤 User entity (JPA with validation)
│   ├── Product.java                   # 📦 Product entity
│   ├── Order.java                     # 📋 Order entity
│   ├── OrderItem.java                 # 📝 Order line items
│   ├── Customer.java                  # 👥 Customer entity
│   ├── Cart.java                      # 🛒 Shopping cart entity
│   └── CartItem.java                  # 🛍️ Cart item entity
├── repository/
│   ├── UserRepository.java            # 💾 User data access (6 methods)
│   ├── ProductRepository.java         # 📦 Product data access
│   ├── CartRepository.java            # 🛒 Cart data access
│   ├── CartItemRepository.java        # 🛍️ Cart item data access
│   └── CustomerRepository.java        # 👥 Customer data access
└── service/
    ├── UserService.java               # 👤 User business logic (simplified)
    ├── UserServiceInterface.java      # 📋 User service contract (7 methods)
    ├── PasswordServiceImpl.java       # 🔐 Password operations + authentication
    ├── PasswordServiceInterface.java  # 📋 Password service contract (clean docs)
    ├── SessionManager.java            # 🔄 Session management (4 methods)
    ├── ProductImplementation.java     # 📦 Product business logic
    ├── ProductInterface.java          # 📋 Product service contract
    ├── CartImplementation.java        # 🛒 Cart business logic
    ├── CartInterface.java             # 📋 Cart service contract
    ├── CartItemImplementation.java    # 🛍️ Cart item business logic
    └── CartItemInterface.java         # 📋 Cart item service contract
```

### **Resources Structure**
```
src/main/resources/
├── application.properties             # ⚙️ Application configuration
├── data.sql                          # 📊 Sample data script
└── templates/                         # 🎨 Thymeleaf HTML templates
    ├── login.html                     # 🔐 Login page (styled)
    ├── register.html                  # 📝 Registration page (styled)
    └── dashboard.html                 # 📊 User dashboard (styled)
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
# Main application: http://localhost:8080
# H2 Database console: http://localhost:8080/h2-console
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
- **SHA-256** (Password hashing)
- **Session-based** authentication

### **Build & Testing**
- **Maven** (Build tool)
- **JUnit 5** (Testing framework)

## 🔐 **Authentication & Security Features**

### **Password Management**
- **SHA-256 Hashing**: Secure password encryption
- **Password Validation**: 8+ chars, uppercase, lowercase, digit, special char
- **Session Management**: Secure user sessions
- **Clean Architecture**: Separated password logic from user logic

### **User Authentication Flow**
1. **Registration**: Create account with encrypted password
2. **Login**: Authenticate with username/email and password
3. **Session**: Maintain logged-in state
4. **Logout**: Secure session termination

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
# Main application
http://localhost:8080

# Authentication endpoints
http://localhost:8080/auth/login
http://localhost:8080/auth/register
http://localhost:8080/auth/logout

# Database console
http://localhost:8080/h2-console
```

## 🏗️ **Architecture Benefits**

### ✅ **Current Strengths**
- **Clean Separation**: UserService ↔ PasswordService ↔ SessionManager
- **Modern Framework**: Latest Spring Boot 3.5.6 with Java 21
- **Secure Authentication**: SHA-256 hashing with proper verification
- **Simple Interfaces**: Clean, focused service contracts
- **Testable Design**: Easy to mock and unit test

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

// Implement MySQL, create table users for all models. (7)
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