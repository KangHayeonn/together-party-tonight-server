package webProject.togetherPartyTonight.global.common;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webProject.togetherPartyTonight.global.common.response.CommonResponse;

@Getter
@Setter
@NoArgsConstructor
public class SingleResponse<T> extends CommonResponse {
    private T data;
}
