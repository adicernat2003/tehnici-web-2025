package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CreateBookRequest;
import org.example.exception.BookNotFoundException;
import org.example.mapper.BookMapper;
import org.example.model.Book;
import org.example.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;
    private final BookMapper mapper;

    public List<Book> getAll() {
        return repository.findAll();
    }

    public Book getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public Book create(CreateBookRequest book) {
        Book entity = mapper.toEntity(book);
        return repository.save(entity);
    }

    public Book update(Long id, Book updated) {
        Book existing = getById(id);
        existing.setTitle(updated.getTitle());
        existing.setAuthor(updated.getAuthor());
        existing.setYear(updated.getYear());
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
