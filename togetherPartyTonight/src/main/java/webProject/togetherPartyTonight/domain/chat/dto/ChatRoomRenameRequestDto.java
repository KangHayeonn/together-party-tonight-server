package webProject.togetherPartyTonight.domain.chat.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@ApiModel("채팅방 이름 변경 요청")
public class ChatRoomRenameRequestDto {
    @ApiModelProperty(notes = "채팅방 id")
    Long chatRoomId;
    @ApiModelProperty(notes = "변경할 채팅방 이름")
    String chatRoomName;
}