package webProject.togetherPartyTonight.domain.member.auth;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import webProject.togetherPartyTonight.domain.member.entity.Member;

import java.util.Collection;


@RequiredArgsConstructor
@ToString
public class MemberDetails implements UserDetails {//UserDetails를 Member에 바로 구현보다는 래퍼패턴을 이용해서 책임분리

    private final Member member;

    public Member getMember(){
        return member;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return String.valueOf(member.getId());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
