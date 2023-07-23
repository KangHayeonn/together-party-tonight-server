package webProject.togetherPartyTonight.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel("비밀번호 변경")
public class PasswordChangeDto {


    @NotNull(message = "비밀번호를 입력하세요.")
    private String currentPassword;

    @NotNull
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*])(?=\\S+$).{8,}$", message = "비밀번호는 8자 이상이어야 하며, 영문, 특수문자를 포함해야 합니다.")
    private String newPassword;
}
