package webProject.togetherPartyTonight.global.common;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webProject.togetherPartyTonight.global.common.response.CommonResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ListResponse<T> extends CommonResponse {
    List<T> dataList;
}
