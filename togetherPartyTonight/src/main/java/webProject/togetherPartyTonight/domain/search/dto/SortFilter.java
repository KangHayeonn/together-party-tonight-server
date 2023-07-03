package webProject.togetherPartyTonight.domain.search.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SortFilter {
    LATEST ("latest"),
    POPULAR("popular");
    private String sortFilter;
}
