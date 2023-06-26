package webProject.togetherPartyTonight.global.common.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ListResponse<T> extends CommonResponse {
    List<T> dataList;
}
