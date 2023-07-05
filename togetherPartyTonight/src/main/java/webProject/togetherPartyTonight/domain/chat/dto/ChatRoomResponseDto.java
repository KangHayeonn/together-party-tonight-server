package webProject.togetherPartyTonight.domain.chat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("채팅방 목록 응답")
public class ChatRoomResponseDto {
    @ApiModelProperty(notes = "채팅방 이름", dataType = "String")
    String chatRoomName;
    @ApiModelProperty(notes = "채팅방 아이디", dataType = "Long")
    Long chatRoomId;
}
