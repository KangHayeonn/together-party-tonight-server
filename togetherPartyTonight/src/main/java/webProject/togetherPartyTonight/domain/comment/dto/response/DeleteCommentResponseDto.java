package webProject.togetherPartyTonight.domain.comment.dto.response;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webProject.togetherPartyTonight.domain.comment.entity.Comment;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DeleteCommentResponseDto {
    private String type;
    private String method;
    private Long commentId;

    public DeleteCommentResponseDto toDto (Long commentId) {
        return DeleteCommentResponseDto.builder()
                .type("comment")
                .method("DELETE")
                .commentId(commentId)
                .build();
    }

    public String toSocketMessage () {
        return new Gson().toJson(this, DeleteCommentResponseDto.class);
    }

}
