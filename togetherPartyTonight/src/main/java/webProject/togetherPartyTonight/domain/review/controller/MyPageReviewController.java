package webProject.togetherPartyTonight.domain.review.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webProject.togetherPartyTonight.domain.club.exception.ClubException;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.exception.errorCode.MemberErrorCode;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.domain.review.dto.response.ReviewListDto;
import webProject.togetherPartyTonight.domain.review.service.ReviewService;
import webProject.togetherPartyTonight.global.common.response.SingleResponse;
import webProject.togetherPartyTonight.global.common.service.ResponseService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Api(tags = {"/members"})
@Slf4j
public class MyPageReviewController {
    private final ReviewService reviewService;
    private final MemberRepository memberRepository;
    private final ResponseService responseService;

    @GetMapping("/myReviews")
    @ApiOperation(value = "마이페이지에서 내가 쓴 리뷰 조회")
    public SingleResponse<ReviewListDto> getMyReviews (@PageableDefault(size = 5, page = 0, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request) {

        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();
        ReviewListDto review = reviewService.getMyReviews(member, pageable);
        return responseService.getSingleResponse(review);
    }

    @GetMapping("/reviews/{memberId}")
    @ApiOperation(value = "마이페이지에서 사용자별 받은 리뷰 조회")
    public SingleResponse<ReviewListDto> getOthersReviews (@PathVariable @ApiParam(name = "조회할 사용자 id", example = "1", required = true) Long memberId, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        ReviewListDto review = reviewService.getOthersReviews(memberId);
        return responseService.getSingleResponse(review);
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
