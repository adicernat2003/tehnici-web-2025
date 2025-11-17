package org.example.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserPayload;
import org.example.exception.ResourceConflictException;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final UserMapper mapper;

    public User create(@Valid UserPayload p) {
        if (repo.existsByEmail(p.getEmail())) {
            throw new ResourceConflictException("A user with this email already exists");
        }
        User u = mapper.toEntity(p);
        return repo.insert(u);
    }

    public User update(@Valid UserPayload p) {
        User existing = repo.findById(p.getId());

        if (!existing.getEmail().equalsIgnoreCase(p.getEmail()) && repo.existsByEmail(p.getEmail())) {
            throw new ResourceConflictException("A user with this email already exists");
        }

        existing = mapper.toEntity(existing, p);
        repo.update(existing);
        return existing;
    }

    public User get(@Min(1) long id) {
        return repo.findById(id);
    }

    public List<User> list() {
        return repo.findAll();
    }

    public void delete(@Min(1) long id) {
        repo.delete(id);
    }
}
