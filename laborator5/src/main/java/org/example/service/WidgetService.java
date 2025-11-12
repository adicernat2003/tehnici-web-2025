package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CreateWidgetRequest;
import org.example.dto.WidgetResponse;
import org.example.mapper.WidgetMapper;
import org.example.model.Widget;
import org.example.repository.WidgetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WidgetService {

    private final WidgetRepository repository;
    private final WidgetMapper mapper;

    public Optional<WidgetResponse> findById(Long id) {
        return repository.findById(id).map(mapper::toResponse);
    }

    public List<WidgetResponse> list(Integer page, Integer size) {
        return repository.findAll(page, size).stream().map(mapper::toResponse).toList();
    }

    public WidgetResponse create(CreateWidgetRequest request) {
        Widget entity = mapper.toEntity(request);
        repository.save(entity);
        return mapper.toResponse(entity);
    }

    public boolean update(Long id, CreateWidgetRequest request) {
        return repository.update(id, request.getName(), request.getDescription());
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }
}