package webProject.togetherPartyTonight.domain.search.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchListDto {
    List<SearchResponseDto> clubList;
}
