package webProject.togetherPartyTonight.domain.chat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@ApiModel("방 생성 요청")
public class CreateChatRoomRequest {

    //내 멤버 아이디
    @ApiModelProperty(dataType = "long", required = true, notes = "내 멤버 아이디")
    long memberId;

    //상대방 맴버 아이디
    @ApiModelProperty(dataType = "long", required = true, notes = "상대방 맴버 아이디")
    long otherMemberId;
}
