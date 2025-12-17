package org.example.mapper;

import org.example.dto.CreateBookRequest;
import org.example.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(CreateBookRequest dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setYear(dto.getYear());
        return book;
    }

}
