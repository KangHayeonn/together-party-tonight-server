package webProject.togetherPartyTonight.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.search.dto.PageDto;
import webProject.togetherPartyTonight.domain.search.dto.SearchListDto;
import webProject.togetherPartyTonight.domain.search.dto.SearchResponseDto;
import webProject.togetherPartyTonight.domain.search.exception.SearchException;
import webProject.togetherPartyTonight.domain.search.repository.SearchRepository;

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

    public ArrayList searchByAddress(Float lat, Float lng, Pageable pageable) {
        String pointWKT = makePointWKT(lat, lng);
        Optional<Page<Club>> clubList = searchRepository.findByAddress(pointWKT, pageable);
        return makeResponseDto(clubList);
    }

    public ArrayList searchByConditions(Float lat, Float lng, Integer distance, String category, String state, Integer userNum, String tags, String sortFilter, Pageable pageable) {
        String pointWKT = makePointWKT(lat, lng);
        String regexpTag = makeRegexp(tags);
        if (category.equals("전체")) category = convertCategory();

        Optional<Page<Club>> clubList = null;

        if (sortFilter.equals("latest")) {
            clubList = searchRepository.findByConditionsOrderByDate(pointWKT, distance * 1000, state, category, userNum, regexpTag, pageable);
        }
        else if (sortFilter.equals("popular")) {
            clubList = searchRepository.findByConditionsOrderByReviewScore(pointWKT,distance*1000, state, category,userNum,regexpTag, pageable);
        }
       return makeResponseDto(clubList);

    }

    public List<String> splitTags (String tags) {
        String[] split = tags.split(",");
        return Arrays.stream(split).collect(Collectors.toList());
    }

    public List<SearchResponseDto> makeSearchResponseDto(Page<Club> clubs) {
        List<SearchResponseDto> list = clubs.stream()
                .map(c -> new SearchResponseDto(c.getClubId(), c.getClubName(), String.valueOf(c.getClubCategory()), splitTags(c.getClubTags()),
                        c.getClubContent(), c.getClubMaximum(), (float) c.getClubPoint().getX(), (float) c.getClubPoint().getY(), c.getAddress(),
                        LocalDateTime.now(),
                        c.getImage(), c.getClubState(),c.getClubMembers().size(),
                        c.getMaster().getId(), c.getMaster().getNickname(), c.getMaster().getRatingAvg(), c.getMaster().getReviewCount(),
                        c.getCreatedDate(), c.getModifiedDate()))
                .collect(Collectors.toList());

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

    public ArrayList makeResponseDto (Optional<Page<Club>> clubList) {

        PageDto pageDto = new PageDto(clubList.get().getNumberOfElements(), clubList.get().getTotalElements());

        SearchListDto searchListDto = new SearchListDto();
        if (clubList.isPresent()) searchListDto.setClubList(makeSearchResponseDto(clubList.get()));
        else searchListDto.setClubList(new ArrayList<>());

        ArrayList<Object> list = new ArrayList<>();
        list.add(pageDto);
        list.add(searchListDto);
        return list;
    }




}
