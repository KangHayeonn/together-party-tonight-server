package webProject.togetherPartyTonight.domain.comment.dto.response;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webProject.togetherPartyTonight.domain.comment.entity.Comment;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UpdateCommentResponseDto {
    private String type;
    private String method;
    private Long commentId;
    private String comment;
    private Long memberId;
    private String nickName;
    private String createdDate;
    private String modifiedDate;

    public UpdateCommentResponseDto toDto (Comment comment) {
        return UpdateCommentResponseDto.builder()
                .type("comment")
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
        return new Gson().toJson(this, UpdateCommentResponseDto.class);
    }
}
