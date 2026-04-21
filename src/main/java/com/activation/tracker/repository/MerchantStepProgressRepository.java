package com.activation.tracker.repository;

import com.activation.tracker.domain.entity.MerchantStepProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantStepProgressRepository extends JpaRepository<MerchantStepProgress, Long> {
    List<MerchantStepProgress> findByMerchantId(Long merchantId);
}
