package webProject.togetherPartyTonight.domain.chat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@ApiModel("채팅발송 응답")
public class ChatSendResponseDto {
    @ApiModelProperty(notes = "채팅 아이디")
    Long chatId;
}
