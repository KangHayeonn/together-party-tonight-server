package webProject.togetherPartyTonight.domain.club.info.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import webProject.togetherPartyTonight.domain.club.info.dto.request.AddClubRequest;
import webProject.togetherPartyTonight.domain.club.info.entity.Club;
import webProject.togetherPartyTonight.domain.club.info.entity.ClubCategory;
import webProject.togetherPartyTonight.domain.club.info.exception.ClubException;
import webProject.togetherPartyTonight.global.error.ErrorCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyClubRequest extends AddClubRequest {

    @NotNull
    private Long clubId;

    public Club toEntity(Club club,Integer flag, Point point, String image) {
        club.setClubName(this.getClubName());
        club.setClubCategory(ClubCategory.valueOf(this.getClubCategory()));
        club.setClubContent(this.getClubContent());
        club.setClubTags(this.getClubTags());
        club.setAddress(this.getAddress());
        club.setMeetingDate(LocalDate.parse(this.getMeetingDate()));
        club.setClubMinimum(this.getClubMinimum());
        club.setClubPoint(point);
        club.setImage(image);

        if (flag ==-1) {
            throw new ClubException(ErrorCode.INVALID_CLUB_MAXIMUM);
        }
        else if (flag ==0) {
            club.setClubMaximum(this.getClubMaximum());
            club.setClubState(false);
        }
        else{
            club.setClubMaximum(this.getClubMaximum());
        }
        return club;
    }
}
