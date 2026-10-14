package com.csci318.lost.dto;

import com.csci318.lost.model.LostItemStatus;
import java.time.LocalDate;

public class LostItemResponse {

    private Long id;
    private Long userId;
    private String itemName;
    private String category;
    private String description;
    private String location;
    private LocalDate lostDate;
    private LostItemStatus status;

    public LostItemResponse() {
    }

    public LostItemResponse(Long id, Long userId, String itemName, String category, String description, String location, LocalDate lostDate, LostItemStatus status) {
        this.id = id;
        this.userId = userId;
        this.itemName = itemName;
        this.category = category;
        this.description = description;
        this.location = location;
        this.lostDate = lostDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getLostDate() {
        return lostDate;
    }

    public void setLostDate(LocalDate lostDate) {
        this.lostDate = lostDate;
    }

    public LostItemStatus getStatus() {
        return status;
    }

    public void setStatus(LostItemStatus status) {
        this.status = status;
    }
}
