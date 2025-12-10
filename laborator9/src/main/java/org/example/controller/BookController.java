package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/{bookId}/title")
    public ResponseEntity<Map<String, Object>> updateBookTitle(@PathVariable(name = "bookId") Long bookId,
                                                               @RequestParam(name = "newTitle") String newTitle) {
        bookService.updateBookTitleInNewTransaction(bookId, newTitle);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "bookId", bookId,
                "newTitle", newTitle
        ));
    }

}
