package com.lostandfound.aimatching.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExtractedItemAttributes {
    private String itemName;
    private String category;
    private String primaryColor;
    private String brand;
    private String location;
    private String approximateDate;
    private List<String> distinctiveFeatures;
    private String serialNumber;
}