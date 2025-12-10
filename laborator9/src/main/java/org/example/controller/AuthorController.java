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
    public void createAuthor(@RequestBody CreateAuthorRequest request) {
        authorService.createAuthorWithBooks(
                request.getName(),
                request.getBookTitles(),
                request.isTriggerError()
        );
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @PostMapping("/{authorId}/risky")
    public ResponseEntity<Map<String, String>> riskyOperation(@PathVariable(name = "authorId") Long authorId,
                                                              @RequestParam(name = "critical", defaultValue = "false") boolean critical) {
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

    //    @Transactional
    @PostMapping("/{authorId}/name")
    public ResponseEntity<Map<String, Object>> updateAuthorNameMandatory(@PathVariable(name = "authorId") Long authorId,
                                                                         @RequestParam(name = "newName") String newName) {
        authorService.updateAuthorNameMandatory(authorId, newName);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "authorId", authorId,
                "newName", newName
        ));
    }

    //    @Transactional
    @GetMapping("/never")
    public ResponseEntity<List<Author>> getAllAuthorsNever() {
        return ResponseEntity.ok(authorService.findAuthorsNever());
    }

}
