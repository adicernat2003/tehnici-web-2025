package org.example.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class InMemoryProductService implements ProductService {

    private final List<Product> store = new ArrayList<>();
    private long idSequence = 0;

    @PostConstruct
    void seed() {
        create(Product.builder().name("Coffee Beans").price(24.99).quantity(10).build());
        create(Product.builder().name("Espresso Machine").price(299.00).quantity(3).build());
        create(Product.builder().name("Mug").price(7.50).quantity(25).build());
        log.info("Seeded {} products", store.size());
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(store);
    }

    @Override
    public void create(Product p) {
        long id = ++idSequence;
        Product newProduct = p.toBuilder().id(id).build();
        store.add(newProduct);
        log.info("Created product id={} name={}", id, newProduct.getName());
    }

    @Override
    public void update(Long id, Product p) {
        Product existing = findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
        int index = store.indexOf(existing);
        Product updated = p.toBuilder().id(id).build();
        store.set(index, updated);
        log.info("Updated product id={}", id);
    }

    @Override
    public void delete(Long id) {
        Product existing = findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
        store.remove(existing);
        log.info("Deleted product id={}", id);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return store.stream().filter(p -> p.getId().equals(id)).findFirst();
    }
}
