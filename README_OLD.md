# Team7 eCommerce Shopping App

## Overview
This repository contains a Java-based e-commerce shopping application built with Java 21 and Spring Boot 3.5.6, featuring a complete user authentication system with password security.

## 📁 Repository Structure

This project contains **two separate Spring Boot applications**:

### 🏪 Main eCommerce Application
- **Location:** `/src/main/`
- **Package:** `com.ecommerce.*`
- **Port:** `8080`
- **Purpose:** Full-featured eCommerce shopping platform

### 🧪 Login Test Application  
- **Location:** `/test/login-test-app/`
- **Package:** `com.logintest.*`
- **Port:** `8081`
- **Purpose:** Standalone login functionality testing

> 📋 See [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md) for detailed information about running both applications.

## 🚀 Quick Start

### Main eCommerce App
```bash
mvn spring-boot:run
# Access: http://localhost:8080
```

### Login Test App
```bash
cd test/login-test-app
mvn spring-boot:run
# Access: http://localhost:8081/login
```

## Java Version Upgrade
This project has been upgraded to **Java 21** (the latest Long Term Support version) from the previous version. Key improvements include:

### Upgrade Changes Made:
1. **Java Runtime**: Upgraded to Java 21 (LTS)
2. **Spring Boot**: Updated to version 3.5.6 (latest stable version)
3. **Spring Framework**: Updated to version 6.2.11 (included with Spring Boot 3.5.6)
4. **Spring Data JPA**: Updated to version 3.5.4 (latest compatible version)
5. **Jakarta EE**: Using Jakarta Persistence API 3.1.0
6. **Build Tool**: Configured Maven with Java 21 target
7. **Project Structure**: Organized into proper Maven directory structure

## 🔐 Authentication & Security Features

### Password Management
- **SHA-256 Hashing**: Secure password encryption with salt
- **Password Validation**: Minimum length requirements
- **Session Management**: Secure user sessions
- **Protected Routes**: Authentication-required pages

### User Authentication Flow
1. **Registration**: Create account with encrypted password
2. **Login**: Authenticate with username/email and password  
3. **Session**: Maintain logged-in state
4. **Logout**: Secure session termination

## 🛠️ Technologies Used

### Backend Framework
- **Java 21** (Latest LTS)
- **Spring Boot 3.5.6** (Latest stable)
- **Spring Framework 6.2.11**
- **Spring Data JPA 3.5.4**
- **Jakarta Persistence API 3.1.0**

### Frontend & Templates  
- **Thymeleaf** (Template engine)
- **HTML5/CSS3** (Responsive design)
- **Bootstrap-inspired styling**

### Database & Security
- **H2 Database** (Development/Testing)
- **MySQL** (Production ready)
- **SHA-256** (Password hashing)
- **Session-based** authentication

### Build & Testing
- **Maven** (Build tool)
- **JUnit 5** (Testing framework)

## Prerequisites
To run this application, you need:
- **Java 21** installed on your system
- **Maven 3.8+** (or use the Maven wrapper)

## Project Structure

### Main Application Structure
```
src/main/java/com/ecommerce/
├── ECommerceApplication.java          # Main Spring Boot application
├── controller/
│   └── AuthController.java            # Authentication endpoints
├── dto/
│   └── LoginRequest.java              # Login form data transfer object
├── model/
│   ├── User.java                      # User entity with authentication
│   ├── Product.java                   # Product entity
│   ├── Order.java                     # Order entity
│   ├── Customer.java                  # Customer entity
│   ├── Cart.java                      # Shopping cart entity
│   └── CartItem.java                  # Cart item entity
├── repository/
│   ├── UserRepository.java            # User data access
│   └── ProductRepository.java         # Product data access
└── service/
    ├── UserService.java               # User business logic
    ├── PasswordService.java           # Password encryption/validation
    └── SessionManager.java            # Session management
```

### Test Application Structure  
```
test/login-test-app/src/main/java/com/logintest/
├── LoginTestApplication.java          # Standalone test application
├── controller/
│   └── AuthController.java            # Test authentication controller
├── dto/
│   └── LoginRequest.java              # Test login DTO
├── model/
│   └── User.java                      # Test user entity
├── repository/
│   └── UserRepository.java            # Test user repository
└── service/
    ├── UserService.java               # Test user service
    ├── PasswordService.java           # Test password service
    └── SessionManager.java            # Test session manager
```

### Resources Structure
```
src/main/resources/
├── application.properties             # Main app configuration
└── templates/                         # Thymeleaf HTML templates
    ├── login.html                     # Login page
    ├── register.html                  # Registration page
    └── dashboard.html                 # User dashboard

test/login-test-app/src/main/resources/
├── application.properties             # Test app configuration  
└── templates/                         # Test app templates
    ├── login.html                     # Test login page
    ├── register.html                  # Test registration page
    ├── dashboard.html                 # Test dashboard
    └── users.html                     # User list page
```

## Getting Started

### Prerequisites
- **Java 21** installed on your system
- **Maven 3.8+** (or use the Maven wrapper)

### Installation & Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/shafikhakim27/Team7_eCommerceShoppingApp.git
   cd Team7_eCommerceShoppingApp
   ```

2. **Verify Java 21**
   ```bash
   java -version
   # Should show: java version "21.x.x"
   ```

3. **Build the Main Project**
   ```bash
   mvn clean compile
   ```

4. **Run Tests**
   ```bash
   mvn test
   ```

### Running Applications

#### 🏪 Main eCommerce Application
```bash
# Run from project root
mvn spring-boot:run

# Or run packaged JAR
mvn package
java -jar target/ecommerce-shopping-app-1.0.0.jar
```
**Access at:** http://localhost:8080

#### 🧪 Login Test Application
```bash
# Navigate to test app directory
cd test/login-test-app

# Run the test application
mvn spring-boot:run
```
**Access at:** http://localhost:8081/login

### Testing the Login System

1. **Open Login Test App:** http://localhost:8081/login
2. **Register New Account:**
   - Click "Register here"  
   - Fill username, email, password (min 6 chars)
   - Submit form
3. **Login with Credentials:**
   - Return to login page
   - Enter username/email and password
   - Access dashboard upon successful login
4. **Explore Features:**
   - View user dashboard
   - Check "View Users" page
   - Test logout functionality

## 🔍 Database Access

### H2 Console (Development)

**Main App Database:**
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

**Test App Database:**
- URL: http://localhost:8081/h2-console  
- JDBC URL: `jdbc:h2:mem:logintest_db`
- Username: `test_user`
- Password: `test_password`

### MySQL Setup (Production)

Uncomment MySQL configuration in `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=root
spring.datasource.password=your_mysql_password
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

## 🏗️ Architecture & Features

### Authentication System
- **Secure Registration**: Password hashing with SHA-256
- **Login Validation**: Username/email authentication
- **Session Management**: Server-side session handling
- **Route Protection**: Authenticated access control
- **Password Security**: Salted hash storage (no plain text)

### Application Features
- **User Management**: Registration, login, logout
- **Product Catalog**: Product entities and repositories
- **Shopping Cart**: Cart and cart item functionality
- **Order System**: Order management entities
- **Customer Management**: Customer entity handling
- **Responsive UI**: Mobile-friendly Thymeleaf templates

## 🔧 Development

### Maven Commands
```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package as JAR
mvn package

# Install dependencies
mvn clean install
```

### IDE Support
Compatible with all major IDEs supporting Java 21:
- **IntelliJ IDEA 2023.2+**
- **Eclipse 2023-09+**  
- **VS Code** with Java Extension Pack

### Code Structure Guidelines
- **Controllers**: Handle HTTP requests and responses
- **Services**: Business logic and data processing
- **Repositories**: Data access layer
- **DTOs**: Data transfer objects for forms
- **Models/Entities**: JPA entity classes

## 🚀 Next Steps

### For Development

1. **Set up Environment**: Install Java 21 and Maven 3.8+
2. **Clone & Build**: `git clone` → `mvn clean install`
3. **Test Login System**: Run test app at localhost:8081
4. **Develop Features**: Add new functionality to main app
5. **Test Integration**: Verify main app login integration

### For Production

1. **Configure MySQL**: Update database settings
2. **Security Review**: Audit authentication implementation
3. **Performance Testing**: Load test both applications
4. **Deployment**: Deploy main eCommerce app only

## 📚 Additional Resources

- **[PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md)**: Detailed project structure guide
- **[Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)**
- **[Java 21 Features](https://openjdk.org/projects/jdk/21/)**
- **[Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)**

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Commit changes: `git commit -m 'Add feature'`
4. Push to branch: `git push origin feature-name`
5. Submit a pull request

## 📄 License

This project is part of Team7's coursework and is intended for educational purposes.

## 🆘 Troubleshooting

### Common Issues

#### Port Already in Use

- Check if applications are already running
- Use `netstat -an | findstr :8080` (Windows) or `lsof -i :8080` (macOS/Linux)

#### Java Version Conflicts

- Verify `JAVA_HOME` points to Java 21
- Check `java -version` and `mvn -version`

#### Database Connection Issues

- Ensure H2 console URLs are correct
- Clear browser cache when switching between apps

#### Build Failures

- Run `mvn clean install` to refresh dependencies
- Check for conflicting Maven settings 
