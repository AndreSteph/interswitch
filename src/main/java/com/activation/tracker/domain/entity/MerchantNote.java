package com.activation.tracker.domain.entity;

import com.activation.tracker.domain.enums.NoteType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "merchant_notes")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long merchantId;

    @Column(nullable = false)
    private String note;

    @Enumerated(EnumType.STRING)
    private NoteType type;

    private LocalDateTime createdAt;

}


