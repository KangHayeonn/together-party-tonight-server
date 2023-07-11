package webProject.togetherPartyTonight.domain.member.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
@Data
@ApiModel("이메일 인증을 위한 이메일")
public class EmailAuthRequestDto {

    private String email;
}
