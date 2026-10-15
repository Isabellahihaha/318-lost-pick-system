package com.lostandfound.claimrecovery.repository;

import com.lostandfound.claimrecovery.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByUserId(Long userId);
    List<Claim> findByLostItemId(Long lostItemId);
    List<Claim> findByFoundItemId(Long foundItemId);
}