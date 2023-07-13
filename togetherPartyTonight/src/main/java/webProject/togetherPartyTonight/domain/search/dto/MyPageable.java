package webProject.togetherPartyTonight.domain.search.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MyPageable {
    @ApiModelProperty(value = "페이지 번호", required = true, example = "0", allowableValues = "range[0,infinity]")
    private Integer page;

    @ApiModelProperty(value = "페이지 크기" , example = "20")
    private Integer size;
}
