package webProject.togetherPartyTonight.domain.club.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import webProject.togetherPartyTonight.domain.club.dto.request.*;
import webProject.togetherPartyTonight.domain.club.dto.response.*;
import webProject.togetherPartyTonight.domain.club.entity.ApprovalState;
import webProject.togetherPartyTonight.domain.club.entity.ClubCategory;
import webProject.togetherPartyTonight.domain.club.exception.ClubException;
import webProject.togetherPartyTonight.domain.club.service.ClubJoinService;
import webProject.togetherPartyTonight.domain.club.service.ClubService;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.exception.errorCode.MemberErrorCode;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.global.common.Enum;
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
@Validated
public class ClubController {
    private final ClubService clubService;
    private final ClubJoinService clubJoinService;

    private final ResponseService responseService;

    private final MemberRepository memberRepository ;


    /**
     * 모임 만들기 api
     */
    @PostMapping("")
    @ApiOperation(value = "모임 만들기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", value = "JSON 데이터", dataType = "string", paramType = "form",required = true, example = "{\n" +
                    "\t\"clubName\": \"오늘 테니스 같이 치실 분 구해요\",\n" +
                    "\t\"clubCategory\": \"운동\",\n" +
                    "\t\"clubMaximum\" :3,\n" +
                    "\t\"clubContent\" : \"테니스 같이 치실 초보 분 구합니다. 저도 초보에요.\",\n" +
                    "\t\"clubTags\": \"테니스,다이어트,오운완\",\n" +
                    "\t\"latitude\": 37.558503,\n" +
                    "\t\"longitude\": 126.939503,\n" +
                    "\t\"address\": \"서울시 서대문구 창천동 테니스장\",\n"+
                    "\t\"meetingDate\" : \"2023-08-23T12:30\"\n" +
                    "}")
    })
    public CommonResponse addClub (@RequestPart(name = "data") @Valid CreateClubRequestDto clubRequest,
                                   @RequestPart(name = "image", required = false) @ApiParam(value = "첨부 이미지") MultipartFile multipartFile,
                                   HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();
        clubService.addClub(clubRequest, multipartFile, member);
        return responseService.getCommonResponse();
    }

    /**
     * 모임 상세 조회 api
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "모임 상세 조회", response = GetClubResponseDto.class)
    public SingleResponse<GetClubResponseDto>  getClub(@PathVariable @ApiParam(value = "모임 id", required = true, example = "1") Long id,
                                                       HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        GetClubResponseDto responseDto = clubService.getClub(id);
        return responseService.getSingleResponse(responseDto);
    }

    /**
     * 모임 삭제 api
     */
    @DeleteMapping("")
    @ApiOperation(value = "모임 삭제")
    public CommonResponse deleteClub(@RequestBody @Valid @ApiParam(value = "모임 삭제 요청", required = true) DeleteClubAndSignupRequestDto requestDto,
                                     HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();
        clubService.deleteClub(requestDto, member);
        return responseService.getCommonResponse();
    }

    /**
     * 모임 변경 api
     */
    @PutMapping("")
    @ApiOperation(value = "모임 변경")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", value = "JSON 데이터", dataType = "string", paramType = "form",required = true, example = "{\n" +
                    "\"clubId\" : 1,\n" +
                    "\t\"clubName\": \"오늘 테니스 같이 치실 분 구해요\",\n" +
                    "\t\"clubCategory\": \"운동\",\n" +
                    "\t\"clubMaximum\" :3,\n" +
                    "\t\"clubContent\" : \"테니스 같이 치실 초보 분 구합니다. 저도 초보에요.\",\n" +
                    "\t\"clubTags\": \"테니스,다이어트,오운완\",\n" +
                    "\t\"latitude\": 37.558503,\n" +
                    "\t\"longitude\": 126.939503,\n" +
                    "\t\"address\": \"서울시 서대문구 창천동 테니스장\",\n"+
                    "\t\"meetingDate\" : \"2023-08-23T12:30\"\n" +
                    "}")
    })
    public  CommonResponse modifyClub (@RequestPart(name = "data") @Valid UpdateClubRequestDto modifyRequest,
                                       @RequestPart(name = "image", required = false) @ApiParam(value = "첨부 이미지") MultipartFile multipartFile,
                                       HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();
        clubService.modifyClub(modifyRequest, multipartFile, member);
        return responseService.getCommonResponse();
    }

    /**
     * 모임 신청 api
     */
    @PostMapping("/signup")
    @ApiOperation(value = "모임 가입신청")
    public  CommonResponse signup(@RequestBody @Valid @ApiParam(value = "모임 가입신청 요청", required = true) DeleteClubAndSignupRequestDto requestDto,
                                  HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();
        clubJoinService.signup(requestDto, member);
        return responseService.getCommonResponse();
    }

    /**
     * 모임 신청 응답(수락/거절) api
     */
    @PostMapping("/approve")
    @ApiOperation(value = "모임 신청 응답(수락/거절)")
    public  CommonResponse approve(@RequestBody @Valid @ApiParam(value = "모임 신청 응답", required = true) ApproveRequestDto requestDto,
                                   HttpServletRequest request){
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();
        clubJoinService.approve(requestDto, member);
        return responseService.getCommonResponse();

    }

    /**
     * 모임별 신청 받은 내역 조회 api
     */
    @GetMapping("/applicationList")
    @ApiOperation(value = "모임별 신청 받은 내역 조회")
    public SingleResponse<ReceivedApplicationListDto> getRequestList(@RequestParam("clubId")@ApiParam(value = "모임 id", required = true, example = "1") Long clubId,
                                                                     HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();
        ReceivedApplicationListDto requestListPerClub = clubJoinService.getRequestListPerClub( clubId, member);
        return responseService.getSingleResponse(requestListPerClub);
    }

    /**
     * 사용자별 신청한 모임 조회 api
     */
    @GetMapping("/myApplied")
    @ApiOperation(value = "사용자별 신청한 모임 조회")
    public  SingleResponse<MyAppliedClubListDto> getMyAppliedList(HttpServletRequest request,@PageableDefault(size = 5, page = 0, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                                                                  @ApiParam(value = "필터 조건", allowableValues = "ALL,PENDING,APPROVE,KICKOUT,REFUSE", example = "PENDING", required = true)
                                                                  @Enum(enumClass = ApprovalState.class, ignoreCase = true, message = "허용되지 않은 상태입니다.")
                                                                  @RequestParam String filter) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();
        MyAppliedClubListDto appliedList = clubJoinService.getAppliedList( member, pageable, ApprovalState.valueOf(filter));
        return responseService.getSingleResponse(appliedList);
    }

    /**
     * 사용자별 생성한 모임 조회 api
     */
    @GetMapping("/myOwned/{memberId}")
    @ApiOperation(value = "사용자별 생성한 모임 조회")
    public  SingleResponse<MyOwnedClubListDto> getMyOwnedClub(HttpServletRequest request,@PageableDefault(size = 5, page = 0, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                                                              @ApiParam(value = "필터 조건", allowableValues = "ALL,RECRUITING,RECRUIT_FINISHED", example = "ALL", required = true)
                                                              @Enum(enumClass = ClubStateFilterEnum.class, ignoreCase = true)
                                                              @RequestParam String filter, @ApiParam(value = "사용자 id", example = "1") @PathVariable Long memberId ) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        MyOwnedClubListDto ownedList = clubJoinService.getOwnedList( memberId, pageable, ClubStateFilterEnum.valueOf(filter));
        return responseService.getSingleResponse(ownedList);
    }

    /**
     * 모임 가입 신청 취소 api
     */
    @DeleteMapping("/myApplied")
    @ApiOperation(value = "모임 가입 신청 취소")
    public CommonResponse deleteAppliedClub(@RequestBody @Valid @ApiParam(value = "모임 가입 신청 취소", required = true, example = "1")DeleteMyAppliedRequestDto requestDto,
                                            HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();
        clubJoinService.deleteAppliedClub(requestDto, member);
        return responseService.getCommonResponse();
    }

    /**
     * 강퇴하기 api
     */
    @PostMapping("/kickout")
    @ApiOperation(value = "강퇴하기")
    public CommonResponse kickoutMember (@RequestBody @Valid @ApiParam(value = "강퇴하기", required = true)DeleteMyAppliedRequestDto deleteMyAppliedRequestDto,
                                         HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();
        clubJoinService.kickout(deleteMyAppliedRequestDto, member);
        return responseService.getCommonResponse();
    }

    public Member getMemberBySecurityContextHolder() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        return memberRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> {
                    throw new ClubException(MemberErrorCode.MEMBER_NOT_FOUND);
                });
    }

}
