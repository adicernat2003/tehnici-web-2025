package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.repository.OrderRepository;
import org.example.model.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order create(Order order) {
        if (order.getCreatedAt() == null) {
            order.setCreatedAt(OffsetDateTime.now());
        }
        return orderRepository.save(order);
    }

    public List<Order> findBigOrdersForCustomer(Long customerId, BigDecimal minAmount) {
        return orderRepository.findBigOrdersForCustomer(customerId, minAmount);
    }
}