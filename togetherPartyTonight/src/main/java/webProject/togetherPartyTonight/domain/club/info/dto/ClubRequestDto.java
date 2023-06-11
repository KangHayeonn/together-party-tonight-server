package webProject.togetherPartyTonight.domain.club.info.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubRequestDto {
    private Long userId;
    private String clubName;
    private String clubCategory;
    private Integer clubMaximum;
    private Integer clubMinimum;
    private String clubContent;
    private String clubTags;
    private String address;
    private Float latitude; //위도
    private Float longitude; //경도
    private LocalDate meetingDate;


}
