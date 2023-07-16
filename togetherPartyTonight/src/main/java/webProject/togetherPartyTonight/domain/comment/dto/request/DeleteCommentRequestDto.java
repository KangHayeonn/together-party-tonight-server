package webProject.togetherPartyTonight.domain.comment.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "댓글 삭제에 필요한 model")
public class DeleteCommentRequestDto {
    @ApiModelProperty(name = "댓글 id", example = "1")
    private Long commentId;
}
