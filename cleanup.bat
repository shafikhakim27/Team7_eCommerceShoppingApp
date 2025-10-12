@echo off
echo Starting cleanup of unnecessary e-commerce files...
echo.

echo Removing unnecessary controllers...
del "src\main\java\com\ecommerce\controller\CartController.java" 2>nul
del "src\main\java\com\ecommerce\controller\ProductController.java" 2>nul  
del "src\main\java\com\ecommerce\controller\CheckoutController.java" 2>nul
del "src\main\java\com\ecommerce\controller\ReviewController.java" 2>nul
del "src\main\java\com\ecommerce\controller\PurchaseHistoryController.java" 2>nul
del "src\main\java\com\ecommerce\controller\HomePageController.java" 2>nul

echo Removing unnecessary services...
del "src\main\java\com\ecommerce\service\PaymentService.java" 2>nul
del "src\main\java\com\ecommerce\service\PaymentServiceImpl.java" 2>nul
del "src\main\java\com\ecommerce\service\CartInterface.java" 2>nul
del "src\main\java\com\ecommerce\service\CartImplementation.java" 2>nul
del "src\main\java\com\ecommerce\service\CartItemInterface.java" 2>nul
del "src\main\java\com\ecommerce\service\CartItemImplementation.java" 2>nul
del "src\main\java\com\ecommerce\service\ProductInterface.java" 2>nul
del "src\main\java\com\ecommerce\service\ProductImplementation.java" 2>nul
del "src\main\java\com\ecommerce\service\OrderService.java" 2>nul
del "src\main\java\com\ecommerce\service\OrderServiceImpl.java" 2>nul
del "src\main\java\com\ecommerce\service\CustomerService.java" 2>nul
del "src\main\java\com\ecommerce\service\CustomerServiceImpl.java" 2>nul
del "src\main\java\com\ecommerce\service\ShoppingService.java" 2>nul
del "src\main\java\com\ecommerce\service\PurchaseHistoryService.java" 2>nul

echo Removing unnecessary repositories...
del "src\main\java\com\ecommerce\repository\CartRepository.java" 2>nul
del "src\main\java\com\ecommerce\repository\CartItemRepository.java" 2>nul
del "src\main\java\com\ecommerce\repository\ProductRepository.java" 2>nul
del "src\main\java\com\ecommerce\repository\OrderRepository.java" 2>nul
del "src\main\java\com\ecommerce\repository\OrderItemRepository.java" 2>nul
del "src\main\java\com\ecommerce\repository\CustomerRepository.java" 2>nul
del "src\main\java\com\ecommerce\repository\ReviewRepository.java" 2>nul

echo Removing unnecessary models...
del "src\main\java\com\ecommerce\model\Product.java" 2>nul
del "src\main\java\com\ecommerce\model\Cart.java" 2>nul
del "src\main\java\com\ecommerce\model\CartItem.java" 2>nul
del "src\main\java\com\ecommerce\model\Order.java" 2>nul
del "src\main\java\com\ecommerce\model\OrderItem.java" 2>nul
del "src\main\java\com\ecommerce\model\Customer.java" 2>nul
del "src\main\java\com\ecommerce\model\Payment.java" 2>nul
del "src\main\java\com\ecommerce\model\Review.java" 2>nul

echo Removing unnecessary templates...
del "src\main\resources\templates\displayProducts.html" 2>nul
del "src\main\resources\templates\ViewDetails.html" 2>nul
del "src\main\resources\templates\searchProductResult.html" 2>nul
del "src\main\resources\templates\cart-products.html" 2>nul
del "src\main\resources\templates\checkout.html" 2>nul
del "src\main\resources\templates\order-confirmation.html" 2>nul
del "src\main\resources\templates\PurchaseHistory.html" 2>nul
del "src\main\resources\templates\register.html" 2>nul

echo Removing unnecessary config...
del "src\main\java\com\ecommerce\config\SessionConfig.java" 2>nul

echo.
echo Cleanup completed! 
echo Remaining files focus on authentication and session management.
echo.
pause