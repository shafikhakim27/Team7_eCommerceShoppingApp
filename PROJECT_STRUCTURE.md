# Project Structure Guide

## 📁 Repository Structure

This repository contains two separate Spring Boot applications:

### 🏪 Main eCommerce Application
- **Location:** `/src/main/`
- **Package:** `com.ecommerce.*`
- **Port:** `8080`
- **Database:** H2/MySQL (configurable)
- **Purpose:** Full eCommerce shopping application

**To run:**
```bash
mvn spring-boot:run
# Access: http://localhost:8080
```

### 🧪 Login Test Application
- **Location:** `/test/login-test-app/`
- **Package:** `com.logintest.*`
- **Port:** `8081`
- **Database:** H2 (separate instance)
- **Purpose:** Isolated login functionality testing

**To run:**
```bash
cd test/login-test-app
mvn spring-boot:run
# Access: http://localhost:8081/login
```

## ⚠️ Important Notes

### 🚫 Potential Conflicts
- **Do NOT run both apps from same directory**
- **Different ports prevent server conflicts**
- **Separate database instances prevent data conflicts**
- **Different package names prevent class conflicts**

### 📝 Best Practices
1. **Development:** Work on one app at a time
2. **Testing:** Use test app for login feature validation
3. **Production:** Only deploy main eCommerce app
4. **IDE:** Open each as separate project if possible

### 🔧 Configuration Differences

| Feature | Main App | Test App |
|---------|----------|----------|
| Port | 8080 | 8081 |
| Package | com.ecommerce | com.logintest |
| Database | ecommerce_db | logintest_db |
| Purpose | Production | Testing |

## 🚀 Quick Commands

**Main App:**
```bash
mvn clean install
mvn spring-boot:run
```

**Test App:**
```bash
cd test/login-test-app
mvn clean install
mvn spring-boot:run
```

**Both Apps (different terminals):**
```bash
# Terminal 1 - Main App
mvn spring-boot:run

# Terminal 2 - Test App
cd test/login-test-app && mvn spring-boot:run
```

## 🔍 Troubleshooting

### Port Already in Use
- Check if other app is running
- Use `netstat -an | findstr :8080` (Windows)
- Stop other processes or change ports

### Class Conflicts
- Ensure different package names
- Clean and rebuild: `mvn clean install`
- Restart IDE if needed

### Database Issues
- Each app uses separate H2 instance
- Clear browser cache if switching between apps
- Check H2 console URLs are different