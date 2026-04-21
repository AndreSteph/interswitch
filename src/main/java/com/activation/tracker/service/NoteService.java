package com.activation.tracker.service;

import com.activation.tracker.domain.entity.MerchantNote;
import com.activation.tracker.domain.enums.NoteType;
import com.activation.tracker.exception.ResourceNotFoundException;
import com.activation.tracker.repository.MerchantNoteRepository;
import com.activation.tracker.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final MerchantNoteRepository noteRepo;
    private final MerchantRepository merchantRepo;

    public MerchantNote addNote(Long merchantId, String note, String type) {

        // Validate merchant exists
        merchantRepo.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found"));

        MerchantNote newNote = new MerchantNote();
        newNote.setMerchantId(merchantId);
        newNote.setNote(note);
        newNote.setType(NoteType.valueOf(type)); // CONTACTED, ESCALATED, NOTE
        newNote.setCreatedAt(LocalDateTime.now());

        return noteRepo.save(newNote);
    }

    public List<MerchantNote> getNotes(Long merchantId) {

        // Validate merchant exists
        merchantRepo.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found"));

        return noteRepo.findByMerchantIdOrderByCreatedAtDesc(merchantId);
    }
}

