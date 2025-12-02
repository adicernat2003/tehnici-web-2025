package org.example.repository;

import org.example.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // --- Derived query methods / autocomplete methods ---

    Optional<Customer> findByEmail(String email);

    List<Customer> findByLastName(String lastName);

    List<Customer> findByLastNameIgnoreCase(String lastName);

    List<Customer> findByFirstNameContainingIgnoreCase(String partOfName);

    List<Customer> findByEmailEndingWith(String domain);

    List<Customer> findByLastNameOrderByFirstNameAsc(String lastName);

    List<Customer> findByFirstNameAndLastName(String firstName, String lastName);

    // --- @Query JPQL example ---

    @Query("SELECT c FROM Customer c WHERE LOWER(c.lastName) = LOWER(:lastName)")
    List<Customer> searchByLastNameCaseInsensitive(@Param("lastName") String lastName);

    // --- @Query native query example ---

    @Query(
            value = "SELECT * FROM customer c WHERE c.email LIKE :domain",
            nativeQuery = true
    )
    List<Customer> findCustomersByEmailDomainNative(@Param("domain") String domain);
}
