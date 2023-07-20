package webProject.togetherPartyTonight.domain.member.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@ApiModel("변경하는 회원프로필 이미지")
@AllArgsConstructor
public class MemberModifyProfileImageDto {


    private String profileImage;

}
