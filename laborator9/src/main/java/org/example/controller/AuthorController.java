package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CreateAuthorRequest;
import org.example.model.Author;
import org.example.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody CreateAuthorRequest request) {
        Author author = authorService.createAuthorWithBooks(
                request.getName(),
                request.getBookTitles(),
                request.isTriggerError()
        );
        return ResponseEntity.ok(author);
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @PostMapping("/{authorId}/risky")
    public ResponseEntity<Map<String, String>> riskyOperation(@PathVariable Long authorId,
                                                              @RequestParam(defaultValue = "false") boolean critical) {
        try {
            authorService.riskyOperation(authorId, critical);
            return ResponseEntity.ok(Map.of("status", "success"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

}
