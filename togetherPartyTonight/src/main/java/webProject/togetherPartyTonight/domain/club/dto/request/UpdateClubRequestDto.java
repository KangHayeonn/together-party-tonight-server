package webProject.togetherPartyTonight.domain.club.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.club.entity.ClubCategory;
import webProject.togetherPartyTonight.domain.club.exception.ClubErrorCode;
import webProject.togetherPartyTonight.domain.club.exception.ClubException;
import webProject.togetherPartyTonight.global.error.ErrorCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateClubRequestDto extends CreateClubRequestDto {

    @NotNull
    private Long clubId;

    public Club toEntity(Club club,Integer flag, Point point, String image) {
        club.setClubName(this.getClubName());
        club.setClubCategory(ClubCategory.valueOf(this.getClubCategory()));
        club.setClubContent(this.getClubContent());
        club.setClubTags(this.getClubTags());
        club.setAddress(this.getAddress());
        club.setMeetingDate(this.getMeetingDate());
        club.setClubPoint(point);
        club.setImage(image);

        if (flag.equals(-1)) {
            throw new ClubException(ClubErrorCode.INVALID_CLUB_MAXIMUM);
        }
        else if (flag.equals(0)) {
            club.setClubMaximum(this.getClubMaximum());
            club.setClubState(false);
        }
        else{
            club.setClubMaximum(this.getClubMaximum());
        }
        return club;
    }
}
