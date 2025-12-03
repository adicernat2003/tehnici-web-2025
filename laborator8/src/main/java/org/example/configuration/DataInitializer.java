package org.example.configuration;

import org.example.repository.CustomerRepository;
import org.example.repository.OrderRepository;
import org.example.model.Customer;
import org.example.model.Order;
import org.example.model.OrderType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.example.utils.EmailGenerator.randomEmail;

@Configuration
public class DataInitializer {

    @Bean
    protected CommandLineRunner initData(CustomerRepository customerRepository,
                                         OrderRepository orderRepository) {
        return _ -> {
            Customer c1 = Customer.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .email(randomEmail())
                    .build();

            Customer c2 = Customer.builder()
                    .firstName("Jane")
                    .lastName("Smith")
                    .email(randomEmail())
                    .build();

            customerRepository.saveAll(List.of(c1, c2));

            Order o1 = Order.builder()
                    .description("Sample order 1")
                    .amount(new BigDecimal("100.50"))
                    .createdAt(OffsetDateTime.now().minusDays(1))
                    .customer(c1)
                    .type(OrderType.NEW)
                    .build();

            Order o2 = Order.builder()
                    .description("Sample order 2")
                    .amount(new BigDecimal("250.00"))
                    .createdAt(OffsetDateTime.now())
                    .customer(c1)
                    .type(OrderType.COMPLETED)
                    .build();

            Order o3 = Order.builder()
                    .description("Sample order 3")
                    .amount(new BigDecimal("50.00"))
                    .createdAt(OffsetDateTime.now())
                    .customer(c2)
                    .type(OrderType.CANCELLED)
                    .build();

            orderRepository.saveAll(List.of(o1, o2, o3));
        };
    }
}
