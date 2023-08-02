package webProject.togetherPartyTonight.domain.club.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ClubCategory {
    운동 ("exercise","운동"),
    여행 ("travel","여행"),
    친목("play","친목"),
    봉사("volunteer","봉사"),
    스터디 ("study","스터디"),
    맛집("food","맛집"),
    취미("hobby","취미");

    private String categoryEng;
    private String categoryKor;

    @Override
    public String toString() {
        return categoryEng;
    }
}
