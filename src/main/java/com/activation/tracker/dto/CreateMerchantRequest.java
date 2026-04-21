package com.activation.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateMerchantRequest {

    @NotBlank(message = "Merchant name is required")
    private String name;
}

