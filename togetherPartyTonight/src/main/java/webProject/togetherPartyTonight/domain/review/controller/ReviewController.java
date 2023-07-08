package webProject.togetherPartyTonight.domain.review.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import webProject.togetherPartyTonight.domain.club.exception.ClubException;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.exception.errorCode.MemberErrorCode;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.domain.review.dto.request.CreateReviewRequestDto;
import webProject.togetherPartyTonight.domain.review.dto.request.UpdateReviewRequestDto;
import webProject.togetherPartyTonight.domain.review.dto.response.GetReviewDetailResponseDto;
import webProject.togetherPartyTonight.domain.review.dto.response.ReviewListDto;
import webProject.togetherPartyTonight.domain.review.service.ReviewService;
import webProject.togetherPartyTonight.global.common.response.CommonResponse;
import webProject.togetherPartyTonight.global.common.ResponseWithData;
import webProject.togetherPartyTonight.global.common.response.SingleResponse;
import webProject.togetherPartyTonight.global.common.service.ResponseService;
import webProject.togetherPartyTonight.infra.S3.S3Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
@Api(tags = {"/reviews"})
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;
    private final ResponseService responseService;

    private final MemberRepository memberRepository;

    @PostMapping("")
    @ApiOperation(value = "리뷰 만들기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", value = "JSON 데이터", dataType = "string", paramType = "form",required = true, example = "{\n" +
                    "\"memberId\": 3,\n" +
                    "\t\"clubId\": \"5\",\n" +
                    "\t\"rating\": \"4.5\",\n" +
                    "\t\"reviewContent\": \"리뷰입니다 리뷰입니다\",\n" +
                    "}")
    })
    public CommonResponse addReview (@RequestPart(name = "data") @Valid CreateReviewRequestDto createReviewRequestDto,
                                                     @RequestPart(required = false)MultipartFile image, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();

        reviewService.addReview(createReviewRequestDto, image, member);
        return responseService.getCommonResponse();
    }

    @GetMapping("/{reviewId}")
    @ApiOperation(value = "리뷰 상세 조회")
    public SingleResponse<GetReviewDetailResponseDto> getReviewDetail (@PathVariable @ApiParam(value = "리뷰 id", required = true, example = "1") Long reviewId, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();

        GetReviewDetailResponseDto reviewDetail = reviewService.getReviewDetail(reviewId);
        return responseService.getSingleResponse(reviewDetail);
    }

    @PutMapping("")
    @ApiOperation(value = "리뷰 수정하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", value = "JSON 데이터", dataType = "string", paramType = "form",required = true, example = "{\n" +
                    "\"memberId\": 3,\n" +
                    "\t\"clubId\": \"5\",\n" +
                    "\t\"rating\": \"4.5\",\n" +
                    "\t\"reviewContent\": \"리뷰입니다 리뷰입니다\",\n" +
                    "}")
    })
    public CommonResponse modifyReview (@RequestPart(name = "data") @Valid UpdateReviewRequestDto updateReviewRequestDto, HttpServletRequest request,
                                                        @RequestPart(required = false) MultipartFile image) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();
        reviewService.modifyReview(updateReviewRequestDto, image, member);
        return responseService.getCommonResponse();
    }

    @DeleteMapping("/{reviewId}")
    @ApiOperation(value = "리뷰 삭제하기")
    public CommonResponse deleteReview (@PathVariable @ApiParam(value = "리뷰 id", required = true, example = "1") Long reviewId,
                                        HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();
        reviewService.deleteReview(reviewId, member);
        return responseService.getCommonResponse();
    }

    public Member getMemberBySecurityContextHolder() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        System.out.println("username = " + username);
        return memberRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> {
                    throw new ClubException(MemberErrorCode.MEMBER_NOT_FOUND);
                });
    }
}
