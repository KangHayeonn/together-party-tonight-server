package webProject.togetherPartyTonight.domain.member.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel("이메일 중복체크를 위한 정보")
public class EmailCheckRequestDto {
    @NotNull(message = "이메일을 입력해주세요")
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$",message = "이메일 형식으로 입력해야합니다.")
    private String email;
}
