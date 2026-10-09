package com.csci318.lost.repository;

import com.csci318.lost.domain.LostItemRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LostItemRequestRepository extends JpaRepository<LostItemRequest, Long> {

    List<LostItemRequest> findByOwnerUsernameIgnoreCase(String ownerUsername);
}
