package webProject.togetherPartyTonight.domain.search.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "검색 결과 리스트")
public class SearchListDto {

    @ApiModelProperty(value = "모임 리스트")
    List<SearchResponseDto> clubList;

    @ApiModelProperty(value = "현재 페이지의 응답 갯수", example = "20")
    Integer count;

    @ApiModelProperty(value = "검색된 총 응답 갯수", example = "95")
    Long totalCount;

}
