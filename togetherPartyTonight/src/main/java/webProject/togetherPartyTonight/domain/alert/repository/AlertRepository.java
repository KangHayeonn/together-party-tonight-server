package webProject.togetherPartyTonight.domain.alert.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.alert.entity.Alert;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlertRepository extends JpaRepository<Alert,Long> {


    // @Query("SELECT c FROM Chat c WHERE c.chatRoom.id = :roomId AND c.id <= :maxChatId ORDER BY c.id DESC")

    @Query("SELECT a FROM Alert a WHERE a.member.id = :memberId AND a.id <= :lastSeq ORDER BY a.id DESC")
    Optional<List<Alert>> findAlertByMemberAndSeq(Long memberId, Long lastSeq, Pageable pageable);

    @Query("SELECT a FROM Alert a WHERE a.member.id = :memberId AND a.id <= :lastSeq AND a.alert_check_state = :isRead ORDER BY a.id DESC")
    Optional<List<Alert>> findAlertByMemberAndSeqNotRead(Long memberId, Long lastSeq, Pageable pageable, boolean isRead);

    @Query("SELECT a FROM Alert a WHERE a.member.id = :memberId ORDER BY a.id DESC")
    Optional<List<Alert>> findAlertByMember(Long memberId, Pageable pageable);

    @Query("SELECT a FROM Alert a WHERE a.member.id = :memberId AND a.alert_check_state = :isRead ORDER BY a.id DESC")
    Optional<List<Alert>> findAlertByMemberNotRead(Long memberId, Pageable pageable, boolean isRead);



    @Query("SELECT COUNT(a) FROM Alert a WHERE a.member.id = :memberId AND a.alert_check_state = :falseValue ORDER BY a.id DESC")
    Integer countUnReadAlert(Long memberId, boolean falseValue);
}
