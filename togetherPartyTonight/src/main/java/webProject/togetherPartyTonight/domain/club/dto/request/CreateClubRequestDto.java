package webProject.togetherPartyTonight.domain.club.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.locationtech.jts.geom.Point;
import org.springframework.format.annotation.DateTimeFormat;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.club.entity.ClubCategory;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.global.common.Enum;


import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClubRequestDto {
    @NotEmpty(message = "clubName은 필수 입력 값입니다.")
    @Size(max = 15, message = "clubName은 최대 15자를 넘을 수 없습니다.")
    private String clubName;

    @NotEmpty (message = "clubCategory는 필수 입력 값입니다.")
    @Size(max = 255 )
    @Enum(enumClass = ClubCategory.class, ignoreCase = true)
    private String clubCategory;

    @NotNull (message = "clubMaximum은 필수 입력 값입니다.")
    @Min(value = 1, message = "최소 1명 이상 이여야 합니다.")
    @Max(value = 20, message = "최대 20명을 넘을 수 없습니다.")
    private Integer clubMaximum;

    @NotEmpty (message = "clubContent는 필수 입력 값입니다.")
    @Size(max = 1000, message = "최대 1000자를 넘을 수 없습니다.")
    private String clubContent;

    @NotEmpty (message = "clubTags는 필수 입력 값입니다.")
    @Size(max = 300, message = "최대 길이 300을 넘을 수 없습니다.")
    private String clubTags;

    @NotEmpty (message = "address는 필수 입력 값입니다.")
    @Size(max = 30, message = "최대 30자를 넘을 수 없습니다.")
    private String address;

    @NotNull (message = "latitude는 필수 입력 값입니다.")
    @Min(value = -90, message = "유효한 위도 범위를 벗어났습니다.")
    @Max(value = 90, message = "유효한 위도 범위를 벗어났습니다.")
    private Float latitude; //위도

    @NotNull (message = "longitude는 필수 입력 값입니다.")
    @Min(value = -180,  message = "유효한 경도 범위를 벗어났습니다.")
    @Max(value = 180, message = "유효한 경도 범위를 벗어났습니다.")
    private Float longitude; //경도

    @NotNull (message = "meetingDate는 필수 입력 값입니다.")
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
//    @Future(message = "모임 날짜가 현재 날짜와 시각보다 미래여야 합니다.")
    private LocalDateTime meetingDate;

    public Club toClub (Member master, Point point, String url) {

        return Club.builder()
                .master(master)
                .clubName(clubName)
                .clubContent(clubContent)
                .clubMaximum(clubMaximum)
                .clubTags(clubTags)
                .address(address)
                .meetingDate(meetingDate)
                .clubPoint(point)
                .clubCategory(ClubCategory.valueOf(clubCategory))
                .clubState(true) //모집중
                .image(url)
                .build();
    }


}
