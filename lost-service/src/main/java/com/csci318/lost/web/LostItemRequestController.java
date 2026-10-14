package com.csci318.lost.web;

import com.csci318.lost.domain.LostItemRequest;
import com.csci318.lost.service.LostItemRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lost-items")
public class LostItemRequestController {

    private final LostItemRequestService service;

    public LostItemRequestController(LostItemRequestService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LostItemResponse> create(@RequestBody CreateLostItemRequest payload) {
        LostItemRequest created = service.create(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(LostItemResponse.from(created));
    }

    @GetMapping
    public List<LostItemResponse> list(
            @RequestParam(value = "owner", required = false) String owner,
            @RequestParam(value = "q", required = false) String search) {
        return service.list(owner, search).stream().map(LostItemResponse::from).toList();
    }

    @GetMapping("/{id}")
    public LostItemResponse get(@PathVariable Long id) {
        return LostItemResponse.from(service.get(id));
    }

    @PutMapping("/{id}")
    public LostItemResponse update(@PathVariable Long id, @RequestBody UpdateLostItemRequest payload) {
        return LostItemResponse.from(service.update(id, payload));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
