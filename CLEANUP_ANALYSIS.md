# File Cleanup Analysis for Authentication-Focused App

## 🗂️ **KEEP - Essential Authentication Files**

### Controllers (Keep)
- ✅ `UserController.java` - Handles login, logout, registration, password reset

### Services (Keep)  
- ✅ `UserService.java` - User management and authentication
- ✅ `UserServiceInterface.java` - Service interface
- ✅ `SessionManager.java` - Enhanced session management

### Repositories (Keep)
- ✅ `UserRepository.java` - User data access

### Models (Keep)
- ✅ `User.java` - User entity

### Templates (Keep)
- ✅ `login.html` - Login page
- ✅ `forgot-password.html` - Password reset
- ✅ `dashboard.html` - User dashboard

### Configuration (Keep)
- ✅ `ECommerceApplication.java` - Main application
- ✅ `application.properties` - App configuration
- ✅ `pom.xml` - Dependencies
- ✅ `schema.sql` / `data.sql` - Database setup

---

## 🗑️ **REMOVE - Unnecessary E-commerce Files**

### Controllers (Remove - 6 files)
- ❌ `CartController.java` - Cart management (using SessionManager instead)
- ❌ `ProductController.java` - Product catalog (not needed for auth)
- ❌ `CheckoutController.java` - Payment processing (not needed)
- ❌ `ReviewController.java` - Product reviews (not needed)
- ❌ `PurchaseHistoryController.java` - Order history (not needed)
- ❌ `HomePageController.java` - Homepage (not needed)

### Services (Remove - 13 files)
- ❌ `PaymentService.java` / `PaymentServiceImpl.java` - Payment processing
- ❌ `CartInterface.java` / `CartImplementation.java` - Cart logic
- ❌ `CartItemInterface.java` / `CartItemImplementation.java` - Cart items
- ❌ `ProductInterface.java` / `ProductImplementation.java` - Product logic
- ❌ `OrderService.java` / `OrderServiceImpl.java` - Order processing
- ❌ `CustomerService.java` / `CustomerServiceImpl.java` - Customer management
- ❌ `ShoppingService.java` - Shopping logic
- ❌ `PurchaseHistoryService.java` - Purchase history

### Repositories (Remove - 6 files)
- ❌ `CartRepository.java` - Cart data access
- ❌ `CartItemRepository.java` - Cart item data
- ❌ `ProductRepository.java` - Product data
- ❌ `OrderRepository.java` - Order data
- ❌ `OrderItemRepository.java` - Order item data
- ❌ `CustomerRepository.java` - Customer data
- ❌ `ReviewRepository.java` - Review data

### Models (Remove - 7 files)
- ❌ `Product.java` - Product entity
- ❌ `Cart.java` - Cart entity
- ❌ `CartItem.java` - Cart item entity
- ❌ `Order.java` - Order entity
- ❌ `OrderItem.java` - Order item entity
- ❌ `Customer.java` - Customer entity
- ❌ `Payment.java` - Payment entity
- ❌ `Review.java` - Review entity

### Templates (Remove - 8 files)
- ❌ `displayProducts.html` - Product listing
- ❌ `ViewDetails.html` - Product details
- ❌ `searchProductResult.html` - Search results
- ❌ `cart-products.html` - Shopping cart
- ❌ `checkout.html` - Checkout process
- ❌ `order-confirmation.html` - Order confirmation
- ❌ `PurchaseHistory.html` - Order history
- ❌ `register.html` - User registration (if using API only)

### Configuration (Remove - 1 file)
- ❌ `SessionConfig.java` - Custom session config (not needed)

---

## 📊 **Summary**
- **Total Files to Remove**: ~40 files
- **Files to Keep**: ~15 essential files
- **Space Saved**: Significant reduction in codebase complexity
- **Focus**: Pure authentication system with enhanced session management

## 🎯 **Result**
Clean, focused authentication application with:
- User login/logout/registration
- Password reset functionality  
- Enhanced session management (cart persistence, browse history)
- Database integration
- Clean architecture