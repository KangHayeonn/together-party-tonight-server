package webProject.togetherPartyTonight.domain.member.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("닉네임변경")
public class MemberNicknameModifyDto {
    
    private String nickname;
}
