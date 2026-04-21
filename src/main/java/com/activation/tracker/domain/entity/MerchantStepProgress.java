package com.activation.tracker.domain.entity;

import com.activation.tracker.domain.enums.StepStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "merchant_step_progress")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantStepProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long merchantId;

    private Long stepId;

    @Enumerated(EnumType.STRING)
    private StepStatus status;

    private LocalDateTime updatedAt;
}


