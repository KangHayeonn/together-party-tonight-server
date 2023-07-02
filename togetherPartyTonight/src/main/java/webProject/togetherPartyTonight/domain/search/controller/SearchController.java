package webProject.togetherPartyTonight.domain.search.controller;


import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import webProject.togetherPartyTonight.domain.club.entity.ClubCategory;
import webProject.togetherPartyTonight.domain.search.dto.SearchListDto;
import webProject.togetherPartyTonight.domain.search.dto.SearchState;
import webProject.togetherPartyTonight.domain.search.dto.SortFilter;
import webProject.togetherPartyTonight.domain.search.service.SearchService;
import webProject.togetherPartyTonight.global.common.Enum;
import webProject.togetherPartyTonight.global.common.response.SingleResponse;
import webProject.togetherPartyTonight.global.common.service.ResponseService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
@Slf4j
@Api(tags = {"/search"})
@SwaggerDefinition(tags = {@Tag(name = "/search", description = "모임 검색 컨트롤러")})
@Validated
public class SearchController {
    private final SearchService searchService;
    private final ResponseService responseService;
    private final String SUCCESS = "true";

    /**
     * 주소/동네 기반 검색 조회 api (기본 반경 5km)
     */
    @GetMapping("/address")
    @ApiOperation(value = "주소/동네 기반 검색 조회", notes = "사용자가 입력한 주소의 위/경도의 반경 5km내의 모임을 검색한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "채팅 최대 글자 수를 초과하였습니다")
    })
    public SingleResponse<SearchListDto> searchByAddress (@RequestParam @NotNull(message = "위도는 필수 입력 값입니다.") @ApiParam(value = "위도")
                                                               @Min(value = -90, message = "유효한 위도 범위를 벗어났습니다.")
                                                               @Max(value = 90, message = "유효한 위도 범위를 벗어났습니다.") Float lat,
                                                          @RequestParam @NotNull (message = "경도는 필수 입력 값입니다.") @ApiParam(value = "경도")
                                                           @Min(value = -180, message = "유효한 경도 범위를 벗어났습니다.")
                                                           @Max(value = 180, message = "유효한 경도 범위를 벗어났습니다.")Float lng,
                                                          @RequestParam @ApiParam(value = "현재 페이지") Integer page,
                                                          @RequestParam @ApiParam(value = "한 페이지당 보낼 리스트 갯수") Integer perPage,
                                                          HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        SearchListDto searchListDto = searchService.searchByAddress(lat, lng);

        return responseService.getSingleResponse(searchListDto);
    }

    /**
     * 전체 조회 api
     */
    @GetMapping("")
    @ApiOperation(value = "전체 조회", notes = "사용자가 선택한 옵션에 따라 모임을 검색한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "vgvh"),
            @ApiResponse(code = 400, message = "채팅 최대 글자 수를 초과하였습니다")
    })
    public SingleResponse<SearchListDto> searchByConditions (@RequestParam @NotNull (message = "위도는 필수 입력 값입니다.") @Min(value = -90, message = "유효한 위도 범위를 벗어났습니다.") @Max(value = 90, message = "유효한 위도 범위를 벗어났습니다.") @ApiParam(value = "위도")Float lat,
                                                             @RequestParam @NotNull (message = "경도는 필수 입력 값입니다.") @Min(value = -180, message = "유효한 경도 범위를 벗어났습니다.") @Max(value = 180, message = "유효한 경도 범위를 벗어났습니다.") @ApiParam(value = "경도")Float lng,
                                                             @RequestParam  @Min (value = 0, message = "검색 가능 최소 거리는 0km입니다.") @Max(value = 10, message = "검색 가능 최대 거리는 10km입니다.") @ApiParam(value = "검색할 반경")Integer distance,
                                                             @RequestParam  @ApiParam(value = "카테고리") String category,
                                                             @RequestParam @Enum(enumClass = SearchState.class, ignoreCase = true, message = "검색할 수 없는 모임 상태입니다.") @ApiParam(value = "모집 상태")String status,
                                                             @RequestParam @Min(value = 1, message = "최대 1명이상의 모임만 검색할 수 있습니다.") @Max(value = 20, message = "최대 20명까지의 모임만 검색할 수 있습니다.") @ApiParam(value = "모임 인원")Integer userNum,
                                                             @RequestParam @ApiParam(value = "태그")String tags,
                                                             @RequestParam @Enum(enumClass = SortFilter.class, ignoreCase = true, message = "허용되지 않은 sorting filter 입니다.") @ApiParam(value = "정렬 조건")String sortFilter,
                                                             @RequestParam @ApiParam(value = "현재 페이지")Integer page, @RequestParam @ApiParam(value = "한 페이지당 보낼 리스트 갯수") Integer perPage,
                                                             HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        SearchListDto searchListDto = searchService.searchByConditions(lat, lng, distance, category, status, userNum, tags, sortFilter);

        return responseService.getSingleResponse(searchListDto);

    }
}
