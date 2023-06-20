package webProject.togetherPartyTonight.domain.club.info.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.lang.NonNull;
import webProject.togetherPartyTonight.domain.club.info.entity.Club;
import webProject.togetherPartyTonight.domain.member.entity.Member;


import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddClubRequest {

    @NotNull
    private Long userId;

    @NotEmpty
    private String clubName;

    @NotEmpty
    @Size(max = 255)
    private String clubCategory;

    @NotNull
    @Min(1)
    @Max(Integer.MAX_VALUE)
    private Integer clubMaximum;

    @NotNull
    @Min(1)
    @Max(Integer.MAX_VALUE)
    private Integer clubMinimum;

    @NotEmpty
    @Size(max = 1000)
    private String clubContent;

    @NotEmpty
    @Size(max = 300)
    private String clubTags;

    @NotEmpty
    @Size(max = 30)
    private String address;

    @NotNull
    @Min(-90)
    @Max(90)
    private Float latitude; //위도

    @NotNull
    @Min(-180)
    @Max(180)
    private Float longitude; //경도

    @NotEmpty
    private String meetingDate;

    public Club toClub (Member master, Point point) {

        return Club.builder()
                .master(master)
                .clubName(clubName)
                .clubContent(clubContent)
                .clubMaximum(clubMaximum)
                .clubMinimum(clubMinimum)
                .clubTags(clubTags)
                .address(address)
                .meetingDate(LocalDate.parse(meetingDate))
                .clubPoint(point)
                .clubCategory(clubCategory)
                .clubState(true) //모집중
                .build();
    }


}
