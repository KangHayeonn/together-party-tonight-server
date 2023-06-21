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

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;

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
        if(LocalDate.now().isAfter(club.getMeetingDate()))  {
            //clubMember에 사용자가 존재하고, 현재날짜가 meetingDate보다 이후일 경우에만 리뷰 작성가능
            Review review = request.toEntity(club, clubMember.getMember());
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
    public void modifyReview(ModifyReviewRequest request) {
        Long reviewId = request.getReviewId();
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ReviewException(ErrorCode.INVALID_REVIEW_ID)
        );
        request.modify(review);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        // TODO: 2023/06/21 권한없음 exception
        try {
            reviewRepository.deleteById(reviewId);
        } catch (EmptyResultDataAccessException e){
            throw new ReviewException(ErrorCode.INVALID_REVIEW_ID);
        }

    }
}
