package webProject.togetherPartyTonight.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import webProject.togetherPartyTonight.domain.member.entity.Member;

@Data
@AllArgsConstructor
public class MemberInfoDto {

    private String email;

    private String nickname;

    private Float rateAvg;

    private int reviewCount;


    public static MemberInfoDto from(Member member){
        return new MemberInfoDto(member.getEmail(),member.getNickname(),member.getRatingAvg(),member.getReviewCount());
    }
}
