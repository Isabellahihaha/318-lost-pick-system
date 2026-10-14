package com.csci318.found.service;

import com.csci318.found.domain.FoundItemPost;
import com.csci318.found.repository.FoundItemPostRepository;
import com.csci318.found.web.CreateFoundItemRequest;
import com.csci318.found.web.UpdateFoundItemRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FoundItemPostService {

    private final FoundItemPostRepository repository;

    public FoundItemPostService(FoundItemPostRepository repository) {
        this.repository = repository;
    }

    public FoundItemPost create(CreateFoundItemRequest payload) {
        requireText(payload.getName(), "name");
        requireText(payload.getDescription(), "description");
        requireText(payload.getLocation(), "location");
        requireText(payload.getContactEmail(), "contactEmail");

        FoundItemPost entity = new FoundItemPost(
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

    public List<FoundItemPost> list(String ownerUsername, String search) {
        List<FoundItemPost> items = (ownerUsername == null || ownerUsername.isBlank())
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

    public FoundItemPost get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No found item post with id " + id));
    }

    public FoundItemPost update(Long id, UpdateFoundItemRequest payload) {
        requireText(payload.getName(), "name");
        requireText(payload.getDescription(), "description");
        requireText(payload.getLocation(), "location");
        requireText(payload.getContactEmail(), "contactEmail");

        FoundItemPost entity = get(id);
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
            throw new NoSuchElementException("No found item post with id " + id);
        }
        repository.deleteById(id);
    }

    private boolean matches(FoundItemPost item, String needle) {
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
