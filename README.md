# Team7 eCommerce Shopping App

## Overview
This is a Java-based e-commerce shopping application upgraded to use Java 21 (the latest LTS version) with Spring Boot 3.5.6 and Jakarta EE.

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

### Technologies Used:
- **Java 21** (Latest LTS)
- **Spring Boot 3.5.6** (Latest stable)
- **Spring Framework 6.2.11** (Latest compatible)
- **Spring Data JPA 3.5.4** (Latest compatible)
- **Jakarta Persistence API 3.1.0**
- **H2 Database** (for development)
- **Maven** (build tool)
- **JUnit 5** (testing framework)

## Prerequisites
To run this application, you need:
- **Java 21** installed on your system
- **Maven 3.8+** (or use the Maven wrapper)

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/ecommerce/
│   │       ├── ECommerceApplication.java (Main application)
│   │       └── model/
│   │           ├── Product.java
│   │           ├── Order.java
│   │           └── Customer.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/ecommerce/
            └── model/
                └── ProductTest.java
```

## Getting Started

### 1. Install Java 21
Download and install Java 21 from:
- [Oracle JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
- [OpenJDK 21](https://adoptium.net/temurin/releases/?version=21)

### 2. Verify Java Version
```cmd
java -version
```
Should show: `openjdk version "21.x.x"` or `java version "21.x.x"`

### 3. Build the Project
```cmd
mvn clean compile
```

### 4. Run Tests
```cmd
mvn test
```

### 5. Run the Application
```cmd
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 6. Access H2 Database Console (Development)
Visit: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## Key Features of Java 21 Upgrade

### Performance Improvements
- **Virtual Threads**: Better concurrency handling
- **Improved Garbage Collection**: Enhanced G1GC and ZGC
- **Startup Time**: Faster application startup

### Language Features (Available in Java 21)
- **Pattern Matching**: Enhanced switch expressions
- **Record Classes**: Immutable data carriers
- **Text Blocks**: Multi-line string literals
- **Local Variable Type Inference**: `var` keyword

### Migration Benefits
- **Long-term Support**: Java 21 is an LTS release (supported until 2031)
- **Security Updates**: Latest security patches and improvements
- **Performance**: Significant performance improvements over older versions
- **Modern APIs**: Access to the latest Java APIs and features

## Build and Deployment

### Maven Commands
```cmd
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package as JAR
mvn package

# Run the packaged application
java -jar target/ecommerce-shopping-app-1.0.0.jar
```

### IDE Setup
This project is compatible with all major IDEs that support Java 21:
- **IntelliJ IDEA 2023.2+**
- **Eclipse 2023-09+**
- **VS Code** with Java Extension Pack

## Next Steps
1. Install Java 21 on your system
2. Run `mvn clean compile` to download dependencies and compile
3. Run `mvn test` to ensure everything works
4. Start developing with modern Java 21 features!

## Support
For any issues with the Java 21 upgrade, please check:
1. Java version is correctly set to 21
2. JAVA_HOME environment variable points to Java 21
3. Maven is using the correct Java version