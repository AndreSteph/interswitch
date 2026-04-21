package com.activation.tracker.dto;

import com.activation.tracker.domain.enums.MerchantStatus;

public class MerchantResponse {
    private Long id;
    private String name;
    private MerchantStatus status;
    private int completedSteps;
    private int totalSteps;
}

