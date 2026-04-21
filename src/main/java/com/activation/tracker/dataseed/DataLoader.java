package com.activation.tracker.dataseed;

import com.activation.tracker.domain.entity.Merchant;
import com.activation.tracker.domain.entity.MerchantNote;
import com.activation.tracker.domain.entity.MerchantStepProgress;
import com.activation.tracker.domain.entity.OnboardingStep;
import com.activation.tracker.domain.enums.MerchantStatus;
import com.activation.tracker.domain.enums.NoteType;
import com.activation.tracker.domain.enums.StepStatus;
import com.activation.tracker.repository.MerchantNoteRepository;
import com.activation.tracker.repository.MerchantRepository;
import com.activation.tracker.repository.MerchantStepProgressRepository;
import com.activation.tracker.repository.OnboardingStepRepository;
import com.activation.tracker.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final OnboardingStepRepository stepRepo;
    private final MerchantRepository merchantRepo;
    private final MerchantStepProgressRepository progressRepo;
    private final MerchantNoteRepository noteRepo;

    @Override
    public void run(String... args) {

        if (stepRepo.count() > 0) return;

        /** ---------------------------
        // 1. Create Steps
        // --------------------------- **/
        List<OnboardingStep> steps = stepRepo.saveAll(List.of(
                createStep("Business Info", 1),
                createStep("Payment Setup", 2),
                createStep("First Transaction", 3),
                createStep("Staff Setup", 4)
        ));

        LocalDateTime now = LocalDateTime.now();

        /** ---------------------------
        // 2. NOT_STARTED
        // --------------------------- **/
        Merchant notStarted = createMerchant("Fresh Shop", MerchantStatus.NOT_STARTED, now, now);
        initAllSteps(notStarted, steps, StepStatus.PENDING, now);

        /** ---------------------------
        // 3. IN_PROGRESS
        // --------------------------- **/
        Merchant inProgress = createMerchant("Growing Store", MerchantStatus.IN_PROGRESS, now, now);

        for (int i = 0; i < steps.size(); i++) {
            StepStatus status = (i < 2) ? StepStatus.COMPLETED : StepStatus.PENDING;
            saveProgress(inProgress.getId(), steps.get(i).getId(), status, now.minusHours(2));
        }

        /** ---------------------------
        // 4. ACTIVATED
        // --------------------------- **/
        Merchant activated = createMerchant("Tech Bazaar", MerchantStatus.ACTIVATED, now, now);
        initAllSteps(activated, steps, StepStatus.COMPLETED, now.minusHours(1));

        /** ---------------------------
        // 5. STUCK
        // --------------------------- **/
        Merchant stuck = createMerchant("Urban Mart", MerchantStatus.STUCK, now, now.minusDays(3));

        for (int i = 0; i < steps.size(); i++) {
            StepStatus status = (i == 0) ? StepStatus.COMPLETED : StepStatus.PENDING;
            saveProgress(stuck.getId(), steps.get(i).getId(), status, now.minusDays(3));
        }

        /** ---------------------------
        // 6. STUCK + INTERVENTION
        // --------------------------- **/
        Merchant stuckWithNotes = createMerchant("City Electronics", MerchantStatus.STUCK, now, now.minusDays(2));

        for (int i = 0; i < steps.size(); i++) {
            StepStatus status = (i == 0) ? StepStatus.COMPLETED : StepStatus.PENDING;
            saveProgress(stuckWithNotes.getId(), steps.get(i).getId(), status, now.minusDays(2));
        }

        saveNote(stuckWithNotes.getId(), "Called merchant, no response", "CONTACTED", now.minusDays(1));
        saveNote(stuckWithNotes.getId(), "Escalated to onboarding team", "ESCALATED", now.minusHours(10));

        /** ---------------------------
        // 7. RECENTLY ACTIVATED
        // --------------------------- **/
        Merchant recent = createMerchant("Quick Shop", MerchantStatus.ACTIVATED, now, now.minusMinutes(30));
        initAllSteps(recent, steps, StepStatus.COMPLETED, now.minusMinutes(30));

        System.out.println(" Seed data loaded: Full lifecycle scenarios ready");
    }

    /** =====================================================
    // 🔧 HELPER METHODS (THIS IS THE REAL UPGRADE)
    // =====================================================**/

    private OnboardingStep createStep(String name, int order) {
        OnboardingStep step = new OnboardingStep();
        step.setName(name);
        step.setStepOrder(order);
        return step;
    }

    private Merchant createMerchant(String name, MerchantStatus status,
                                    LocalDateTime createdAt, LocalDateTime updatedAt) {

        Merchant m = new Merchant();
        m.setName(name);
        m.setStatus(status);
        m.setCreatedAt(createdAt);
        m.setLastUpdatedAt(updatedAt);

        // 👉 if you have "type" field, set it here
        // m.setType("DEFAULT");

        return merchantRepo.save(m);
    }

    private void initAllSteps(Merchant merchant, List<OnboardingStep> steps,
                              StepStatus status, LocalDateTime time) {

        steps.forEach(step ->
                saveProgress(merchant.getId(), step.getId(), status, time)
        );
    }

    private void saveProgress(Long merchantId, Long stepId,
                              StepStatus status, LocalDateTime time) {

        MerchantStepProgress progress = new MerchantStepProgress();
        progress.setMerchantId(merchantId);
        progress.setStepId(stepId);
        progress.setStatus(status);
        progress.setUpdatedAt(time);

        progressRepo.save(progress);
    }

    private void saveNote(Long merchantId, String noteText,
                          String type, LocalDateTime time) {

        MerchantNote note = new MerchantNote();
        note.setMerchantId(merchantId);
        note.setNote(noteText);
        note.setType(NoteType.valueOf(type));
        note.setCreatedAt(time);

        noteRepo.save(note);
    }
}