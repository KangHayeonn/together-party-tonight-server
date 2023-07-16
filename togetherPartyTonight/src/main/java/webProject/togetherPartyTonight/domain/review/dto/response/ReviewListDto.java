package webProject.togetherPartyTonight.domain.review.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewListDto {
    List<?> reviewList;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
}
