package webProject.togetherPartyTonight.global.common.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SingleResponse<T> extends CommonResponse {
    private T data;
}
