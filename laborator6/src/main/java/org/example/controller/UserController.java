package org.example.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserPayload;
import org.example.model.User;
import org.example.service.UserService;
import org.example.validation.Groups;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<User> create(@Validated(Groups.Create.class) @RequestBody UserPayload p) {
        return new ResponseEntity<>(service.create(p), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable(name = "id") @Min(1) long id,
                       @Validated(Groups.Update.class) @RequestBody UserPayload p) {
        p.setId(id);
        return service.update(p);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable(name = "id") @Min(1) long id) {
        return service.get(id);
    }

    @GetMapping
    public List<User> list() {
        return service.list();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") @Min(1) long id) {
        service.delete(id);
    }
}
