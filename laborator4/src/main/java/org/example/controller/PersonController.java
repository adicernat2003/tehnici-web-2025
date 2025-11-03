package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.PersonRequest;
import org.example.dto.PersonResponse;
import org.example.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/people")
@RequiredArgsConstructor
@Validated
public class PersonController {

    private final PersonService service;

    // GET /api/v1/people
    @GetMapping
    public ResponseEntity<List<PersonResponse>> list() {
        return new ResponseEntity<>(service.list(), HttpStatus.OK);
    }

    // GET /api/v1/people/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    // POST /api/v1/people
    @PostMapping
    public ResponseEntity<PersonResponse> create(@Valid @RequestBody PersonRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    // PUT /api/v1/people/{id} (full replace)
    @PutMapping("/{id}")
    public ResponseEntity<PersonResponse> replace(@PathVariable Long id, @Valid @RequestBody PersonRequest request) {
        return new ResponseEntity<>(service.replace(id, request), HttpStatus.OK);
    }

    // PATCH /api/v1/people/{id} (partial update)
    @PatchMapping("/{id}")
    public ResponseEntity<PersonResponse> patch(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        return new ResponseEntity<>(service.patch(id, fields), HttpStatus.OK);
    }

    // DELETE /api/v1/people/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}