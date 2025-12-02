package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.customer.CustomerRepository;
import org.example.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> searchByLastName(String lastName) {
        return customerRepository.findByLastNameIgnoreCase(lastName);
    }

    public List<Customer> searchByEmailDomainNative(String domain) {
        return customerRepository.findCustomersByEmailDomainNative(domain);
    }
}
