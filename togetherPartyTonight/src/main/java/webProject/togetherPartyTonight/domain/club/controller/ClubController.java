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
import webProject.togetherPartyTonight.domain.club.dto.response.GetClubResponseDto;
import webProject.togetherPartyTonight.domain.club.dto.response.MyAppliedClubListDto;
import webProject.togetherPartyTonight.domain.club.dto.response.MyOwnedClubListDto;
import webProject.togetherPartyTonight.domain.club.dto.response.ReceivedApplicationListDto;
import webProject.togetherPartyTonight.domain.club.service.ClubJoinService;
import webProject.togetherPartyTonight.domain.club.service.ClubService;
import webProject.togetherPartyTonight.global.common.ResponseWithData;
import webProject.togetherPartyTonight.global.common.response.CommonResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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


    /**
     * 모임 만들기 api
     */
    @PostMapping("")
    @ApiOperation(value = "모임 만들기")
    public ResponseEntity<CommonResponse> addClub (@RequestPart(name = "data") @Valid CreateClubRequestDto clubRequest,
                                                   @RequestPart(name = "image", required = false) MultipartFile multipartFile, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        clubService.addClub(clubRequest, multipartFile);
        CommonResponse response = new CommonResponse(SUCCESS, HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 모임 상세 조회 api
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "모임 상세 조회", response = GetClubResponseDto.class)
    public ResponseEntity<ResponseWithData> getClub(@PathVariable Long id, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        GetClubResponseDto responseDto = clubService.getClub(id);
        ResponseWithData response = new ResponseWithData(SUCCESS,HttpStatus.OK.value(),responseDto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**
     * 모임 삭제 api
     */
    @DeleteMapping("")
    @ApiOperation(value = "모임 삭제")
    public ResponseEntity<CommonResponse> deleteClub(@RequestBody @Valid DeleteClubAndSignupRequestDto requestDto, HttpServletRequest request) {
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
    public  ResponseEntity<CommonResponse> modifyClub (@RequestPart(name = "data") @Valid UpdateClubRequestDto modifyRequest,
                                                       @RequestPart(name = "image", required = false) MultipartFile multipartFile, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        clubService.modifyClub(modifyRequest, multipartFile);
        CommonResponse response = new CommonResponse(SUCCESS, HttpStatus.OK.value());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**
     * 모임 신청 api
     */
    @PostMapping("/signup")
    @ApiOperation(value = "모임 신청")
    public  ResponseEntity<CommonResponse> signup(@RequestBody @Valid DeleteClubAndSignupRequestDto requestDto, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        clubJoinService.signup(requestDto);
        CommonResponse response = new CommonResponse(SUCCESS, HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 모임 신청 응답(수락/거절) api
     */
    @PostMapping("/approve")
    @ApiOperation(value = "모임 신청 응답(수락/거절)")
    public  ResponseEntity<CommonResponse> approve(@RequestBody @Valid ApproveRequestDto requestDto, HttpServletRequest request){
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        clubJoinService.approve(requestDto);
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
        ReceivedApplicationListDto requestListPerClub = clubJoinService.getRequestListPerClub(userId, clubId);
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
        MyAppliedClubListDto appliedList = clubJoinService.getAppliedList(userId);
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
        MyOwnedClubListDto ownedList = clubJoinService.getOwnedList(userId);
        ResponseWithData response = new ResponseWithData(SUCCESS, HttpStatus.OK.value(),ownedList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 모임 가입 신청 취소 api
     */
    @DeleteMapping("/myApplied")
    @ApiOperation(value = "모임 가입 신청 취소")
    public  ResponseEntity<CommonResponse> deleteAppliedClub(@RequestBody @Valid DeleteMyAppliedRequestDto requestDto, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        clubJoinService.deleteAppliedClub(requestDto);
        CommonResponse response = new CommonResponse(SUCCESS, HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 강퇴하기 api
     */
    @PostMapping("/kickout")
    @ApiOperation(value = "강퇴하기")
    public ResponseEntity<CommonResponse> kickoutMember (@RequestBody @Valid DeleteMyAppliedRequestDto deleteMyAppliedRequestDto, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        clubJoinService.kickout(deleteMyAppliedRequestDto);
        CommonResponse response = new CommonResponse(SUCCESS, HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
