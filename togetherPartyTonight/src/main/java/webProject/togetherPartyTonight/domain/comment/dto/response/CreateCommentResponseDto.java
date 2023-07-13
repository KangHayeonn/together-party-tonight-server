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
public class CreateCommentResponseDto {
    private String type;
    private String method;
    private Long commentId;
    private String comment;
    private Long memberId;
    private String nickName;
    private String createdDate;
    private String modifiedDate;

    public CreateCommentResponseDto toDto (Comment comment) {
        return CreateCommentResponseDto.builder()
                .type("comment")
                .method("CREATE")
                .commentId(comment.getId())
                .memberId(comment.getMember().getId())
                .nickName(comment.getMember().getNickname())
                .comment(comment.getCommentContent())
                .createdDate(String.valueOf(comment.getCreatedDate()))
                .modifiedDate(String.valueOf(comment.getModifiedDate()))
                .build();
    }

    public String toSocketMessage () {
        return new Gson().toJson(this, CreateCommentResponseDto.class);
    }
}
