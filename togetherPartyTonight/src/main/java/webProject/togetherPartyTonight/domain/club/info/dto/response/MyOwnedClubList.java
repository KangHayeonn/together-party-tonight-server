package webProject.togetherPartyTonight.domain.club.info.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import webProject.togetherPartyTonight.domain.club.info.dto.response.MyOwnedClub;

import java.util.List;

@Data
@AllArgsConstructor
public class MyOwnedClubList {
    private List<MyOwnedClub> myOwnedList;
}
