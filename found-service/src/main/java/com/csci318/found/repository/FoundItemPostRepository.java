package com.csci318.found.repository;

import com.csci318.found.domain.FoundItemPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoundItemPostRepository extends JpaRepository<FoundItemPost, Long> {

    List<FoundItemPost> findByOwnerUsernameIgnoreCase(String ownerUsername);
}
