package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Book;
import org.example.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;

    public List<Book> getAll() {
        return repository.findAll();
    }

    public Optional<Book> getById(Long id) {
        return repository.findById(id);
    }

    public Book create(Book b) {
        return repository.create(b.getTitle(), b.getAuthor(), b.getPriceEur());
    }

    public boolean updatePrice(Long id, BigDecimal price) {
        return repository.updatePrice(id, price) == 1;
    }

    public boolean delete(Long id) {
        return repository.deleteById(id) == 1;
    }

    public List<String> getDistinctTitles() {
        return repository.findDistinctTitles();
    }

    public List<Book> searchByAuthor(String authorFragment) {
        return repository.searchByAuthorContains(authorFragment);
    }

    public Map<String, Long> countByAuthor() {
        return repository.countByAuthor();
    }

    public long countAll() {
        return repository.count();
    }

    @Transactional
    public void batchCreate(List<Book> books) {
        repository.batchInsert(books);
    }
}