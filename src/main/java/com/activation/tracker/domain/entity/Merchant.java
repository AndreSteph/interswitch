package com.activation.tracker.domain.entity;

import com.activation.tracker.domain.enums.MerchantStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "merchants")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private MerchantStatus status;

    private String type;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdatedAt;
}


