package webProject.togetherPartyTonight.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.search.dto.SearchListDto;
import webProject.togetherPartyTonight.domain.search.dto.SearchResponseDto;
import webProject.togetherPartyTonight.domain.search.exception.SearchException;
import webProject.togetherPartyTonight.domain.search.repository.SearchRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchRepository searchRepository;

    public SearchListDto searchByAddress(Float lat, Float lng) {
        String pointWKT = makePointWKT(lat, lng);
        Optional<List<Club>> clubList = searchRepository.findByAddress(pointWKT);

        return makeResponseDto(clubList);
    }

    public SearchListDto searchByConditions(Float lat, Float lng, Integer distance, String category, String state, Integer userNum, String tags, String sortFilter) {
        String pointWKT = makePointWKT(lat, lng);
        String regexpTag = makeRegexp(tags);
        if (category.equals("전체")) category = convertCategory();

        Optional<List<Club>> clubList = null;

        if (sortFilter.equals("latest")) {
            clubList = searchRepository.findByConditionsOrderByDate(pointWKT, distance * 1000, state, category, userNum, regexpTag);
        }
        else if (sortFilter.equals("popular")) {
            clubList = searchRepository.findByConditionsOrderByReviewScore(pointWKT,distance*1000, state, category,userNum,regexpTag);
        }
       return makeResponseDto(clubList);

    }

    public List<String> splitTags (String tags) {
        String[] split = tags.split(",");
        return Arrays.stream(split).collect(Collectors.toList());
    }

    public List<SearchResponseDto> makeSearchResponseDto(List<Club> clubs) {
        List<SearchResponseDto> res = new ArrayList<>();

        for (Club c : clubs) {
            Point clubPoint = c.getClubPoint();
            List<String> tags = splitTags(c.getClubTags());
            res.add(
                    SearchResponseDto.builder()
                            .clubId(c.getClubId())
                            .clubName(c.getClubName())
                            .clubCategory(String.valueOf(c.getClubCategory()))
                            .clubTags(tags)
                            .clubContent(c.getClubContent())
                            .clubMaximum(c.getClubMaximum())
                            .latitude((float) clubPoint.getX())
                            .longitude((float) clubPoint.getY())
                            .address(c.getAddress())
                            //.meetingDate(c.getMeetingDate())
                            .image(c.getImage())
//                            .isRecruit()
//                            .memberCount()
                            .userId(c.getMaster().getId())
                            .nickName(c.getMaster().getNickname())
                            .ratingAvg(c.getMaster().getRatingAvg())
                            .reviewCnt(c.getMaster().getReviewCount())
                           // .totalCnt()
                            .count(clubs.size())
                            .createdDate(c.getCreatedDate())
                            .modifiedDate(c.getModifiedDate())
                            .build()
            );
        }
        return res;
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

    public SearchListDto makeResponseDto (Optional<List<Club>> clubList) {
        SearchListDto searchListDto = new SearchListDto();

        if (clubList.isPresent()) searchListDto.setClubList(makeSearchResponseDto(clubList.get()));
        else searchListDto.setClubList(new ArrayList<>());

        return searchListDto;
    }


}
