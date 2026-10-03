package com.csci318.lost.repository;

import com.csci318.lost.model.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostItemRepository extends JpaRepository<LostItem, Long> {
    List<LostItem> findByUserId(Long userId);
    List<LostItem> findByItemNameContainingIgnoreCase(String name);
}
