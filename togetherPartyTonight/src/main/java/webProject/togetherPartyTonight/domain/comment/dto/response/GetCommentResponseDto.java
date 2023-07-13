package webProject.togetherPartyTonight.domain.comment.dto.response;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webProject.togetherPartyTonight.domain.comment.entity.Comment;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentResponseDto {
    private Long commentId;
    private Long memberId;
    private String nickName;
    private Long clubId;
    private String comment;
    private String  createdDate;
    private String  modifiedDate;

    public GetCommentResponseDto toDto (Comment comment) {
        return GetCommentResponseDto.builder()
                .commentId(comment.getId())
                .memberId(comment.getMember().getId())
                .nickName(comment.getMember().getNickname())
                .clubId(comment.getClub().getClubId())
                .comment(comment.getCommentContent())
                .createdDate(String.valueOf(comment.getCreatedDate()))
                .modifiedDate(String.valueOf(comment.getModifiedDate()))
                .build();
    }


}
