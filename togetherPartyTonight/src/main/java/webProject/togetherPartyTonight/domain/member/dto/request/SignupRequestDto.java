package webProject.togetherPartyTonight.domain.member.dto.request;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import webProject.togetherPartyTonight.domain.member.entity.Member;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel("회원가입을 위한 데이터")
public class SignupRequestDto {

    @NotNull(message = "이메일을 입력해주세요")
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$",message = "이메일 형식으로 입력해야합니다.")
    private String email;

    @NotNull(message = "비밀번호를 입력하세요")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*])(?=\\S+$).{8,}$", message = "비밀번호는 8자 이상이어야 하며, 영문, 특수문자를 포함해야 합니다.")
    private String password;

    @NotNull(message = "닉네임을 입력하세요")
    private String nickname;

    @NotNull(message = "인증번호를 입력하세요")
    private String authCode;

}
