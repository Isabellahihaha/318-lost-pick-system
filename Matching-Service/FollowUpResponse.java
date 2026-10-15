package com.lostandfound.aimatching.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowUpResponse {
    private List<String> questions;
    private boolean requiresMoreInfo;
}