package com.csci318.lost.controller;

import com.csci318.lost.dto.LostItemRequest;
import com.csci318.lost.dto.LostItemResponse;
import com.csci318.lost.service.LostItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lost-items")
public class LostItemController {

    private final LostItemService lostItemService;

    public LostItemController(LostItemService lostItemService) {
        this.lostItemService = lostItemService;
    }

    @PostMapping
    public ResponseEntity<LostItemResponse> createLostItem(@RequestBody LostItemRequest request) {
        LostItemResponse response = lostItemService.createLostItem(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LostItemResponse>> getAllLostItems() {
        List<LostItemResponse> responses = lostItemService.getAllLostItems();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LostItemResponse> getLostItemById(@PathVariable Long id) {
        LostItemResponse response = lostItemService.getLostItemById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LostItemResponse> updateLostItem(@PathVariable Long id, @RequestBody LostItemRequest request) {
        LostItemResponse response = lostItemService.updateLostItem(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLostItem(@PathVariable Long id) {
        lostItemService.deleteLostItem(id);
        return ResponseEntity.noContent().build();
    }
}
