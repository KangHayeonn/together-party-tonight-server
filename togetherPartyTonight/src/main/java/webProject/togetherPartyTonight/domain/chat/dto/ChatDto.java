package webProject.togetherPartyTonight.domain.chat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("채팅")
public class ChatDto {
    @ApiModelProperty(notes = "채팅 id")
    Long chatId;
    @ApiModelProperty(notes = "채팅 내용", dataType = "String")
    String message;
    @ApiModelProperty(notes = "채팅 시간", dataType = "String", example = "2023-07-16T17:31:43.490118")
    String dateTime;
}
