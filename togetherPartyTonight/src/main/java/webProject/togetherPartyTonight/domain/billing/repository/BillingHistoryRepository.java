package webProject.togetherPartyTonight.domain.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import webProject.togetherPartyTonight.domain.billing.entity.BillingHistory;

import java.util.List;
import java.util.Optional;

public interface BillingHistoryRepository extends JpaRepository<BillingHistory, Long> {
    Optional<List<BillingHistory>> findByBillingId(Long billingId);
    Optional<BillingHistory> findByClubMemberClubMemberId(Long clubMemberId);
}
