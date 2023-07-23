package webProject.togetherPartyTonight.domain.club.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ClubCategory {
    운동 ("exercise"),
    여행 ("travel"),
    친목("play"),
    봉사("volunteer"),
    스터디 ("study"),
    맛집("food"),
    취미("hobby");

    private String clubCategory;

    @Override
    public String toString() {
        return clubCategory;
    }
}
