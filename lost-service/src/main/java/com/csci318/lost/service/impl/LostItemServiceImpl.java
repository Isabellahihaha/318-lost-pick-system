package com.csci318.lost.service.impl;

import com.csci318.lost.dto.LostItemRequest;
import com.csci318.lost.dto.LostItemResponse;
import com.csci318.lost.exception.ResourceNotFoundException;
import com.csci318.lost.model.LostItem;
import com.csci318.lost.model.LostItemStatus;
import com.csci318.lost.repository.LostItemRepository;
import com.csci318.lost.service.LostItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LostItemServiceImpl implements LostItemService {

    private final LostItemRepository lostItemRepository;

    public LostItemServiceImpl(LostItemRepository lostItemRepository) {
        this.lostItemRepository = lostItemRepository;
    }

    @Override
    public LostItemResponse createLostItem(LostItemRequest request) {
        if (request.getItemName() == null || request.getItemName().trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty");
        }
        LostItem item = new LostItem();
        item.setUserId(request.getUserId());
        item.setItemName(request.getItemName());
        item.setCategory(request.getCategory());
        item.setDescription(request.getDescription());
        item.setLocation(request.getLocation());
        item.setLostDate(request.getLostDate());
        item.setStatus(request.getStatus() != null ? request.getStatus() : LostItemStatus.OPEN);

        LostItem savedItem = lostItemRepository.save(item);
        return mapToResponse(savedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LostItemResponse> getAllLostItems() {
        return lostItemRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LostItemResponse getLostItemById(Long id) {
        LostItem item = lostItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lost item not found with id: " + id));
        return mapToResponse(item);
    }

    @Override
    public LostItemResponse updateLostItem(Long id, LostItemRequest request) {
        LostItem item = lostItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lost item not found with id: " + id));

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
        if (request.getLostDate() != null) {
            item.setLostDate(request.getLostDate());
        }
        if (request.getStatus() != null) {
            item.setStatus(request.getStatus());
        }
        if (request.getUserId() != null) {
            item.setUserId(request.getUserId());
        }

        LostItem updatedItem = lostItemRepository.save(item);
        return mapToResponse(updatedItem);
    }

    @Override
    public void deleteLostItem(Long id) {
        LostItem item = lostItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lost item not found with id: " + id));
        lostItemRepository.delete(item);
    }

    private LostItemResponse mapToResponse(LostItem item) {
        return new LostItemResponse(
                item.getId(),
                item.getUserId(),
                item.getItemName(),
                item.getCategory(),
                item.getDescription(),
                item.getLocation(),
                item.getLostDate(),
                item.getStatus()
        );
    }
}
