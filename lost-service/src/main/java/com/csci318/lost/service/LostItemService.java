package com.csci318.lost.service;

import com.csci318.lost.dto.LostItemRequest;
import com.csci318.lost.dto.LostItemResponse;

import java.util.List;

public interface LostItemService {
    LostItemResponse createLostItem(LostItemRequest request);
    List<LostItemResponse> getAllLostItems();
    LostItemResponse getLostItemById(Long id);
    LostItemResponse updateLostItem(Long id, LostItemRequest request);
    void deleteLostItem(Long id);
}
