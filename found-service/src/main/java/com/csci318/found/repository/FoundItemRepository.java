package com.csci318.found.repository;

import com.csci318.found.model.FoundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoundItemRepository extends JpaRepository<FoundItem, Long> {
    List<FoundItem> findByFinderId(Long finderId);
    List<FoundItem> findByItemNameContainingIgnoreCase(String name);
}
