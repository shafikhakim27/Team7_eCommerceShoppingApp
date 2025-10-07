# Project Structure Guide

## 📁 Current Repository Structure

This is a **single, unified Spring Boot application** with a clean, modern architecture.

### 🏪 Main eCommerce Application
- **Location:** `/src/main/`
- **Package:** `com.ecommerce.*`
- **Port:** `8080`
- **Database:** H2 (Development) / MySQL (Production)
- **Architecture:** Clean service separation with modern Spring Boot 3.5.6
- **Purpose:** Full-featured eCommerce shopping platform

**To run:**
```bash
mvn spring-boot:run
# Access: http://localhost:8080
# H2 Console: http://localhost:8080/h2-console
```

## 🏗️ **Clean Architecture Overview**

```
📦 Service Layer Architecture
┌─────────────────┐    ┌──────────────────┐    ┌────────────────┐
│   UserService   │    │PasswordService   │    │ SessionManager │
│ (User CRUD)     │◄───┤ (Security +      │    │ (Session Mgmt) │
│ - Find users    │    │  Authentication) │    │ - Login/logout │
│ - Check exists  │    │ - Validate       │    │ - Current user │
│ - Save/register │    │ - Encrypt        │    │ - Status check │
│                 │    │ - Authenticate   │    │                │
└─────────────────┘    └──────────────────┘    └────────────────┘
```

## 📂 **Detailed Project Structure**

### **Main Application Structure**
```
src/main/java/com/ecommerce/
├── ECommerceApplication.java          # 🚀 Spring Boot main class
├── controller/                        # 🎮 Web Controllers
│   ├── AuthController.java            #   🔐 Authentication endpoints (/auth/*)
│   ├── CartController.java            #   🛒 Shopping cart management
│   └── ProductController.java         #   📦 Product catalog operations
├── model/                             # 📊 JPA Entities
│   ├── User.java                      #   👤 User entity (with validation)
│   ├── Product.java                   #   📦 Product entity
│   ├── Order.java                     #   📋 Order entity
│   ├── OrderItem.java                 #   📝 Order line items
│   ├── Customer.java                  #   👥 Customer entity
│   ├── Cart.java                      #   🛒 Shopping cart entity
│   └── CartItem.java                  #   🛍️ Cart item entity
├── repository/                        # 💾 Data Access Layer
│   ├── UserRepository.java            #   👤 User data access (6 essential methods)
│   ├── ProductRepository.java         #   📦 Product data access
│   ├── CartRepository.java            #   🛒 Cart data access
│   ├── CartItemRepository.java        #   🛍️ Cart item data access
│   └── CustomerRepository.java        #   👥 Customer data access
└── service/                           # 🔧 Business Logic Layer
    ├── UserService.java               #   � User operations (simplified to 7 methods)
    ├── UserServiceInterface.java      #   📋 User service contract
    ├── PasswordServiceImpl.java       #   🔐 Password + authentication operations
    ├── PasswordServiceInterface.java  #   📋 Password service contract (clean docs)
    ├── SessionManager.java            #   🔄 Session management (4 essential methods)
    ├── ProductImplementation.java     #   📦 Product business logic
    ├── ProductInterface.java          #   📋 Product service contract
    ├── CartImplementation.java        #   🛒 Cart business logic
    ├── CartInterface.java             #   📋 Cart service contract
    ├── CartItemImplementation.java    #   🛍️ Cart item business logic
    └── CartItemInterface.java         #   📋 Cart item service contract
```

### **Resources Structure**
```
src/main/resources/
├── application.properties             # ⚙️ App configuration (H2/MySQL)
├── data.sql                          # 📊 Sample data script (comprehensive)
└── templates/                         # 🎨 Thymeleaf HTML templates
    ├── login.html                     #   🔐 Login page (modern styling)
    ├── register.html                  #   📝 Registration page (with validation)
    └── dashboard.html                 #   📊 User dashboard (responsive)
```

### **Test Structure**
```
src/test/java/com/ecommerce/
└── model/
    └── ProductTest.java               # 🧪 JUnit 5 test example
```

## 🎯 **Service Layer Details**

### **UserService** (Simplified & Clean)
- **Methods:** 7 essential operations
- **Focus:** User CRUD operations only
- **No Password Logic:** Clean separation of concerns
```java
// Essential user operations
findById(), findByEmail(), findByUsernameOrEmail()
existsByEmail(), getAllUsers(), registerUser(), saveUser()
```

### **PasswordService** (Security Focused)
- **Methods:** 7 operations (4 core + 3 authentication)
- **Focus:** All password and authentication operations
- **Security:** SHA-256 hashing with proper verification
```java
// Core password operations
isValidPassword(), verifyPassword(), getPasswordRequirements(), encryptPassword()
// Authentication operations
registerUserWithPassword(), authenticateUser(), changeUserPassword()
```

### **SessionManager** (Streamlined)
- **Methods:** 4 essential operations
- **Focus:** HTTP session management only
- **Clean API:** Simple login/logout with status checks
```java
// Session operations
login(), getCurrentUser(), isLoggedIn(), logout()
```

## 🔧 **Configuration Details**

### **Database Configuration**
```properties
# H2 Database (Development - Default)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=password

# H2 Console Access
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### **Application Properties**
```properties
# Server Configuration
server.port=8080
spring.application.name=ecommerce-shopping-app

# JPA Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

## 🚀 **Development Commands**

### **Quick Start**
```bash
# Clone and setup
git clone https://github.com/shafikhakim27/Team7_eCommerceShoppingApp.git
cd Team7_eCommerceShoppingApp

# Build and run
mvn clean spring-boot:run
```

### **Development Workflow**
```bash
# Clean build
mvn clean compile

# Run tests
mvn test

# Package application
mvn package

# Run with profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### **Useful Development URLs**
```bash
# Main application
http://localhost:8080

# Authentication endpoints
http://localhost:8080/auth/login      # Login page
http://localhost:8080/auth/register   # Registration page
http://localhost:8080/auth/logout     # Logout

# Database console (H2)
http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:testdb
# Username: sa
# Password: password
```

## 🛠️ **Technology Stack**

### **Backend Framework**
- **Java 21** (Latest LTS)
- **Spring Boot 3.5.6** (Latest stable)
- **Spring Framework 6.2.11**
- **Spring Data JPA 3.5.4**
- **Jakarta Persistence API 3.1.0**

### **Database & Security**
- **H2 Database** (Development)
- **MySQL Support** (Production ready)
- **SHA-256** (Password hashing)
- **Session-based** authentication

### **Frontend**
- **Thymeleaf** (Template engine)
- **HTML5/CSS3** (Modern styling)
- **Responsive Design**

## 🔍 **Troubleshooting**

### **Port Already in Use**
```bash
# Check what's using port 8080
netstat -an | findstr :8080    # Windows
lsof -i :8080                  # macOS/Linux

# Change port in application.properties
server.port=8081
```

### **Java Version Issues**
```bash
# Check Java version (should be 21)
java -version

# Check Maven Java version
mvn -version

# Set JAVA_HOME if needed
set JAVA_HOME=C:\Program Files\Java\jdk-21    # Windows
export JAVA_HOME=/usr/lib/jvm/java-21         # Linux
```

### **Database Connection Issues**
```bash
# Clear H2 database (restart application)
mvn clean spring-boot:run

# Check H2 console
# URL: http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:testdb
```

### **Build Issues**
```bash
# Clean Maven cache
mvn clean install

# Force update dependencies
mvn clean install -U

# Skip tests if needed
mvn clean install -DskipTests
```

## 📋 **Development Guidelines**

### **Code Organization**
- **Controllers**: Handle HTTP requests/responses only
- **Services**: Business logic and data processing
- **Repositories**: Data access layer abstraction
- **Models**: JPA entities with validation
- **Interfaces**: Service contracts and abstractions

### **Best Practices**
1. **Separation of Concerns**: Each service has a single responsibility
2. **Dependency Injection**: Use Spring's IoC container
3. **Interface-Based Design**: Program to interfaces, not implementations
4. **Clean Architecture**: UserService ↔ PasswordService ↔ SessionManager
5. **Validation**: Use Jakarta validation annotations

### **Testing Strategy**
- **Unit Tests**: Test individual service methods
- **Integration Tests**: Test controller-service interactions
- **Repository Tests**: Test custom queries
- **Security Tests**: Test authentication flows

## 🎯 **Next Development Steps**

### **High Priority**
1. **Complete Authentication Integration** - Test full login/register flow
2. **Cart-User Integration** - Link shopping cart with authenticated users
3. **Product Management** - Connect product catalog with cart functionality

### **Medium Priority**
4. **UI/UX Improvements** - Enhanced Thymeleaf templates
5. **Validation Enhancement** - Client-side and server-side validation
6. **Error Handling** - Comprehensive error pages and messages

### **Low Priority**
7. **Security Enhancements** - CSRF protection, rate limiting
8. **Performance Optimization** - Caching, database indexing
9. **Production Setup** - MySQL configuration, logging

## 🏗️ **Architecture Benefits**

### ✅ **Current Strengths**
- **Modern Framework**: Latest Spring Boot 3.5.6 with Java 21 LTS
- **Clean Separation**: Distinct responsibilities for each service
- **Secure Design**: Proper password hashing and session management
- **Testable Architecture**: Easy to mock and unit test
- **Scalable Structure**: Ready for additional features

### 🎨 **Design Patterns Used**
- **Dependency Injection**: Spring-managed beans
- **Repository Pattern**: Data access abstraction
- **Service Layer Pattern**: Business logic separation
- **MVC Pattern**: Model-View-Controller structure
- **Interface Segregation**: Focused service interfaces