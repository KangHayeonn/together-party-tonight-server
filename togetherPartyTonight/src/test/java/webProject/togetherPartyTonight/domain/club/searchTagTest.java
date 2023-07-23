package webProject.togetherPartyTonight.domain.club;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.search.repository.SearchRepository;
import webProject.togetherPartyTonight.domain.search.service.SearchService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class searchTagTest {

    @Autowired
    SearchService searchService;

    @Autowired
    SearchRepository searchRepository;
    @Test
    public void tagList () {
        String target= "";
        List<String> tags = searchService.findTags(target);
        log.info("정렬 전 태그 리스트");
        for (String s : tags) {
            log.info("{}", s);
        }
    }

    @Test
    public void accuracy() {
        String target ="";
        List<String> tags = searchService.findTags(target);
        List<String> res = searchService.sortBySimilarity(target, tags);
        log.info("정렬 후 태그 리스트:{}",res);
    }

}
