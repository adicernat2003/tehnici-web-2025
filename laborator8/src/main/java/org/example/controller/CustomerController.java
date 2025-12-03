package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CustomerNameEmail;
import org.example.model.Customer;
import org.example.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<Customer> getAll() {
        return customerService.getAll();
    }

    @PostMapping
    public Customer create(@RequestBody Customer customer) {
        return customerService.create(customer);
    }

    @GetMapping("/search/by-lastname")
    public List<Customer> searchByLastName(@RequestParam(name = "lastName") String lastName) {
        return customerService.searchByLastName(lastName);
    }

    @GetMapping("/search/by-domain")
    public List<Customer> searchByEmailDomain(@RequestParam(name = "domain") String domain) {
        return customerService.searchByEmailDomainNative(domain);
    }

    @GetMapping("/search/by-last-name/dto")
    public List<CustomerNameEmail> searchNameAndEmailByLastName(@RequestParam(name = "lastName") String lastName) {
        return customerService.findNameAndEmailByLastName(lastName);
    }
}