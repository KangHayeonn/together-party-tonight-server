package webProject.togetherPartyTonight.domain.club.info.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ClubCategory {
    운동 ("운동"),
    여행 ("여행"),
    친목("친목"),
    봉사("봉사"),
    스터디 ("스터디"),
    맛집("맛집"),
    취미("취미");

    private String clubCategory;
}
