package webProject.togetherPartyTonight.domain.club.info.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubRequestDto {
    private String name;
    private String clubCategory;
    private Integer maximum;
    private Integer minimum;
    private String clubDetails;
    private String clubTags; //json
    private Point clubPoint; //point
    private String clubState;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


}
