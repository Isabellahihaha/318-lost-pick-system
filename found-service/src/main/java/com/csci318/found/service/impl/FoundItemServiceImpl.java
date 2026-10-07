package com.csci318.found.service.impl;

import com.csci318.found.dto.FoundItemRequest;
import com.csci318.found.dto.FoundItemResponse;
import com.csci318.found.exception.ResourceNotFoundException;
import com.csci318.found.model.FoundItem;
import com.csci318.found.model.FoundItemStatus;
import com.csci318.found.repository.FoundItemRepository;
import com.csci318.found.service.FoundItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FoundItemServiceImpl implements FoundItemService {

    private final FoundItemRepository foundItemRepository;

    public FoundItemServiceImpl(FoundItemRepository foundItemRepository) {
        this.foundItemRepository = foundItemRepository;
    }

    @Override
    public FoundItemResponse createFoundItem(FoundItemRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request payload cannot be null");
        }
        if (request.getFinderId() == null) {
            throw new IllegalArgumentException("Finder ID is required");
        }
        if (request.getItemName() == null || request.getItemName().trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty");
        }
        if (request.getCategory() == null || request.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        FoundItem item = new FoundItem();
        item.setFinderId(request.getFinderId());
        item.setItemName(request.getItemName());
        item.setCategory(request.getCategory());
        item.setDescription(request.getDescription());
        item.setLocation(request.getLocation());
        item.setFoundDate(request.getFoundDate());
        item.setStatus(request.getStatus() != null ? request.getStatus() : FoundItemStatus.UNCLAIMED);

        FoundItem savedItem = foundItemRepository.save(item);
        return mapToResponse(savedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoundItemResponse> getAllFoundItems() {
        return foundItemRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FoundItemResponse getFoundItemById(Long id) {
        FoundItem item = foundItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Found item not found with id: " + id));
        return mapToResponse(item);
    }

    @Override
    public FoundItemResponse updateFoundItem(Long id, FoundItemRequest request) {
        FoundItem item = foundItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Found item not found with id: " + id));

        if (request.getItemName() != null && !request.getItemName().trim().isEmpty()) {
            item.setItemName(request.getItemName());
        }
        if (request.getCategory() != null) {
            item.setCategory(request.getCategory());
        }
        if (request.getDescription() != null) {
            item.setDescription(request.getDescription());
        }
        if (request.getLocation() != null) {
            item.setLocation(request.getLocation());
        }
        if (request.getFoundDate() != null) {
            item.setFoundDate(request.getFoundDate());
        }
        if (request.getStatus() != null) {
            item.setStatus(request.getStatus());
        }
        if (request.getFinderId() != null) {
            item.setFinderId(request.getFinderId());
        }

        FoundItem updatedItem = foundItemRepository.save(item);
        return mapToResponse(updatedItem);
    }

    @Override
    public void deleteFoundItem(Long id) {
        FoundItem item = foundItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Found item not found with id: " + id));
        foundItemRepository.delete(item);
    }

    private FoundItemResponse mapToResponse(FoundItem item) {
        return new FoundItemResponse(
                item.getId(),
                item.getFinderId(),
                item.getItemName(),
                item.getCategory(),
                item.getDescription(),
                item.getLocation(),
                item.getFoundDate(),
                item.getStatus()
        );
    }
}
