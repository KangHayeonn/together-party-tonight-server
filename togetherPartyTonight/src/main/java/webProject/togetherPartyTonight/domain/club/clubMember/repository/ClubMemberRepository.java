package webProject.togetherPartyTonight.domain.club.clubMember.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.club.clubMember.entity.ClubMember;

@Repository
public interface ClubMemberRepository extends JpaRepository<ClubMember,Long> {
}
