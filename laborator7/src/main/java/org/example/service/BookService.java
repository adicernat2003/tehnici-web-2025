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

    private final BookRepository repo;

    public List<Book> getAll() {
        return repo.findAll();
    }

    public Optional<Book> getById(Long id) {
        return repo.findById(id);
    }

    public Book create(Book b) {
        return repo.create(b.getTitle(), b.getAuthor(), b.getPriceEur());
    }

    public boolean updatePrice(Long id, BigDecimal price) {
        return repo.updatePrice(id, price) == 1;
    }

    public boolean delete(Long id) {
        return repo.deleteById(id) == 1;
    }

    public List<String> getTitles() {
        return repo.findAllTitles();
    }

    public List<Book> searchByAuthor(String q) {
        return repo.searchByAuthorContains(q);
    }

    public Map<String, Long> countByAuthor() {
        return repo.countByAuthor();
    }

    @Transactional
    public void batchCreate(List<Book> books) {
        repo.batchInsert(books);
    }
}