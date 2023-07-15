package webProject.togetherPartyTonight.domain.member.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@ApiModel("자기소개 변경 데이터")
@AllArgsConstructor
public class MemberDetailsModifyDto {

    private String details;
}
