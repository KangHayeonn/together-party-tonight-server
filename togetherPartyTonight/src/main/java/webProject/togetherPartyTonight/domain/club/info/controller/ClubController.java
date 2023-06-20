package webProject.togetherPartyTonight.domain.club.info.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import webProject.togetherPartyTonight.domain.club.info.dto.*;
import webProject.togetherPartyTonight.domain.club.info.exception.ClubException;
import webProject.togetherPartyTonight.domain.club.info.service.ClubService;
import webProject.togetherPartyTonight.global.common.CommonResponse;
import webProject.togetherPartyTonight.global.common.ErrorResponse;
import webProject.togetherPartyTonight.global.common.ResponseWithData;
import webProject.togetherPartyTonight.global.error.ErrorCode;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs")
@Slf4j
@Api(tags = {"/clubs"})
public class ClubController {
    private final ClubService clubService;
    private final String SUCCESS = "true";
    private final String FAIL = "false";


    /**
     * 모임 만들기 api
     */
    @PostMapping("")
    @ApiOperation(value = "모임 만들기")
    public ResponseEntity<CommonResponse> addClub (@RequestBody @Valid AddClubRequest clubRequest, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        clubService.addClub(clubRequest);
        CommonResponse response = new CommonResponse(SUCCESS, HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 모임 상세 조회 api
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "모임 상세 조회", response = ClubDetailResponse.class)
    public ResponseEntity<ResponseWithData> getClub(@PathVariable Long id, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        ClubDetailResponse responseDto = clubService.getClub(id);
        ResponseWithData response = new ResponseWithData(SUCCESS,HttpStatus.OK.value(),responseDto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**
     * 모임 삭제 api
     */
    @DeleteMapping("")
    @ApiOperation(value = "모임 삭제")
    public ResponseEntity<CommonResponse> deleteClub(@RequestBody @Valid  DeleteAndSignupRequestDto requestDto, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        clubService.deleteClub(requestDto);
        CommonResponse response = new CommonResponse(SUCCESS, HttpStatus.OK.value());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**
     * 모임 변경 api
     */
    @PutMapping("")
    @ApiOperation(value = "모임 변경")
    public  ResponseEntity<CommonResponse> modifyClub (@RequestBody @Valid ModifyClubRequest modifyRequest, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        clubService.modifyClub(modifyRequest);
        CommonResponse response = new CommonResponse(SUCCESS, HttpStatus.OK.value());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**
     * 모임 신청 api
     */
    @PostMapping("/signup")
    @ApiOperation(value = "모임 신청")
    public  ResponseEntity<CommonResponse> signup(@RequestBody @Valid DeleteAndSignupRequestDto requestDto, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        clubService.signup(requestDto);
        CommonResponse response = new CommonResponse(SUCCESS, HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 모임 신청 응답(수락/거절) api
     */
    @PostMapping("/approve")
    @ApiOperation(value = "모임 신청 응답(수락/거절)")
    public  ResponseEntity<CommonResponse> approve(@RequestBody @Valid ApproveRequest requestDto, HttpServletRequest request){
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        clubService.approve(requestDto);
        CommonResponse response = new CommonResponse(SUCCESS, HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /**
     * 모임별 신청 받은 내역 조회 api
     */
    @GetMapping("/applicationList")
    @ApiOperation(value = "모임별 신청 받은 내역 조회")
    public  ResponseEntity<ResponseWithData> getRequestList(@RequestParam("userId") Long userId, @RequestParam("clubId") Long clubId, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        ReceivedApplicationList requestListPerClub = clubService.getRequestListPerClub(userId, clubId);
        ResponseWithData response = new ResponseWithData(SUCCESS, HttpStatus.OK.value(),requestListPerClub);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 사용자별 신청한 모임 조회 api
     */
    @GetMapping("/myApplied")
    @ApiOperation(value = "사용자별 신청한 모임 조회")
    public  ResponseEntity<ResponseWithData> getMyAppliedList(@RequestParam("userId") Long userId, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        MyAppliedClubList appliedList = clubService.getAppliedList(userId);
        ResponseWithData response = new ResponseWithData(SUCCESS, HttpStatus.OK.value(),appliedList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 사용자별 생성한 모임 조회 api
     */
    @GetMapping("/myOwned")
    @ApiOperation(value = "사용자별 생성한 모임 조회")
    public  ResponseEntity<ResponseWithData> getMyOwnedClub(@RequestParam("userId") Long userId, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        MyOwnedClubList ownedList = clubService.getOwnedList(userId);
        ResponseWithData response = new ResponseWithData(SUCCESS, HttpStatus.OK.value(),ownedList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 모임 가입 신청 취소 api
     */
    @DeleteMapping("/myApplied")
    @ApiOperation(value = "모임 가입 신청 취소")
    public  ResponseEntity<CommonResponse> deleteAppliedClub(@RequestBody @Valid DeleteMyAppliedRequest requestDto, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        clubService.deleteAppliedClub(requestDto);
        CommonResponse response = new CommonResponse(SUCCESS, HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 강퇴하기 api
     */
    @PostMapping("/kickout")
    @ApiOperation(value = "강퇴하기")
    public ResponseEntity<CommonResponse> kickoutMember (@RequestBody @Valid DeleteMyAppliedRequest deleteMyAppliedRequest, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        clubService.kickout(deleteMyAppliedRequest);
        CommonResponse response = new CommonResponse(SUCCESS, HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
