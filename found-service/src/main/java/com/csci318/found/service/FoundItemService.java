package com.csci318.found.service;

import com.csci318.found.dto.FoundItemRequest;
import com.csci318.found.dto.FoundItemResponse;

import java.util.List;

public interface FoundItemService {
    FoundItemResponse createFoundItem(FoundItemRequest request);
    List<FoundItemResponse> getAllFoundItems();
    FoundItemResponse getFoundItemById(Long id);
    FoundItemResponse updateFoundItem(Long id, FoundItemRequest request);
    void deleteFoundItem(Long id);
}
