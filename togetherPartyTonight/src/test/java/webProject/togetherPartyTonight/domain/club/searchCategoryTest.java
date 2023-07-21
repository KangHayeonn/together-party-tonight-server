package webProject.togetherPartyTonight.domain.club;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import webProject.togetherPartyTonight.domain.search.dto.SearchListDto;
import webProject.togetherPartyTonight.domain.search.dto.SearchResponseDto;
import webProject.togetherPartyTonight.domain.search.service.SearchService;

import java.util.List;

@SpringBootTest
@Slf4j
public class searchCategoryTest {

    @Autowired
    SearchService searchService;

    @Test
    public void severalCategoryTest() {
        Pageable pageable = PageRequest.of(0,20);
        SearchListDto searchListDto = searchService.searchByConditions(37.5585021972656F, 126.939506530762F, 100, "맛집,운동", "ALL", 20, null, "popular", pageable);
        List<SearchResponseDto> clubList = searchListDto.getClubList();
        for (SearchResponseDto dto : clubList) {
            log.info("clubId : {}", dto.getClubId());
        }
    }

    @Test
    public void totalElementTest() {
        Pageable pageable = PageRequest.of(1,7);
        SearchListDto conditions = searchService.searchByConditions(37.5585021972656F, 126.939506530762F, 100, "모두", "ALL", 20, null, "popular", pageable);
        log.info("totalCount : {}",conditions.getTotalCount());
        log.info("count : {}",conditions.getCount());
    }
}
