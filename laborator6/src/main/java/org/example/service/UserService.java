package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserPayload;
import org.example.dto.UserResponse;
import org.example.exception.ResourceConflictException;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public void create(UserPayload p) {
        if (repository.existsByEmail(p.getEmail())) {
            throw new ResourceConflictException("A user with this email already exists");
        }
        User u = mapper.toEntity(p);
        repository.insert(u);
    }

    public void update(long id, UserPayload p) {
        User existing = repository.findById(id);

        if (!existing.getEmail().equalsIgnoreCase(p.getEmail()) && repository.existsByEmail(p.getEmail())) {
            throw new ResourceConflictException("A user with this email already exists");
        }

        existing = mapper.toEntity(existing, p);
        repository.update(existing);
    }

    public UserResponse get(long id) {
        return mapper.toResponse(repository.findById(id));
    }

    public List<UserResponse> list() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    public void delete(long id) {
        repository.delete(id);
    }
}
