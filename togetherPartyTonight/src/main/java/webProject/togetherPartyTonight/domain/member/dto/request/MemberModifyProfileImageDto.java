package webProject.togetherPartyTonight.domain.member.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("변경하는 회원프로필 이미지")
public class MemberModifyProfileImageDto {


    private String profileImage;

}
