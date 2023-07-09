package webProject.togetherPartyTonight.domain.chat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@ApiModel("채팅방")
public class ChatRoomListResponseDto {
    @ApiModelProperty(notes = "채팅방 이름", dataType = "String")
    String chatRoomName;
    @ApiModelProperty(notes = "채팅방 아이디", dataType = "Long")
    Long chatRoomId;
    @ApiModelProperty(notes = "가장 최근 채팅", dataType = "String")
    String latestMessage;
}


