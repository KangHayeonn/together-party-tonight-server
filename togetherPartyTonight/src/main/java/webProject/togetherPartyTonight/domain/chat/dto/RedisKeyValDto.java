package webProject.togetherPartyTonight.domain.chat.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RedisKeyValDto {
    private String key;
    private String value;
}
