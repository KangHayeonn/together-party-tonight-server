package webProject.togetherPartyTonight.domain.search.controller;


import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import webProject.togetherPartyTonight.domain.club.entity.ClubCategory;
import webProject.togetherPartyTonight.domain.search.dto.SearchListDto;
import webProject.togetherPartyTonight.domain.search.dto.SearchState;
import webProject.togetherPartyTonight.domain.search.dto.SortFilter;
import webProject.togetherPartyTonight.domain.search.service.SearchService;
import webProject.togetherPartyTonight.global.common.Enum;
import webProject.togetherPartyTonight.global.common.response.ListResponse;
import webProject.togetherPartyTonight.global.common.response.SingleResponse;
import webProject.togetherPartyTonight.global.common.service.ResponseService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
@Slf4j
@Api(tags = {"/search"})
@Validated
public class SearchController {
    private final SearchService searchService;
    private final ResponseService responseService;

    /**
     * 주소/동네 기반 검색  api (기본 반경 5km)
     */
    @GetMapping("/address")
    @ApiOperation(value = "주소/동네 기반 검색", notes = "사용자가 입력한 주소의 위/경도의 반경 5km내의 모임을 검색한다.")
    public SingleResponse<SearchListDto> searchByAddress (@RequestParam @ApiParam(value = "위도", example = "37.55920",required = true, allowableValues = "[-90,90]") @NotNull(message = "위도는 필수 입력 값입니다.") @Min(value = -90, message = "유효한 위도 범위를 벗어났습니다.") @Max(value = 90, message = "유효한 위도 범위를 벗어났습니다.") Float lat,
                                                          @RequestParam @ApiParam(value = "경도", example = "126.942310", required = true, allowableValues = "[-180,180]") @NotNull (message = "경도는 필수 입력 값입니다.") @Min(value = -180, message = "유효한 경도 범위를 벗어났습니다.") @Max(value = 180, message = "유효한 경도 범위를 벗어났습니다.")Float lng,
                                                          Pageable pageable, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        SearchListDto searchListDto = searchService.searchByAddress(lat, lng, pageable);

        return responseService.getSingleResponse(searchListDto);
    }

    /**
     * 옵션 기반 검색 api
     */
    @GetMapping("")
    @ApiOperation(value = "옵션 기반 검색", notes = "사용자가 선택한 여러 옵션에 따라 모임을 검색한다.")
    public SingleResponse<SearchListDto> searchByConditions (@RequestParam @ApiParam(required = true, value = "위도", example = "37.55920", allowableValues = "[-90,90]")  @Min(value = -90, message = "유효한 위도 범위를 벗어났습니다.")  @Max(value = 90, message = "유효한 위도 범위를 벗어났습니다.")Float lat,
                                            @RequestParam @ApiParam(required = true, value = "경도", example = "126.942310", allowableValues = "[-180,180]")  @Min(value = -180, message = "유효한 경도 범위를 벗어났습니다.") @Max(value = 180, message = "유효한 경도 범위를 벗어났습니다.")Float lng,
                                            @RequestParam  @ApiParam(required = true, value = "검색할 거리 반경", example = "5", allowableValues = "[0,10]") @Min (value = 0, message = "검색 가능 최소 거리는 0km입니다.") @Max(value = 10, message = "검색 가능 최대 거리는 10km입니다.") Integer distance,
                                            @RequestParam @ApiParam(required = true, value = "모임 카테고리", example = "취미", allowableValues = "취미,봉사,운동,스터디,맛집,여행,친목,전체") @Enum(enumClass = ClubCategory.class, ignoreCase = true) String category,
                                            @RequestParam @ApiParam(required = true, value = "모임 상태", example = "all", allowableValues = "recruit,all") @Enum(enumClass = SearchState.class, ignoreCase = true, message = "검색할 수 없는 모임 상태입니다.") String status,
                                            @RequestParam @ApiParam(required = true, value = "검색할 모임 최대 인원", example = "7" ,allowableValues = "[1,20]") @Min(value = 1, message = "최대 1명이상의 모임만 검색할 수 있습니다.") @Max(value = 20, message = "최대 20명까지의 모임만 검색할 수 있습니다.") Integer userNum,
                                            @RequestParam @ApiParam(required = false, value = "모임 태그", example = "테니스,다이어트,오운완") String tags,
                                            @RequestParam @ApiParam(required = true, value = "정렬 조건", example = "popular", allowableValues = "popular,latest") @Enum(enumClass = SortFilter.class, ignoreCase = true, message = "허용되지 않은 sorting filter 입니다.") String sortFilter,
                                            Pageable pageable,
                                            HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        SearchListDto searchListDto = searchService.searchByConditions(lat, lng, distance, category, status, userNum, tags, sortFilter, pageable);

        return responseService.getSingleResponse(searchListDto);
    }
}
