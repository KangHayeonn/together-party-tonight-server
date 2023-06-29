package webProject.togetherPartyTonight.domain.chat.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@ApiModel("방 생성 응답")
@Getter
@Setter
@Builder
public class CreateChatRoomResponseDto {
    @ApiModelProperty (notes = "생성된 방의 id", example = "1")
    Long chatRoomId;
}
