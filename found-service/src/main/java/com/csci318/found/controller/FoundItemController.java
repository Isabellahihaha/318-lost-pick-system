package com.csci318.found.controller;

import com.csci318.found.dto.FoundItemRequest;
import com.csci318.found.dto.FoundItemResponse;
import com.csci318.found.service.FoundItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/found-items")
public class FoundItemController {

    private final FoundItemService foundItemService;

    public FoundItemController(FoundItemService foundItemService) {
        this.foundItemService = foundItemService;
    }

    @PostMapping
    public ResponseEntity<FoundItemResponse> createFoundItem(@RequestBody FoundItemRequest request) {
        FoundItemResponse response = foundItemService.createFoundItem(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FoundItemResponse>> getAllFoundItems() {
        List<FoundItemResponse> responses = foundItemService.getAllFoundItems();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoundItemResponse> getFoundItemById(@PathVariable Long id) {
        FoundItemResponse response = foundItemService.getFoundItemById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoundItemResponse> updateFoundItem(@PathVariable Long id, @RequestBody FoundItemRequest request) {
        FoundItemResponse response = foundItemService.updateFoundItem(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoundItem(@PathVariable Long id) {
        foundItemService.deleteFoundItem(id);
        return ResponseEntity.noContent().build();
    }
}
