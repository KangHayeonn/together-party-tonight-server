package webProject.togetherPartyTonight.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.club.entity.ClubMember;
import webProject.togetherPartyTonight.domain.club.exception.ClubErrorCode;
import webProject.togetherPartyTonight.domain.club.repository.ClubMemberRepository;
import webProject.togetherPartyTonight.domain.club.repository.ClubRepository;
import webProject.togetherPartyTonight.domain.review.dto.request.CreateReviewRequestDto;
import webProject.togetherPartyTonight.domain.review.dto.request.UpdateReviewRequestDto;
import webProject.togetherPartyTonight.domain.review.dto.response.GetReviewDetailResponseDto;
import webProject.togetherPartyTonight.domain.review.entity.Review;
import webProject.togetherPartyTonight.domain.review.exception.ReviewErrorCode;
import webProject.togetherPartyTonight.domain.review.exception.ReviewException;
import webProject.togetherPartyTonight.domain.review.repository.ReviewRepository;
import webProject.togetherPartyTonight.global.error.ErrorCode;
import webProject.togetherPartyTonight.infra.S3.S3Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;

    private final S3Service s3Service;

    private final String directory = "review/";

    @Transactional
    public void addReview(CreateReviewRequestDto request, MultipartFile image) {
        Long clubId = request.getClubId();
        Long userId = request.getUserId();

        if(reviewRepository.findByClubClubIdAndMemberId(clubId, userId).isPresent())
            throw new ReviewException(ReviewErrorCode.IS_ALREADY_WRITTEN);

        Club club = clubRepository.findById(clubId).orElseThrow(
                () -> new ReviewException(ClubErrorCode.INVALID_CLUB_ID)
        );

        ClubMember clubMember = clubMemberRepository.findByClubClubIdAndMemberId(clubId, userId).orElseThrow(
                () -> new ReviewException(ErrorCode.INVALID_MEMBER_ID)
        );

        //clubMember에 사용자가 존재하고, 현재날짜가 meetingDate보다 이후일 경우에만 리뷰 작성가능
        if(LocalDate.now().isAfter(club.getMeetingDate()))  {
            String imageUrl;
            if (image==null) {
                imageUrl = s3Service.getImage("review_default.jpg");
            }
            else {
                imageUrl = s3Service.uploadImage(image, directory, request.getUserId());
            }
            Review review = request.toEntity(club, clubMember.getMember(),imageUrl);
            reviewRepository.save(review);
        }
        else throw new ReviewException(ReviewErrorCode.INVALID_REVIEW_DATE);
    }

    public GetReviewDetailResponseDto getReviewDetail(Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ReviewException(ReviewErrorCode.INVALID_REVIEW_ID)
        );
        return new GetReviewDetailResponseDto().toDto(review);

    }

    @Transactional
    public void modifyReview(UpdateReviewRequestDto request, MultipartFile image) {
        Long reviewId = request.getReviewId();
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ReviewException(ReviewErrorCode.INVALID_REVIEW_ID)
        );

        String imageUrl="";
        if(image!=null){ //수정할 새로운 이미지가 있으면
            imageUrl = s3Service.uploadImage(image, directory, request.getUserId()); //s3에 업로드하고 url받아옴
            if(!review.getImageUrl().contains("default")) { //기존 이미지가 default 이미지가 아니라면
                s3Service.deleteImage(review.getImageUrl()); //s3에서 삭제
            }
        }

        request.modify(review, imageUrl); //디비에 새로운 수정사항 반영
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        // TODO: 2023/06/21 권한없음 exception
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                ()-> new ReviewException(ReviewErrorCode.INVALID_REVIEW_ID)
        );
        if (!review.getImageUrl().contains("default"))
            s3Service.deleteImage(review.getImageUrl());
        reviewRepository.deleteById(reviewId);

    }
}
