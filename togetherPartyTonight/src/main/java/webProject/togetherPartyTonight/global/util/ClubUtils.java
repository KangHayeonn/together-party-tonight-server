package webProject.togetherPartyTonight.global.util;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClubUtils {
    public List<String> splitTags (String tags) {
        String[] split = tags.split(",");
        return Arrays.stream(split).collect(Collectors.toList());
    }
}
