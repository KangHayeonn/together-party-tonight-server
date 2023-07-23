package webProject.togetherPartyTonight.domain.club;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import webProject.togetherPartyTonight.domain.search.service.SearchService;

import java.util.List;

@SpringBootTest
@Slf4j
public class randomTagTest {

    @Autowired
    SearchService searchService;

    @Test
    public void getRandomTags () {
        List<String> randomTags = searchService.getRandomTags();
        for (int i=0;i<randomTags.size();i++) {
            log.info(i+"번째 태그 : {}",randomTags.get(i));
        }
    }
}
