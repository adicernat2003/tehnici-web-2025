package org.example.service;

import org.example.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();

    Optional<Product> findById(Long id);

    void create(Product p);

    void update(Long id, Product p);

    void delete(Long id);
}
