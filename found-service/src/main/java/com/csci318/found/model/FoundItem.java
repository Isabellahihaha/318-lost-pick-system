package com.csci318.found.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "found_items")
public class FoundItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "finder_id", nullable = false)
    private Long finderId;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "category")
    private String category;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "location")
    private String location;

    @Column(name = "found_date")
    private LocalDate foundDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FoundItemStatus status;

    public FoundItem() {
    }

    public FoundItem(Long finderId, String itemName, String category, String description, String location, LocalDate foundDate, FoundItemStatus status) {
        this.finderId = finderId;
        this.itemName = itemName;
        this.category = category;
        this.description = description;
        this.location = location;
        this.foundDate = foundDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
