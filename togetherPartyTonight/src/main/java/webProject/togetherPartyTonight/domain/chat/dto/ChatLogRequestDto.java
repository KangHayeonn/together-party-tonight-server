package webProject.togetherPartyTonight.domain.chat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * 채팅 기록 요청 dto
 * 채팅방 ID,
 * 마지막 seq, (-1 일 경우 첫 입장)
 * 요청갯수
 */
@Getter
@ApiModel("채팅방 기록 요청")
public class ChatLogRequestDto {

    @ApiModelProperty(notes = "요청 채팅방 id")
    long chatRoomId;
    @ApiModelProperty(notes = "마지막으로 가지고있던 채팅 seq, 최초입장 : -1")
    long lastChatSeq;
    @ApiModelProperty(notes = "요청 개수, 100 이하로 가능, 100 초과는 exception")
    int listCount;
}
