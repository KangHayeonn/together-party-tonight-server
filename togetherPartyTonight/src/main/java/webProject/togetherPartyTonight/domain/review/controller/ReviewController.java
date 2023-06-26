package webProject.togetherPartyTonight.domain.review.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import webProject.togetherPartyTonight.domain.review.dto.request.AddReviewRequest;
import webProject.togetherPartyTonight.domain.review.dto.request.ModifyReviewRequest;
import webProject.togetherPartyTonight.domain.review.dto.response.ReviewDetailResponse;
import webProject.togetherPartyTonight.domain.review.service.ReviewService;
import webProject.togetherPartyTonight.global.common.response.CommonResponse;
import webProject.togetherPartyTonight.global.common.ResponseWithData;
import webProject.togetherPartyTonight.infra.S3.S3Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;
    private final String SUCCESS = "true";

    private final S3Service s3Service;
    @PostMapping("")
    @ApiOperation(value = "리뷰 만들기")
    public ResponseEntity<CommonResponse> addReview (@RequestPart(name = "data") @Valid AddReviewRequest addReviewRequest,
                                                     @RequestPart(required = false)MultipartFile image, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        reviewService.addReview(addReviewRequest, image);
        CommonResponse response = new CommonResponse(SUCCESS, HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{reviewId}")
    @ApiOperation(value = "리뷰 상세 조회")
    public ResponseEntity<ResponseWithData> getReviewDetail (@PathVariable Long reviewId, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        ReviewDetailResponse reviewDetail = reviewService.getReviewDetail(reviewId);
        ResponseWithData response = new ResponseWithData(SUCCESS, HttpStatus.OK.value(), reviewDetail);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("")
    @ApiOperation(value = "리뷰 수정하기")
    public ResponseEntity<CommonResponse> modifyReview (@RequestPart(name = "data") @Valid ModifyReviewRequest modifyReviewRequest, HttpServletRequest request,
                                                        @RequestPart(required = false) MultipartFile image) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        reviewService.modifyReview(modifyReviewRequest, image);
        CommonResponse response = new CommonResponse(SUCCESS, HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    @ApiOperation(value = "리뷰 삭제하기")
    public ResponseEntity<CommonResponse> deleteReview (@PathVariable Long reviewId, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());

        reviewService.deleteReview(reviewId);
        CommonResponse response = new CommonResponse(SUCCESS, HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
