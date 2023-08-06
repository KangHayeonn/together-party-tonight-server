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
import webProject.togetherPartyTonight.domain.search.entity.Tag;
import webProject.togetherPartyTonight.domain.search.repository.SearchRepository;
import webProject.togetherPartyTonight.domain.search.repository.TagRepository;
import webProject.togetherPartyTonight.global.util.ClubUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchRepository searchRepository;
    private final ClubUtils clubUtils;

    private final TagRepository tagRepository;

    public SearchListDto searchByAddress(Float lat, Float lng, Pageable pageable) {
        String pointWKT = makePointWKT(lat, lng);
        Optional<Page<Club>> clubList = searchRepository.findByAddress(pointWKT, pageable);
        return makeResponseDto(clubList);
    }

    public SearchListDto searchByConditions(Float lat, Float lng, Integer distance, String category, String state, Integer memberNum, String tags, String sortFilter, Pageable pageable) {
        String pointWKT = makePointWKT(lat, lng);
        String regexpTag;
        if(tags!=null)
            regexpTag = makeRegexp(tags);
        else regexpTag ="";
        
        if (category.equals("모두")) category = convertCategory();

        Optional<Page<Club>> clubList = null;

        if (sortFilter.equals("latest")) {
            clubList = searchRepository.findByConditionsOrderByDate(pointWKT, distance * 1000, state, category, memberNum, regexpTag, pageable);
        }
        else if (sortFilter.equals("popular")) {
            clubList = searchRepository.findByConditionsOrderByReviewScore(pointWKT,distance*1000, state, category,memberNum,regexpTag, pageable);
        }
       return makeResponseDto(clubList);

    }

    public List<String> findTags (String partial) {
        String target = "%"+partial+"%";
        Optional<List<Club>> findByPartial = searchRepository.findByClubTagsLike(target);
        Set<String> res = new HashSet<>();
        for (Club clubs : findByPartial.get()) {
            if (!clubs.getClubTags().contains(",")) {
                res.add(clubs.getClubTags());
            }
            else {
                String[] split = clubs.getClubTags().split(",");
                for (String tag : split) {
                    if (tag.contains(partial)) {
                        res.add(tag);
                    }
                }
            }
        }
        return new ArrayList<>(res);
    }

    public List<String> sortBySimilarity(String target, List<String> tags) {
        /*
        주어진 문자열 리스트를 정확도 순으로 정렬하는 메소드입니다.
        정확도는 주어진 target 문자열과의 유사도에 기반합니다.
        */
        Collections.sort(tags, new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                int score1 = levenshteinDistance(target, str1);
                int score2 = levenshteinDistance(target, str2);
                if (score1==score2) {
                    return str1.length() - str2.length();
                }
                else return score1-score2;
            }
        });

        return tags;
    }


    public Integer levenshteinDistance(String tag, String target) {
          /*
        두 문자열 간의 유사도 점수를 계산하는 메소드입니다.
        레벤슈타인 거리를 이용하여 유사도를 측정합니다.
        */
        int[][] distanceMatrix = new int[target.length() + 1][tag.length() + 1];

        for (int i = 0; i <= target.length(); i++) {
            distanceMatrix[i][0] = i;
        }

        for (int j = 0; j <= tag.length(); j++) {
            distanceMatrix[0][j] = j;
        }

        for (int i = 1; i <= target.length(); i++) {
            for (int j = 1; j <= tag.length(); j++) {
                if (target.charAt(i - 1) == tag.charAt(j - 1)) {
                    distanceMatrix[i][j] = distanceMatrix[i - 1][j - 1];
                } else {
                    int insertion = distanceMatrix[i][j - 1] + 1;
                    //삽입이 필요한경우 위의 수 +1
                    int deletion = distanceMatrix[i - 1][j] + 1;
                    //삭제가 필요한경우 왼쪽 수 +1
                    int substitution = distanceMatrix[i - 1][j - 1] + 1;
                    //삽입이 필요한경우 대각선 수 +1
                    distanceMatrix[i][j] = Math.min(Math.min(insertion, deletion), substitution);
                }
            }
        }

        return distanceMatrix[target.length()][tag.length()];
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
        return "맛집,취미,봉사,운동,스터디,친목,여행";
    }

    public String makePointWKT (Float lat, Float lng) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(lat, lng));
        return point.toText();
    }

    public SearchListDto makeResponseDto (Optional<Page<Club>> clubList) {
        SearchListDto searchListDto = new SearchListDto();
        searchListDto.setCount(clubList.get().getNumberOfElements());
        searchListDto.setTotalCount(clubList.get().getTotalElements());

        if (clubList.isPresent()) searchListDto.setClubList(makeSearchResponseDto(clubList.get()));
        else searchListDto.setClubList(new ArrayList<>());

        return searchListDto;
    }

    public List<String> getPopularTags () {
        List<Tag> tagList = tagRepository.findTop12ByOrderByTagCountDesc();
        List<String> res = new ArrayList<>();
        for (Tag t : tagList) {
            res.add(t.getTagName());
        }
        return res;
    }

}
