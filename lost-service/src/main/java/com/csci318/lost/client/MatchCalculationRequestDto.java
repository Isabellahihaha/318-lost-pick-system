package com.csci318.lost.client;

import java.time.LocalDateTime;

public class MatchCalculationRequestDto {

    private Long lostItemId;
    private Long foundItemId;
    private String lostItemDescription;
    private String foundItemDescription;
    private String lostCategory;
    private String foundCategory;
    private String lostLocation;
    private String foundLocation;
    private Double lostLatitude;
    private Double lostLongitude;
    private Double foundLatitude;
    private Double foundLongitude;
    private LocalDateTime lostDate;
    private LocalDateTime foundDate;

    public Long getLostItemId() {
        return lostItemId;
    }

    public void setLostItemId(Long lostItemId) {
        this.lostItemId = lostItemId;
    }

    public Long getFoundItemId() {
        return foundItemId;
    }

    public void setFoundItemId(Long foundItemId) {
        this.foundItemId = foundItemId;
    }

    public String getLostItemDescription() {
        return lostItemDescription;
    }

    public void setLostItemDescription(String lostItemDescription) {
        this.lostItemDescription = lostItemDescription;
    }

    public String getFoundItemDescription() {
        return foundItemDescription;
    }

    public void setFoundItemDescription(String foundItemDescription) {
        this.foundItemDescription = foundItemDescription;
    }

    public String getLostCategory() {
        return lostCategory;
    }

    public void setLostCategory(String lostCategory) {
        this.lostCategory = lostCategory;
    }

    public String getFoundCategory() {
        return foundCategory;
    }

    public void setFoundCategory(String foundCategory) {
        this.foundCategory = foundCategory;
    }

    public String getLostLocation() {
        return lostLocation;
    }

    public void setLostLocation(String lostLocation) {
        this.lostLocation = lostLocation;
    }

    public String getFoundLocation() {
        return foundLocation;
    }

    public void setFoundLocation(String foundLocation) {
        this.foundLocation = foundLocation;
    }

    public Double getLostLatitude() {
        return lostLatitude;
    }

    public void setLostLatitude(Double lostLatitude) {
        this.lostLatitude = lostLatitude;
    }

    public Double getLostLongitude() {
        return lostLongitude;
    }

    public void setLostLongitude(Double lostLongitude) {
        this.lostLongitude = lostLongitude;
    }

    public Double getFoundLatitude() {
        return foundLatitude;
    }

    public void setFoundLatitude(Double foundLatitude) {
        this.foundLatitude = foundLatitude;
    }

    public Double getFoundLongitude() {
        return foundLongitude;
    }

    public void setFoundLongitude(Double foundLongitude) {
        this.foundLongitude = foundLongitude;
    }

    public LocalDateTime getLostDate() {
        return lostDate;
    }

    public void setLostDate(LocalDateTime lostDate) {
        this.lostDate = lostDate;
    }

    public LocalDateTime getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(LocalDateTime foundDate) {
        this.foundDate = foundDate;
    }
}
