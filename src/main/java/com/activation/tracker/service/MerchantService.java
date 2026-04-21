package com.activation.tracker.service;

import com.activation.tracker.domain.entity.Merchant;
import com.activation.tracker.domain.entity.MerchantStepProgress;
import com.activation.tracker.domain.entity.OnboardingStep;
import com.activation.tracker.domain.enums.MerchantStatus;
import com.activation.tracker.domain.enums.StepStatus;
import com.activation.tracker.domain.specification.MerchantSpecification;
import com.activation.tracker.exception.ResourceNotFoundException;
import com.activation.tracker.repository.MerchantRepository;
import com.activation.tracker.repository.MerchantStepProgressRepository;
import com.activation.tracker.repository.OnboardingStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepo;
    private final MerchantStepProgressRepository progressRepo;
    private final OnboardingStepRepository stepRepo;

    public Merchant createMerchant(String name) {
        Merchant merchant = new Merchant();
        merchant.setName(name);
        merchant.setStatus(MerchantStatus.NOT_STARTED);
        merchant.setCreatedAt(LocalDateTime.now());
        merchant.setLastUpdatedAt(LocalDateTime.now());

        Merchant saved = merchantRepo.save(merchant);

        // initialize steps
        List<OnboardingStep> steps = stepRepo.findAll();
        steps.forEach(step -> {
            MerchantStepProgress p = new MerchantStepProgress();
            p.setMerchantId(saved.getId());
            p.setStepId(step.getId());
            p.setStatus(StepStatus.PENDING);
            p.setUpdatedAt(LocalDateTime.now());
            progressRepo.save(p);
        });

        return saved;
    }

    public Page<Merchant> getMerchants(MerchantStatus status, String name, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Specification<Merchant> spec = Specification
                .where(MerchantSpecification.hasStatus(status))
                .and(MerchantSpecification.nameContains(name));

        return merchantRepo.findAll(spec, pageable);
    }

    public List<MerchantStepProgress> getSteps(Long merchantId) {
        return progressRepo.findByMerchantId(merchantId);
    }

    public void completeStep(Long merchantId, Long stepId) {

        MerchantStepProgress progress = progressRepo.findByMerchantId(merchantId)
                .stream()
                .filter(p -> p.getStepId().equals(stepId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Step not found"));

        progress.setStatus(StepStatus.COMPLETED);
        progress.setUpdatedAt(LocalDateTime.now());
        progressRepo.save(progress);

        updateStatus(merchantId);
    }

    public OnboardingStep getNextStep(Long merchantId) {
        List<MerchantStepProgress> steps = progressRepo.findByMerchantId(merchantId);

        return steps.stream()
                .filter(s -> s.getStatus() != StepStatus.COMPLETED)
                .findFirst()
                .map(s -> stepRepo.findById(s.getStepId()).orElse(null))
                .orElse(null);
    }

    private void updateStatus(Long merchantId) {

        List<MerchantStepProgress> steps = progressRepo.findByMerchantId(merchantId);
        Merchant merchant = merchantRepo.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found"));

        long completed = steps.stream()
                .filter(s -> s.getStatus() == StepStatus.COMPLETED)
                .count();

        if (completed == 0) {
            merchant.setStatus(MerchantStatus.NOT_STARTED);
        } else if (completed == steps.size()) {
            merchant.setStatus(MerchantStatus.ACTIVATED);
        } else {
            merchant.setStatus(MerchantStatus.IN_PROGRESS);
        }

        boolean stuck = steps.stream()
                .anyMatch(s -> s.getUpdatedAt().isBefore(LocalDateTime.now().minusHours(48)));

        if (stuck && merchant.getStatus() != MerchantStatus.ACTIVATED) {
            merchant.setStatus(MerchantStatus.STUCK);
        }

        merchant.setLastUpdatedAt(LocalDateTime.now());
        merchantRepo.save(merchant);
    }
}