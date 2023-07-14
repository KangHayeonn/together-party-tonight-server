package webProject.togetherPartyTonight.domain.club.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public enum ClubStateFilterEnum {
    ALL("전체"),
    RECRUITING("모집중"),
    RECRUIT_FINISHED("모집완료");

    private String clubStateFilter;
}
