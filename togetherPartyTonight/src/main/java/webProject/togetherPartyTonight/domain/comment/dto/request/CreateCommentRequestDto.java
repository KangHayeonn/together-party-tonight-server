package webProject.togetherPartyTonight.domain.comment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "댓글 작성에 필요한 model")
public class CreateCommentRequestDto {
    @NotNull
    @Size(max = 100, message = "댓글은 100자를 넘길 수 없습니다.")
    @ApiModelProperty(name = "댓글 내용", example = "댓글 예시입니다")
    private String commentContent;

    @NotNull
    @ApiModelProperty(name = "모임 글 id", example = "1")
    private Long clubId;
}
