package com.csci318.lost.service;

import com.csci318.lost.domain.LostItemRequest;
import com.csci318.lost.repository.LostItemRequestRepository;
import com.csci318.lost.web.CreateLostItemRequest;
import com.csci318.lost.web.UpdateLostItemRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LostItemRequestService {

    private final LostItemRequestRepository repository;

    public LostItemRequestService(LostItemRequestRepository repository) {
        this.repository = repository;
    }

    public LostItemRequest create(CreateLostItemRequest payload) {
        requireText(payload.getName(), "name");
        requireText(payload.getDescription(), "description");
        requireText(payload.getLocation(), "location");
        requireText(payload.getContactEmail(), "contactEmail");

        LostItemRequest entity = new LostItemRequest(
                payload.getName(),
                payload.getCategory(),
                payload.getDescription(),
                payload.getLocation(),
                payload.getEventDate(),
                payload.getContactEmail(),
                payload.getOwnerUsername()
        );
        return repository.save(entity);
    }

    public List<LostItemRequest> list(String ownerUsername, String search) {
        List<LostItemRequest> items = (ownerUsername == null || ownerUsername.isBlank())
                ? repository.findAll()
                : repository.findByOwnerUsernameIgnoreCase(ownerUsername);

        if (search == null || search.isBlank()) {
            return items;
        }
        String needle = search.trim().toLowerCase();
        return items.stream()
                .filter(item -> matches(item, needle))
                .toList();
    }

    public LostItemRequest get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No lost item request with id " + id));
    }

    public LostItemRequest update(Long id, UpdateLostItemRequest payload) {
        requireText(payload.getName(), "name");
        requireText(payload.getDescription(), "description");
        requireText(payload.getLocation(), "location");
        requireText(payload.getContactEmail(), "contactEmail");

        LostItemRequest entity = get(id);
        entity.setName(payload.getName());
        entity.setCategory(payload.getCategory());
        entity.setDescription(payload.getDescription());
        entity.setLocation(payload.getLocation());
        entity.setEventDate(payload.getEventDate());
        entity.setContactEmail(payload.getContactEmail());
        entity.setResolved(payload.isResolved());
        return repository.save(entity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("No lost item request with id " + id);
        }
        repository.deleteById(id);
    }

    private boolean matches(LostItemRequest item, String needle) {
        return containsIgnoreCase(item.getName(), needle)
                || containsIgnoreCase(item.getDescription(), needle)
                || containsIgnoreCase(item.getLocation(), needle)
                || containsIgnoreCase(item.getCategory(), needle);
    }

    private boolean containsIgnoreCase(String haystack, String needle) {
        return haystack != null && haystack.toLowerCase().contains(needle);
    }

    private void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
    }
}
