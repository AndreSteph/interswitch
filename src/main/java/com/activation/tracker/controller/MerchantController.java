package com.activation.tracker.controller;

import com.activation.tracker.domain.entity.Merchant;
import com.activation.tracker.domain.entity.MerchantNote;
import com.activation.tracker.domain.entity.MerchantStepProgress;
import com.activation.tracker.domain.entity.OnboardingStep;
import com.activation.tracker.domain.enums.MerchantStatus;
import com.activation.tracker.dto.CreateMerchantRequest;
import com.activation.tracker.service.MerchantService;
import com.activation.tracker.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/merchants")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService service;
    private final NoteService noteService;

    @PostMapping
    public Merchant create(@Valid @RequestBody CreateMerchantRequest request) {
        return service.createMerchant(request.getName());
    }

    @GetMapping
    public Page<Merchant> getAll(
            @RequestParam(required = false) MerchantStatus status,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.getMerchants(status, name, page, size);
    }

    @GetMapping("/{id}/steps")
    public List<MerchantStepProgress> getSteps(@PathVariable Long id) {
        return service.getSteps(id);
    }

    @GetMapping("/{id}/next-step")
    public OnboardingStep nextStep(@PathVariable Long id) {
        return service.getNextStep(id);
    }

    @PostMapping("/{merchantId}/steps/{stepId}/complete")
    public void completeStep(@PathVariable Long merchantId,
                             @PathVariable Long stepId) {
        service.completeStep(merchantId, stepId);
    }

    // Add Note (Intervention)
    @PostMapping("/{id}/notes")
    public MerchantNote addNote(@PathVariable Long id,
                                @RequestBody Map<String, String> body) {

        return noteService.addNote(
                id,
                body.get("note"),
                body.getOrDefault("type", "NOTE")
        );
    }

    // Get Notes
    @GetMapping("/{id}/notes")
    public List<MerchantNote> getNotes(@PathVariable Long id) {
        return noteService.getNotes(id);
    }
}