package webProject.togetherPartyTonight.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel("변경하는 회원정보")
public class MemberInfoModifyDto {

    @NotNull(message = "닉네임을 입력하지않았습니다.")
    private String nickname;

    private String profileImage;

    private String memberDetails;
}
