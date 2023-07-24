package webProject.togetherPartyTonight.domain.alert.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.alert.entity.Alert;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlertRepository extends JpaRepository<Alert,Long> {

    Optional<List<Alert>> findByMemberIdAndIdLessThanOrderByIdDesc(Long memberId, Long lastSeq, Pageable pageable);

    Optional<List<Alert>> findByMemberIdAndIdLessThanAndAlertCheckStateOrderByIdDesc(Long memberId, Long lastSeq, boolean isRead, Pageable pageable);

    Optional<List<Alert>> findByMemberIdOrderByIdDesc(Long memberId, Pageable pageable);

    Optional<List<Alert>> findByMemberIdAndAlertCheckStateOrderByIdDesc(Long memberId, boolean isRead, Pageable pageable);

    Integer countByMemberIdAndAlertCheckState(Long memberId, boolean isRead);
}
