package org.example.customer;

import org.example.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // --- Derived methods ---

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByAmountGreaterThan(BigDecimal amount);

    List<Order> findByCreatedAtBetween(OffsetDateTime createdAt, OffsetDateTime createdAt2);

    List<Order> findByCustomerEmail(String email);

    // --- JPQL query ---

    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId AND o.amount > :minAmount")
    List<Order> findBigOrdersForCustomer(@Param("customerId") Long customerId,
                                         @Param("minAmount") BigDecimal minAmount);

    // --- Native query with join ---

    @Query(
            value = """
                    SELECT o.* FROM orders o
                    JOIN customer c ON o.customer = c.id
                    WHERE c.email = :email
                    """,
            nativeQuery = true
    )
    List<Order> findOrdersByCustomerEmailNative(@Param("email") String email);
}
