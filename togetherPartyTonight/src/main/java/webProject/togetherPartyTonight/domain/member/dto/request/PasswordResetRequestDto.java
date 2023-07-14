package webProject.togetherPartyTonight.domain.member.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel("비밀번호 찾기 시 비밀번호 변경 데이터")
public class PasswordResetRequestDto {

    @NotNull
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$",message = "이메일 형식으로 입력해야합니다.")
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*])(?=\\S+$).{8,}$", message = "비밀번호는 8자 이상이어야 하며, 영문, 특수문자를 포함해야 합니다.")
    private String newPassword;

    @NotNull
    private String authCode;
}
