package webProject.togetherPartyTonight.domain.club.info.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import webProject.togetherPartyTonight.domain.club.info.dto.response.Application;

import java.util.List;

@Data
@AllArgsConstructor
public class ReceivedApplicationList {
    private List<Application> applicationList;
}
