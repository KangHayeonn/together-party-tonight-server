package webProject.togetherPartyTonight.domain.search.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SearchState {
    RECRUIT("recruit"),
    ALL("all");
    private String  state;
}
