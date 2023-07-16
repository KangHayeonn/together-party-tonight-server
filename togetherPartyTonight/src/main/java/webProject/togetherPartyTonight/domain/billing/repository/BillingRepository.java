package webProject.togetherPartyTonight.domain.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.billing.entity.Billing;

@Repository
public interface BillingRepository extends JpaRepository<Billing,Long> {
}
