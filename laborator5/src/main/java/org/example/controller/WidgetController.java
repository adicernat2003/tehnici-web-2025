package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.CreateWidgetRequest;
import org.example.dto.WidgetResponse;
import org.example.service.WidgetService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/widgets")
public class WidgetController {

    private final WidgetService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<WidgetResponse> getById(@PathVariable(value = "id") Long id) {
        return service.findById(id)
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WidgetResponse> create(@RequestBody @Valid CreateWidgetRequest body) {
        WidgetResponse created = service.create(body);
        return ResponseEntity.ok()
                .header("X-Created-By", "WidgetController")
                .body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@PathVariable(value = "id") Long id,
                                       @RequestBody @Valid CreateWidgetRequest request) {
        boolean updated = service.update(id, request);
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().header("X-Updated", "true").build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean deleted = service.delete(id);
        return !deleted
                ? ResponseEntity.notFound().build()
                : ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<WidgetResponse>> list(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(value = "size", defaultValue = "2") Integer size) {
        List<WidgetResponse> items = service.list(page, size);
        HttpHeaders h = new HttpHeaders();
        h.add("X-Page", String.valueOf(page));
        h.add("X-Size", String.valueOf(size));
        h.add("X-Total-Count", String.valueOf(items.size()));
        return new ResponseEntity<>(items, h, HttpStatus.OK);
    }
}