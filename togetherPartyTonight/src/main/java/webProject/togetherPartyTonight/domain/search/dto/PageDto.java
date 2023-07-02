package webProject.togetherPartyTonight.domain.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageDto {
    private Integer count;
    private Long  totalCount;
}
