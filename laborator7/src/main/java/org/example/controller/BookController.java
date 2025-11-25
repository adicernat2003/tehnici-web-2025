package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Book;
import org.example.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @GetMapping
    public List<Book> all() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> byId(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book req) {
        return service.create(req);
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<Void> updatePrice(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        var price = new BigDecimal(body.get("priceEur").toString());
        return service.updatePrice(id, price) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/titles")
    public List<String> titles() {
        return service.getTitles();
    }

    @GetMapping("/search")
    public List<Book> search(@RequestParam("author") String authorFragment) {
        return service.searchByAuthor(authorFragment);
    }

    @GetMapping("/count-by-author")
    public Map<String, Long> countByAuthor() {
        return service.countByAuthor();
    }

    @PostMapping("/batch")
    public void batch(@RequestBody List<Book> books) {
        service.batchCreate(books);
    }
}
