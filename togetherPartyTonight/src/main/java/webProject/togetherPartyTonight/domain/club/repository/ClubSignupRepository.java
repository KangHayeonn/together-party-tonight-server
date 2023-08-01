package webProject.togetherPartyTonight.domain.club.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.club.entity.ApprovalState;
import webProject.togetherPartyTonight.domain.club.entity.ClubSignup;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubSignupRepository extends JpaRepository<ClubSignup,Long> {
    Optional<Page<ClubSignup>> findAllByClubMemberIdAndClubSignupApprovalStateLike (Long clubSignupMemberId, ApprovalState filter, Pageable pageable );
    Optional<Page<ClubSignup>> findAllByClubMemberId (Long clubSignupMemberId, Pageable pageable );

    @Query(nativeQuery = true, value =
            "SELECT a.* FROM club_signup a LEFT JOIN billing b ON a.club_id=b.club_id " +
            "WHERE a.club_signup_member_id=:clubSignupMemberId AND b.club_id IS NULL ")
    Optional<Page<ClubSignup>> findByClubMemberIdAndBilling (@Param(value = "clubSignupMemberId") Long clubSignupMemberId, Pageable pageable );

    @Query(nativeQuery = true, value =
            "SELECT pre.club_signup_id, pre.club_id, pre.club_master_id, pre.club_signup_member_id, pre.club_signup_date,"+
            "pre.club_signup_approval_date, pre.club_signup_approval_state, pre.created_date, pre.modified_date "+
            "FROM " +
                    "(SELECT b.billing_id as billing_id,a.* FROM club_signup a JOIN billing b ON a.club_id=b.club_id "+
                            "WHERE a.club_signup_member_id=:clubSignupMemberId) pre "+
            "JOIN billing_history c ON pre.billing_id = c.billing_id "+
            "WHERE c.billing_state=:filter ")
    Optional<Page<ClubSignup>> findByClubMemberIdAndBillingWait (@Param(value = "clubSignupMemberId")Long clubSignupMemberId, @Param(value = "filter") String filter, Pageable pageable );


    @Query(nativeQuery = true, value = "SELECT * FROM club_signup WHERE club_id = :clubId AND " +
            "(club_signup_approval_state LIKE 'APPROVE' OR club_signup_approval_state LIKE 'PENDING') " +
            "")
    List<ClubSignup> findAllByClubClubId (@Param("clubId") Long clubId);

    void deleteByClubClubId (Long clubId);

    Optional<ClubSignup> findByClubClubIdAndClubMemberId (Long clubId, Long memberId);

}
