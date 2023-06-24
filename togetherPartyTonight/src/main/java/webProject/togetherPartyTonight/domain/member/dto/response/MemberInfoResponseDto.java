package webProject.togetherPartyTonight.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import webProject.togetherPartyTonight.domain.member.entity.Member;

@Data
@AllArgsConstructor
public class MemberInfoResponseDto {

    private String email;

    private String nickname;

    private Float rateAvg;

    private int reviewCount;


    public static MemberInfoResponseDto from(Member member){
        return new MemberInfoResponseDto(member.getEmail(),member.getNickname(),member.getRatingAvg(),member.getReviewCount());
    }
}
