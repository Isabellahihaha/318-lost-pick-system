package com.csci318.found.dto;

import com.csci318.found.model.FoundItemStatus;
import java.time.LocalDate;

public class FoundItemRequest {

    private Long finderId;
    private String itemName;
    private String category;
    private String description;
    private String location;
    private LocalDate foundDate;
    private FoundItemStatus status;

    public FoundItemRequest() {
    }

    public FoundItemRequest(Long finderId, String itemName, String category, String description, String location, LocalDate foundDate, FoundItemStatus status) {
        this.finderId = finderId;
        this.itemName = itemName;
        this.category = category;
        this.description = description;
        this.location = location;
        this.foundDate = foundDate;
        this.status = status;
    }

    public Long getFinderId() {
        return finderId;
    }

    public void setFinderId(Long finderId) {
        this.finderId = finderId;
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

    public LocalDate getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(LocalDate foundDate) {
        this.foundDate = foundDate;
    }

    public FoundItemStatus getStatus() {
        return status;
    }

    public void setStatus(FoundItemStatus status) {
        this.status = status;
    }
}
