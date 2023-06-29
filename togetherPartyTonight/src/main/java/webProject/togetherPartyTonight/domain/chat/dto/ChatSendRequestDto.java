package webProject.togetherPartyTonight.domain.chat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@ApiModel("채팅 발송 요청")
public class ChatSendRequestDto {
    @ApiModelProperty(notes = "발송자 멤버 id")
    Long senderMemberId;
    @ApiModelProperty(notes = "채팅방 id")
    Long chatRoomId;
    @ApiModelProperty(notes = "채팅 메시지(500 byte 이내)")
    String chatMsg;
}
