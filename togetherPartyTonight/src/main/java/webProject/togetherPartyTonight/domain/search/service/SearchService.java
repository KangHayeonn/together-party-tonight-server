package webProject.togetherPartyTonight.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.search.dto.SearchListDto;
import webProject.togetherPartyTonight.domain.search.dto.SearchResponseDto;
import webProject.togetherPartyTonight.domain.search.repository.SearchRepository;
import webProject.togetherPartyTonight.global.util.ClubUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchRepository searchRepository;
    private final ClubUtils clubUtils;

    public SearchListDto searchByAddress(Float lat, Float lng, Pageable pageable) {
        String pointWKT = makePointWKT(lat, lng);
        Optional<Page<Club>> clubList = searchRepository.findByAddress(pointWKT, pageable);
        return makeResponseDto(clubList);
    }

    public SearchListDto searchByConditions(Float lat, Float lng, Integer distance, String category, String state, Integer memberNum, String tags, String sortFilter, Pageable pageable) {
        String pointWKT = makePointWKT(lat, lng);
        String regexpTag = makeRegexp(tags);
        if (category.equals("전체")) category = convertCategory();

        Optional<Page<Club>> clubList = null;

        if (sortFilter.equals("latest")) {
            clubList = searchRepository.findByConditionsOrderByDate(pointWKT, distance * 1000, state, category, memberNum, regexpTag, pageable);
        }
        else if (sortFilter.equals("popular")) {
            clubList = searchRepository.findByConditionsOrderByReviewScore(pointWKT,distance*1000, state, category,memberNum,regexpTag, pageable);
        }
       return makeResponseDto(clubList);

    }

    public List<SearchResponseDto> makeSearchResponseDto(Page<Club> clubs) {

        List<SearchResponseDto> list = new ArrayList<>();
        for (Club c : clubs) {
            List<String> tags = clubUtils.splitTags(c.getClubTags());
            Point point= c.getClubPoint();
            list.add(new SearchResponseDto().toDto(c, tags, point));
        }

        return list;
    }

    public String makeRegexp (String tags) {
        return tags.replace(',','|');
    }

    public String convertCategory () {
        return "맛집,취미,봉사,운동,스터디,찬목,여행";
    }

    public String makePointWKT (Float lat, Float lng) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(lat, lng));
        return point.toText();
    }

    public SearchListDto makeResponseDto (Optional<Page<Club>> clubList) {
        SearchListDto searchListDto = new SearchListDto();
        searchListDto.setCount(clubList.get().getNumberOfElements());ㄴ
        searchListDto.setTotalCount(clubList.get().getTotalElements());

        if (clubList.isPresent()) searchListDto.setClubList(makeSearchResponseDto(clubList.get()));
        else searchListDto.setClubList(new ArrayList<>());

        return searchListDto;
    }




}
