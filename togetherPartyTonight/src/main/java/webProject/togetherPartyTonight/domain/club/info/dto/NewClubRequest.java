package webProject.togetherPartyTonight.domain.club.info.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.time.LocalDateTime;

@Data
@Builder
public class NewClubRequest {
    private String name;
    private String clubCategory;
    private Integer maximum;
    private Integer minimum;
    private String clubDetails;
    private String clubTags; //json
    private String clubPoint;
    private String clubState;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


}
