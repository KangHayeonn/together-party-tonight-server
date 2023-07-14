package webProject.togetherPartyTonight.domain.comment.dto.response;

import com.google.gson.Gson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webProject.togetherPartyTonight.domain.comment.entity.Comment;
import webProject.togetherPartyTonight.global.websocket.socketMessage.CommonSocketResponseMessage;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ApiModel("댓글 삭제에 대한 응답")
public class DeleteCommentResponseDto {
    @ApiModelProperty(value = "요청 종류", example = "DELETE")
    private String method;
    @ApiModelProperty(value = "댓글 id", example = "1")
    private Long commentId;

    public DeleteCommentResponseDto toDto (Long commentId) {
        return DeleteCommentResponseDto.builder()
                .method("DELETE")
                .commentId(commentId)
                .build();
    }

    public String toSocketMessage () {
        CommonSocketResponseMessage sendMessage = new CommonSocketResponseMessage();
        sendMessage.setType("comment");
        sendMessage.setData(this);
        return new Gson().toJson(sendMessage, CommonSocketResponseMessage.class);
    }

}
