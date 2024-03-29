package webProject.togetherPartyTonight.domain.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.billing.entity.Billing;

import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    Optional<Billing> findByClubClubId(Long clubId);
}
