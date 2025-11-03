package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.PersonRequest;
import org.example.dto.PersonResponse;
import org.example.exception.NotFoundException;
import org.example.model.Person;
import org.example.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository repository;

    public List<PersonResponse> list() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public PersonResponse get(Long id) {
        Person p = repository.findById(id).orElseThrow(() -> new NotFoundException("Person %d not found".formatted(id)));
        return toResponse(p);
    }

    public PersonResponse create(PersonRequest req) {
        Person saved = repository.create(Person.builder()
                .name(req.getName())
                .email(req.getEmail())
                .age(req.getAge())
                .build());
        return toResponse(saved);
    }

    public PersonResponse replace(Long id, PersonRequest req) {
        ensureExists(id);
        Person p = Person.builder()
                .id(id)
                .name(req.getName())
                .email(req.getEmail())
                .age(req.getAge())
                .build();
        repository.update(p);
        return toResponse(p);
    }

    public PersonResponse patch(Long id, Map<String, Object> fields) {
        ensureExists(id);
        if (CollectionUtils.isEmpty(fields) || (!fields.containsKey("name") && !fields.containsKey("email") && !fields.containsKey("age"))) {
            return get(id);
        }
        repository.partialUpdate(id, fields);
        return get(id);
    }

    public void delete(Long id) {
        ensureExists(id);
        repository.deleteById(id);
    }

    private void ensureExists(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Person %d not found".formatted(id));
        }
    }

    private PersonResponse toResponse(Person p) {
        return PersonResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .email(p.getEmail())
                .age(p.getAge())
                .build();
    }
}