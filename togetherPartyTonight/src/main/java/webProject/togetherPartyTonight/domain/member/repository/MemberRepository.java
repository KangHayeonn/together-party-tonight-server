package webProject.togetherPartyTonight.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.entity.OAuthProvider;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByEmail(String email);
    Optional<Member> findMemberByEmailAndOauthProvider(String email, OAuthProvider oauthProvider);
}
