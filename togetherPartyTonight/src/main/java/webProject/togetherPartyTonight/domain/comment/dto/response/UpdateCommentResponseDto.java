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

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ApiModel("댓글 수정에 대한 응답")
public class UpdateCommentResponseDto {
    @ApiModelProperty(value = "요청 종류", example = "UPDATE")
    private String method;
    @ApiModelProperty(value = "댓글 id", example = "1")
    private Long commentId;
    @ApiModelProperty(value = "댓글 내용", example = "댓글 수정 예시입니다.")
    private String comment;
    @ApiModelProperty(value = "댓글 작성자 id", example = "1")
    private Long memberId;
    @ApiModelProperty(value = "댓글 작성자 닉네임", example = "경은")
    private String nickName;
    @ApiModelProperty(value = "댓글 최초 작성일자", example = "2023-07-14T13:58.987658")
    private String createdDate;
    @ApiModelProperty(value = "댓글 수정일자", example = "2023-07-14T13:58.987658")
    private String modifiedDate;

    public UpdateCommentResponseDto toDto (Comment comment) {
        return UpdateCommentResponseDto.builder()
                .method("UPDATE")
                .commentId(comment.getId())
                .memberId(comment.getMember().getId())
                .nickName(comment.getMember().getNickname())
                .comment(comment.getCommentContent())
                .createdDate(String.valueOf(comment.getCreatedDate()))
                .modifiedDate(String.valueOf(comment.getModifiedDate()))
                .build();
    }

    public String toSocketMessage () {
        CommonSocketResponseMessage sendMessage = new CommonSocketResponseMessage();
        sendMessage.setType("comment");
        sendMessage.setData(this);
        return new Gson().toJson(sendMessage, CommonSocketResponseMessage.class);
    }
}
