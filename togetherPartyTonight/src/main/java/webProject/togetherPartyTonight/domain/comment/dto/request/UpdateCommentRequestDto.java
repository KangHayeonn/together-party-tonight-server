package webProject.togetherPartyTonight.domain.comment.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "댓글 수정에 필요한 model")
public class UpdateCommentRequestDto {
    @ApiModelProperty(name = "댓글 id", example = "1")
    private Long commentId;
    @ApiModelProperty(name = "수정할 댓글 내용", example = "수정할 댓글입니다.")
    private String commentContent;
}
