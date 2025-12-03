package org.example.customer;

import org.example.dto.CustomerNameEmail;
import org.example.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // --- Derived queries ---

    Optional<Customer> findByEmail(String email);

    List<Customer> findByLastName(String lastName);

    List<Customer> findByLastNameIgnoreCase(String lastName);

    List<Customer> findByFirstNameContainingIgnoreCase(String partOfName);

    List<Customer> findByEmailEndingWith(String domain);

    List<Customer> findByLastNameOrderByFirstNameAsc(String lastName);

    List<Customer> findByFirstNameAndLastName(String firstName, String lastName);

    List<Customer> findByFirstNameStartingWithIgnoreCase(String prefix);

    List<Customer> findByFirstNameEndingWithIgnoreCase(String suffix);

    List<Customer> findByFirstNameIn(List<String> firstNames);

    List<Customer> findByLastNameNot(String lastName);

    List<Customer> findByEmailIsNull();

    List<Customer> findByEmailIsNotNull();

    List<Customer> findByFirstNameOrLastNameAllIgnoreCase(String firstName, String lastName);

    boolean existsByEmail(String email);

    long countByLastName(String lastName);

    List<Customer> findTop3ByLastNameOrderByFirstNameAsc(String lastName);

    List<Customer> findFirst5ByOrderByLastNameAsc();

    // --- Simple JPQL @Query examples ---

    @Query("SELECT c FROM Customer c WHERE LOWER(c.lastName) = LOWER(:lastName)")
    List<Customer> searchByLastNameCaseInsensitive(@Param("lastName") String lastName);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :namePart, '%'))")
    List<Customer> searchByFirstNameLike(@Param("namePart") String namePart);

    @Query("SELECT c FROM Customer c WHERE c.email IS NULL")
    List<Customer> findCustomersWithNoEmail();

    @Query("SELECT c FROM Customer c WHERE c.email LIKE :domain")
    List<Customer> findCustomersByEmailDomain(@Param("domain") String domain);

    @Query("SELECT c FROM Customer c WHERE c.id IN :ids")
    List<Customer> findByIdIn(@Param("ids") List<Long> ids);

    @Query("SELECT c.email FROM Customer c WHERE c.lastName = :lastName")
    List<String> findEmailsByLastName(@Param("lastName") String lastName);

    @Query("SELECT new org.example.dto.CustomerNameEmail(c.firstName, c.lastName, c.email) " +
            "FROM Customer c WHERE c.lastName = :lastName")
    List<CustomerNameEmail> findNameAndEmailByLastName(@Param("lastName") String lastName);

    // --- JPQL with JOIN to orders ---

    @Query("SELECT DISTINCT c FROM Customer c JOIN c.orders o WHERE o.amount > :minAmount")
    List<Customer> findCustomersWithOrdersGreaterThan(@Param("minAmount") BigDecimal minAmount);

    @Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.orders WHERE c.id = :id")
    Optional<Customer> findByIdWithOrdersFetched(@Param("id") Long id);

    // --- Aggregation with GROUP BY (JPQL) ---

    @Query("SELECT c, AVG(o.amount) FROM Customer c JOIN c.orders o GROUP BY c")
    List<Object[]> findCustomersWithAverageOrderAmount();

    @Query("SELECT c.id, COUNT(o) FROM Customer c LEFT JOIN c.orders o GROUP BY c.id")
    List<Object[]> countOrdersPerCustomer();

    // --- Native query examples ---

    @Query(
            value = "SELECT * FROM customer c WHERE c.email LIKE CONCAT('%', :domain, '%')",
            nativeQuery = true
    )
    List<Customer> findCustomersByEmailDomainNative(@Param("domain") String domain);

    @Query(
            value = "SELECT * FROM customer c WHERE LOWER(c.first_name) LIKE LOWER(CONCAT('%', :namePart, '%'))",
            nativeQuery = true
    )
    List<Customer> searchByFirstNameLikeNative(@Param("namePart") String namePart);

    @Query(
            value = "SELECT c.* FROM customer c " +
                    "JOIN orders o ON o.customer = c.id " +
                    "WHERE o.amount > :minAmount " +
                    "GROUP BY c.id",
            nativeQuery = true
    )
    List<Customer> findCustomersWithBigOrdersNative(@Param("minAmount") BigDecimal minAmount);

    @Query(
            value = "SELECT DISTINCT c.* FROM customer c " +
                    "LEFT JOIN orders o ON o.customer = c.id " +
                    "WHERE o.id IS NULL",
            nativeQuery = true
    )
    List<Customer> findCustomersWithoutOrdersNative();

    @Query(
            value = "SELECT c.email FROM customer c WHERE c.last_name = :lastName",
            nativeQuery = true
    )
    List<String> findEmailsByLastNameNative(@Param("lastName") String lastName);

    // --- Update queries (JPQL + native) ---

    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.email = :newEmail WHERE c.id = :id")
    int updateEmailById(@Param("id") Long id, @Param("newEmail") String newEmail);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE customer SET last_name = :newLastName WHERE id = :id",
            nativeQuery = true
    )
    int updateLastNameNative(@Param("id") Long id, @Param("newLastName") String newLastName);
}
