package webProject.togetherPartyTonight.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import webProject.togetherPartyTonight.domain.member.entity.Member;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MemberInfoResponseDto {

    private String email;

    private String nickname;

    private Float rateAvg;

    private int reviewCount;

    private String profileImage;

    private LocalDateTime createTime;

    private LocalDateTime modifiedTime;
    public static MemberInfoResponseDto from(Member member){
        return new MemberInfoResponseDto(member.getEmail(),member.getNickname(),member.getRatingAvg(),member.getReviewCount(),member.getProfileImage(),member.getCreatedDate(),member.getModifiedDate());
    }
}
