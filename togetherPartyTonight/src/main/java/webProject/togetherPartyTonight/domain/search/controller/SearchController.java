package webProject.togetherPartyTonight.domain.search.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import webProject.togetherPartyTonight.domain.club.entity.ClubCategory;
import webProject.togetherPartyTonight.domain.search.dto.SearchListDto;
import webProject.togetherPartyTonight.domain.search.dto.SearchState;
import webProject.togetherPartyTonight.domain.search.dto.SortFilter;
import webProject.togetherPartyTonight.domain.search.service.SearchService;
import webProject.togetherPartyTonight.global.common.Enum;
import webProject.togetherPartyTonight.global.common.ResponseWithData;
import webProject.togetherPartyTonight.global.common.response.CommonResponse;
import webProject.togetherPartyTonight.global.common.response.ListResponse;
import webProject.togetherPartyTonight.global.common.response.SingleResponse;
import webProject.togetherPartyTonight.global.common.service.ResponseService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
     * 주소/동네 기반 검색 조회 api (기본 반경 5km)
     */
    @GetMapping("/address")
    @ApiOperation(value = "주소/동네 기반 검색 조회")
    public ListResponse searchByAddress (@RequestParam @NotNull(message = "위도는 필수 입력 값입니다.")
                                                               @Min(value = -90, message = "유효한 위도 범위를 벗어났습니다.")
                                                               @Max(value = 90, message = "유효한 위도 범위를 벗어났습니다.") Float lat,
                                                           @RequestParam @NotNull (message = "경도는 필수 입력 값입니다.")
                                                           @Min(value = -180, message = "유효한 경도 범위를 벗어났습니다.")
                                                           @Max(value = 180, message = "유효한 경도 범위를 벗어났습니다.")Float lng,
                                                           Pageable pageable,
                                                           HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        ArrayList arrayList = searchService.searchByAddress(lat, lng, pageable);
        ListResponse response = responseService.getListResponse(arrayList);
        return response;
       // return responseService.getSingleResponse(searchListDto);
    }

    /**
     * 전체 조회 api
     */
    @GetMapping("")
    @ApiOperation(value = "전체 조회")
    public ListResponse searchByConditions (@RequestParam @NotNull (message = "위도는 필수 입력 값입니다.") @Min(value = -90, message = "유효한 위도 범위를 벗어났습니다.")  @Max(value = 90, message = "유효한 위도 범위를 벗어났습니다.")Float lat,
                                                             @RequestParam @NotNull (message = "경도는 필수 입력 값입니다.") @Min(value = -90, message = "유효한 경도 범위를 벗어났습니다.") @Max(value = 90, message = "유효한 경도 범위를 벗어났습니다.")Float lng,
                                                             @RequestParam  @Min (value = 0, message = "검색 가능 최소 거리는 0km입니다.") @Max(value = 10, message = "검색 가능 최대 거리는 10km입니다.") Integer distance,
                                                             @RequestParam @Enum(enumClass = ClubCategory.class, ignoreCase = true) String category,
                                                             @RequestParam @Enum(enumClass = SearchState.class, ignoreCase = true, message = "검색할 수 없는 모임 상태입니다.") String status,
                                                             @RequestParam @Min(value = 1, message = "최대 1명이상의 모임만 검색할 수 있습니다.") @Max(value = 20, message = "최대 20명까지의 모임만 검색할 수 있습니다.") Integer userNum,
                                                             @RequestParam String tags,
                                                             @RequestParam @Enum(enumClass = SortFilter.class, ignoreCase = true, message = "허용되지 않은 sorting filter 입니다.") String sortFilter,
                                                             Pageable pageable,
                                                             HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        ArrayList arrayList = searchService.searchByConditions(lat, lng, distance, category, status, userNum, tags, sortFilter, pageable);

        return responseService.getListResponse(arrayList);
    }
}
