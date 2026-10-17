package com.csci318.found.web;

import com.csci318.found.domain.FoundItemPost;
import com.csci318.found.service.FoundItemMatchingService;
import com.csci318.found.service.FoundItemPostService;
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
@RequestMapping("/api/found-items")
public class FoundItemPostController {

    private final FoundItemPostService service;
    private final FoundItemMatchingService matchingService;

    public FoundItemPostController(FoundItemPostService service, FoundItemMatchingService matchingService) {
        this.service = service;
        this.matchingService = matchingService;
    }

    @PostMapping
    public ResponseEntity<FoundItemResponse> create(@RequestBody CreateFoundItemRequest payload) {
        FoundItemPost created = service.create(payload);
        List<MatchCandidateResponse> matches = matchingService.findMatches(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(FoundItemResponse.from(created, matches));
    }

    @GetMapping
    public List<FoundItemResponse> list(
            @RequestParam(value = "owner", required = false) String owner,
            @RequestParam(value = "q", required = false) String search) {
        return service.list(owner, search).stream().map(FoundItemResponse::from).toList();
    }

    @GetMapping("/{id}")
    public FoundItemResponse get(@PathVariable Long id) {
        return FoundItemResponse.from(service.get(id));
    }

    @GetMapping("/{id}/matches")
    public List<MatchCandidateResponse> matches(@PathVariable Long id) {
        FoundItemPost item = service.get(id);
        return matchingService.findMatches(item);
    }

    @PutMapping("/{id}")
    public FoundItemResponse update(@PathVariable Long id, @RequestBody UpdateFoundItemRequest payload) {
        return FoundItemResponse.from(service.update(id, payload));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
