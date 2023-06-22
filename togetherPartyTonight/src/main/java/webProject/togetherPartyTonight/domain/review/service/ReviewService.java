package webProject.togetherPartyTonight.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import webProject.togetherPartyTonight.domain.club.info.entity.Club;
import webProject.togetherPartyTonight.domain.club.info.entity.ClubMember;
import webProject.togetherPartyTonight.domain.club.info.repository.ClubMemberRepository;
import webProject.togetherPartyTonight.domain.club.info.repository.ClubRepository;
import webProject.togetherPartyTonight.domain.review.dto.request.AddReviewRequest;
import webProject.togetherPartyTonight.domain.review.dto.request.ModifyReviewRequest;
import webProject.togetherPartyTonight.domain.review.dto.response.ReviewDetailResponse;
import webProject.togetherPartyTonight.domain.review.entity.Review;
import webProject.togetherPartyTonight.domain.review.exception.ReviewException;
import webProject.togetherPartyTonight.domain.review.repository.ReviewRepository;
import webProject.togetherPartyTonight.global.error.ErrorCode;
import webProject.togetherPartyTonight.infra.S3.S3Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;

    private final S3Service s3Service;

    @Transactional
    public void addReview(AddReviewRequest request, MultipartFile image) {
        Long clubId = request.getClubId();
        Long userId = request.getUserId();

        if(reviewRepository.findByClubClubIdAndMemberId(clubId, userId).isPresent())
            throw new ReviewException(ErrorCode.IS_ALREADY_WRITTEN);

        Club club = clubRepository.findById(clubId).orElseThrow(
                () -> new ReviewException(ErrorCode.INVALID_CLUB_ID)
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
                imageUrl = s3Service.uploadImage(image);
            }
            Review review = request.toEntity(club, clubMember.getMember(),imageUrl);
            reviewRepository.save(review);
        }
        else throw new ReviewException(ErrorCode.INVALID_REVIEW_DATE);
    }

    public ReviewDetailResponse getReviewDetail(Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ReviewException(ErrorCode.INVALID_REVIEW_ID)
        );
        return new ReviewDetailResponse().toDto(review);

    }

    @Transactional
    public void modifyReview(ModifyReviewRequest request, MultipartFile image) {
        Long reviewId = request.getReviewId();
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ReviewException(ErrorCode.INVALID_REVIEW_ID)
        );

        String imageUrl="";
        if(image!=null){ //수정할 새로운 이미지가 있으면
            imageUrl = s3Service.uploadImage(image); //s3에 업로드하고 url받아옴
            if(!review.getImageUrl().contains("default")) { //기존 이미지가 default 이미지가 아니라면
                s3Service.deleteImage(review.getImageUrl()); //s3에서 삭제
            }
        }

        request.modify(review, imageUrl); //디비에 새로운 수정사항 반영
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        // TODO: 2023/06/21 권한없음 exception
        try {
            s3Service.deleteImage(reviewRepository.getReferenceById(reviewId).getImageUrl());
            reviewRepository.deleteById(reviewId);
        } catch (EmptyResultDataAccessException e){
            throw new ReviewException(ErrorCode.INVALID_REVIEW_ID);
        }
    }
}
