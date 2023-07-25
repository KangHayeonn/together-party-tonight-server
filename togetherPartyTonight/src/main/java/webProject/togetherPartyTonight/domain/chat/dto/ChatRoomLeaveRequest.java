package webProject.togetherPartyTonight.domain.chat.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@ApiModel("채팅방 나가기 요청")
public class ChatRoomLeaveRequest {
    @ApiModelProperty(notes = "채팅방 id")
    Long chatRoomId;
}
