package webProject.togetherPartyTonight.domain.club.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import webProject.togetherPartyTonight.domain.club.dto.request.*;
import webProject.togetherPartyTonight.domain.club.dto.response.*;
import webProject.togetherPartyTonight.domain.club.service.ClubJoinService;
import webProject.togetherPartyTonight.domain.club.service.ClubService;
import webProject.togetherPartyTonight.global.common.ResponseWithData;
import webProject.togetherPartyTonight.global.common.response.CommonResponse;
import webProject.togetherPartyTonight.global.common.response.ListResponse;
import webProject.togetherPartyTonight.global.common.response.SingleResponse;
import webProject.togetherPartyTonight.global.common.service.ResponseService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs")
@Slf4j
@Api(tags = {"/clubs"})
public class ClubController {
    private final ClubService clubService;
    private final ClubJoinService clubJoinService;
    private final String SUCCESS = "true";
    private final String FAIL = "false";

    private final ResponseService responseService;


    /**
     * 모임 만들기 api
     */
    @PostMapping("")
    @ApiOperation(value = "모임 만들기")
    public CommonResponse addClub (@RequestPart(name = "data") @Valid CreateClubRequestDto clubRequest,
                                                   @RequestPart(name = "image", required = false) MultipartFile multipartFile, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        clubService.addClub(clubRequest, multipartFile);
        return responseService.getCommonResponse();
    }

    /**
     * 모임 상세 조회 api
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "모임 상세 조회", response = GetClubResponseDto.class)
    public SingleResponse<GetClubResponseDto>  getClub(@PathVariable Long id, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        GetClubResponseDto responseDto = clubService.getClub(id);
        return responseService.getSingleResponse(responseDto);
    }

    /**
     * 모임 삭제 api
     */
    @DeleteMapping("")
    @ApiOperation(value = "모임 삭제")
    public CommonResponse deleteClub(@RequestBody @Valid DeleteClubAndSignupRequestDto requestDto, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        clubService.deleteClub(requestDto);
        return responseService.getCommonResponse();
    }

    /**
     * 모임 변경 api
     */
    @PutMapping("")
    @ApiOperation(value = "모임 변경")
    public  CommonResponse modifyClub (@RequestPart(name = "data") @Valid UpdateClubRequestDto modifyRequest,
                                                       @RequestPart(name = "image", required = false) MultipartFile multipartFile, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        clubService.modifyClub(modifyRequest, multipartFile);
        return responseService.getCommonResponse();
    }

    /**
     * 모임 신청 api
     */
    @PostMapping("/signup")
    @ApiOperation(value = "모임 신청")
    public  CommonResponse signup(@RequestBody @Valid DeleteClubAndSignupRequestDto requestDto, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        clubJoinService.signup(requestDto);
        return responseService.getCommonResponse();
    }

    /**
     * 모임 신청 응답(수락/거절) api
     */
    @PostMapping("/approve")
    @ApiOperation(value = "모임 신청 응답(수락/거절)")
    public  CommonResponse approve(@RequestBody @Valid ApproveRequestDto requestDto, HttpServletRequest request){
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        clubJoinService.approve(requestDto);
        return responseService.getCommonResponse();

    }

    /**
     * 모임별 신청 받은 내역 조회 api
     */
    @GetMapping("/applicationList")
    @ApiOperation(value = "모임별 신청 받은 내역 조회")
    public SingleResponse<ReceivedApplicationListDto> getRequestList(@RequestParam("userId") Long userId, @RequestParam("clubId") Long clubId, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        ReceivedApplicationListDto requestListPerClub = clubJoinService.getRequestListPerClub(userId, clubId);
        return responseService.getSingleResponse(requestListPerClub);
    }

    /**
     * 사용자별 신청한 모임 조회 api
     */
    @GetMapping("/myApplied")
    @ApiOperation(value = "사용자별 신청한 모임 조회")
    public  SingleResponse<MyAppliedClubListDto> getMyAppliedList(@RequestParam("userId") Long userId, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        MyAppliedClubListDto appliedList = clubJoinService.getAppliedList(userId);
        return responseService.getSingleResponse(appliedList);
    }

    /**
     * 사용자별 생성한 모임 조회 api
     */
    @GetMapping("/myOwned")
    @ApiOperation(value = "사용자별 생성한 모임 조회")
    public  SingleResponse<MyOwnedClubListDto> getMyOwnedClub(@RequestParam("userId") Long userId, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        MyOwnedClubListDto ownedList = clubJoinService.getOwnedList(userId);
        return responseService.getSingleResponse(ownedList);
    }

    /**
     * 모임 가입 신청 취소 api
     */
    @DeleteMapping("/myApplied")
    @ApiOperation(value = "모임 가입 신청 취소")
    public CommonResponse deleteAppliedClub(@RequestBody @Valid DeleteMyAppliedRequestDto requestDto, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        clubJoinService.deleteAppliedClub(requestDto);
        return responseService.getCommonResponse();
    }

    /**
     * 강퇴하기 api
     */
    @PostMapping("/kickout")
    @ApiOperation(value = "강퇴하기")
    public CommonResponse kickoutMember (@RequestBody @Valid DeleteMyAppliedRequestDto deleteMyAppliedRequestDto, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        clubJoinService.kickout(deleteMyAppliedRequestDto);
        return responseService.getCommonResponse();
    }

}
