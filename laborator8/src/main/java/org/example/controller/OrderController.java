package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Order;
import org.example.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<Order> getAll() {
        return orderService.getAll();
    }

    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderService.create(order);
    }

    // JPQL query example
    @GetMapping("/big-orders")
    public List<Order> bigOrdersForCustomer(@RequestParam(name = "customerId") Long customerId,
                                            @RequestParam(name = "minAmount") BigDecimal minAmount) {
        return orderService.findBigOrdersForCustomer(customerId, minAmount);
    }
}