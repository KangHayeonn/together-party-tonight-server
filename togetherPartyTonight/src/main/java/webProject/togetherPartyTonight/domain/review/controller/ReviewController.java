package webProject.togetherPartyTonight.domain.review.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import webProject.togetherPartyTonight.domain.review.dto.request.CreateReviewRequestDto;
import webProject.togetherPartyTonight.domain.review.dto.request.UpdateReviewRequestDto;
import webProject.togetherPartyTonight.domain.review.dto.response.GetReviewDetailResponseDto;
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
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;
    private final ResponseService responseService;

    @PostMapping("")
    @ApiOperation(value = "리뷰 만들기")
    public CommonResponse addReview (@RequestPart(name = "data") @Valid CreateReviewRequestDto createReviewRequestDto,
                                                     @RequestPart(required = false)MultipartFile image, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        reviewService.addReview(createReviewRequestDto, image);
        return responseService.getCommonResponse();
    }

    @GetMapping("/{reviewId}")
    @ApiOperation(value = "리뷰 상세 조회")
    public SingleResponse<GetReviewDetailResponseDto> getReviewDetail (@PathVariable Long reviewId, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        GetReviewDetailResponseDto reviewDetail = reviewService.getReviewDetail(reviewId);
        return responseService.getSingleResponse(reviewDetail);
    }

    @PutMapping("")
    @ApiOperation(value = "리뷰 수정하기")
    public CommonResponse modifyReview (@RequestPart(name = "data") @Valid UpdateReviewRequestDto updateReviewRequestDto, HttpServletRequest request,
                                                        @RequestPart(required = false) MultipartFile image) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        reviewService.modifyReview(updateReviewRequestDto, image);
        return responseService.getCommonResponse();
    }

    @DeleteMapping("/{reviewId}")
    @ApiOperation(value = "리뷰 삭제하기")
    public CommonResponse deleteReview (@PathVariable Long reviewId, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        reviewService.deleteReview(reviewId);
        return responseService.getCommonResponse();
    }
}
