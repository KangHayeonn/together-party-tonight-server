package webProject.togetherPartyTonight.domain.member.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@ApiModel("자기소개 변경 데이터")
@AllArgsConstructor
@NoArgsConstructor
public class MemberDetailsModifyDto {

    @Length(max = 45)
    private String details;
}
