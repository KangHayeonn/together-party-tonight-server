package webProject.togetherPartyTonight.domain.club.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MyAppliedClubListDto {
    private List<ApplicationDto> myAppliedList ;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
}
