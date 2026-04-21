package com.activation.tracker.repository;

import com.activation.tracker.domain.entity.OnboardingStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnboardingStepRepository extends JpaRepository<OnboardingStep, Long> {}
