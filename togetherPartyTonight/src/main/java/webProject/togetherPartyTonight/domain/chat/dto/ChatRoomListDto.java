package webProject.togetherPartyTonight.domain.chat.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ApiModel("채팅방 목록 리스트 응답")
public class ChatRoomListDto {
    List<ChatRoomListResponseDto> chatRoomList;
}
