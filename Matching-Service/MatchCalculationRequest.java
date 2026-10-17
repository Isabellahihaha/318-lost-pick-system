package com.lostandfound.aimatching.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MatchCalculationRequest {
    @NotNull
    private Long lostItemId;
    @NotNull
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
}
