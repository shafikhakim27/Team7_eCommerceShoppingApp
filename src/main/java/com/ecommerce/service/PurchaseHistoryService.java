package com.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import ca.purchaseHistory.model.Order;
// import ca.purchaseHistory.model.OrderItem;
// import ca.purchaseHistory.repository.OrderItemRepository;
// import ca.purchaseHistory.repository.OrderRepository;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.repository.OrderItemRepository;
import com.ecommerce.repository.OrderRepository;

@Service
public class PurchaseHistoryService {
    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderItemRepository orderItemRepo;

    // 查看用户全部订单
    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepo.findByCustomerId(customerId);
    }

    // 搜索产品名相关的订单
    public List<OrderItem> searchProduct(String name) {
        return orderItemRepo.SearchOrderItemByName(name);
    }

    // 查看订单详情
    public List<OrderItem> getOrderDetails(Long id) {
        return orderItemRepo.findByOrderID(id);
    }
}