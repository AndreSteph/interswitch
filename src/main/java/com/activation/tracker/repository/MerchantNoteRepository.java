package com.activation.tracker.repository;

import com.activation.tracker.domain.entity.MerchantNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantNoteRepository extends JpaRepository<MerchantNote, Long> {

    List<MerchantNote> findByMerchantIdOrderByCreatedAtDesc(Long merchantId);
}
