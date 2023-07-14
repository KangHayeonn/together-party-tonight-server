package webProject.togetherPartyTonight.domain.comment.dto.response;

import com.google.gson.Gson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("댓글 조회에 대한 응답")
public class GetCommentResponseDto {
    @ApiModelProperty(value = "요청 종류", example = "READ")
    private Long commentId;
    @ApiModelProperty(value = "댓글 작성자 id", example = "1")
    private Long memberId;
    @ApiModelProperty(value = "댓글 작성자 닉네임", example = "경은")
    private String nickName;
    @ApiModelProperty(value = "모임 id", example = "1")
    private Long clubId;
    @ApiModelProperty(value = "댓글 내용", example = "댓글 예시입니다.")
    private String comment;
    @ApiModelProperty(value = "댓글 최초 작성일자", example = "2023-07-14T13:58.987658")
    private String  createdDate;
    @ApiModelProperty(value = "댓글 수정일자", example = "2023-07-14T13:58.987658")
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
