package webProject.togetherPartyTonight.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.club.entity.ClubMember;
import webProject.togetherPartyTonight.domain.club.exception.ClubErrorCode;
import webProject.togetherPartyTonight.domain.club.exception.ClubException;
import webProject.togetherPartyTonight.domain.club.repository.ClubMemberRepository;
import webProject.togetherPartyTonight.domain.club.repository.ClubRepository;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.review.dto.request.CreateReviewRequestDto;
import webProject.togetherPartyTonight.domain.review.dto.request.UpdateReviewRequestDto;
import webProject.togetherPartyTonight.domain.review.dto.response.GetReviewDetailResponseDto;
import webProject.togetherPartyTonight.domain.review.dto.response.MyPageReviewResponseDto;
import webProject.togetherPartyTonight.domain.review.dto.response.ReviewListDto;
import webProject.togetherPartyTonight.domain.review.entity.Review;
import webProject.togetherPartyTonight.domain.review.exception.ReviewErrorCode;
import webProject.togetherPartyTonight.domain.review.exception.ReviewException;
import webProject.togetherPartyTonight.domain.review.repository.ReviewRepository;
import webProject.togetherPartyTonight.global.error.ErrorCode;
import webProject.togetherPartyTonight.infra.S3.S3Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;

    private final S3Service s3Service;

    private final String directory = "review/";

    @Transactional
    public void addReview(CreateReviewRequestDto request, MultipartFile image, Member member) {
        Long clubId = request.getClubId();

        Club club = clubRepository.findById(clubId).orElseThrow(
                () -> new ReviewException(ClubErrorCode.INVALID_CLUB_ID)
        );

        if(reviewRepository.findByClubClubIdAndMemberId(clubId, member.getId()).isPresent())
            throw new ReviewException(ReviewErrorCode.IS_ALREADY_WRITTEN);

        ClubMember clubMember = clubMemberRepository.findByClubClubIdAndMemberId(clubId, member.getId()).orElseThrow(
                () -> new ReviewException(ReviewErrorCode.NOT_A_MEMBER)
        );

        //clubMember에 사용자가 존재하고, 현재날짜가 meetingDate보다 이후일 경우에만 리뷰 작성가능
        if(LocalDateTime.now().isAfter(club.getMeetingDate()))  {
            String imageUrl;
            if (image==null) {
                imageUrl = s3Service.getImage("review_default.jpg");
            }
            else {
                imageUrl = s3Service.uploadImage(image, directory, member.getId());
            }
            Review review = request.toEntity(club, clubMember.getMember(),imageUrl);
            reviewRepository.save(review);
            addMasterReviewInfo(review);
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
    public void modifyReview(UpdateReviewRequestDto request, MultipartFile image, Member member) {
        Long reviewId = request.getReviewId();
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ReviewException(ReviewErrorCode.INVALID_REVIEW_ID)
        );

        checkAuthority(review.getMember().getId(), member);

        String imageUrl="";
        if(image!=null){ //수정할 새로운 이미지가 있으면
            imageUrl = s3Service.uploadImage(image, directory, member.getId()); //s3에 업로드하고 url받아옴
            if(!review.getImageUrl().contains("default")) { //기존 이미지가 default 이미지가 아니라면
                s3Service.deleteImage(review.getImageUrl()); //s3에서 삭제
            }
        }
        else {
            imageUrl= s3Service.getImage("review_default.jpg");
        }

        request.modify(review, imageUrl); //디비에 새로운 수정사항 반영
        updateMasterReviewInfo(review);
    }

    @Transactional
    public void deleteReview(Long reviewId, Member member) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                ()-> new ReviewException(ReviewErrorCode.INVALID_REVIEW_ID)
        );

        checkAuthority(review.getMember().getId(), member);

        if (!review.getImageUrl().contains("default"))
            s3Service.deleteImage(review.getImageUrl());
        reviewRepository.deleteById(reviewId);
        deleteMasterReviewInfo(review);

    }

    public ReviewListDto getMyReviews (Member member, Pageable pageable) {
        Optional<Page<Review>> optionalReviews = reviewRepository.findByMemberId(member.getId(), pageable);
        ReviewListDto reviewListDto = new ReviewListDto();
        setReviewList(optionalReviews, reviewListDto);
        setPageable(reviewListDto, optionalReviews.get());
        return reviewListDto;

    }

    public ReviewListDto getOthersReviews (Long memberId ) {
        Optional<List<Review>> optionalReviews = reviewRepository.findByClubMasterId(memberId);
        ReviewListDto reviewListDto = new ReviewListDto();
        setReviewList(optionalReviews, reviewListDto);
        return reviewListDto;
    }

    public void setPageable (ReviewListDto reviewListDto, Page<Review> pageable) {
        reviewListDto.setPage(pageable.getPageable().getPageNumber());
        reviewListDto.setSize(pageable.getPageable().getPageSize());
        reviewListDto.setTotalPages(pageable.getTotalPages());
        reviewListDto.setTotalElements(pageable.getTotalElements());
    }


    public void checkAuthority(Long memberId, Member member) {
        if(memberId != member.getId()) throw new ClubException(ErrorCode.FORBIDDEN);
    }

    public void setReviewList (Optional<? extends Iterable<Review>> reviews, ReviewListDto reviewListDto) {
        List<MyPageReviewResponseDto> list = new ArrayList<>();
        if (reviews.isPresent()) {
            for (Review r : reviews.get()) {
                list.add(new MyPageReviewResponseDto().toDto(r));
            }
        }
        reviewListDto.setReviewList(list);
    }

    @Transactional
    public void addMasterReviewInfo (Review review) {
        Member master = review.getClub().getMaster();
        int reviewCount = master.getReviewCount();
        float avg = master.getRatingAvg();

        float total = avg * reviewCount;
        total += review.getRating(); //새로운 합계
        reviewCount += 1; //리뷰 갯수 증가
        avg = total/reviewCount; //새로운 평점

        master.setReviewCount(reviewCount);
        master.setRatingAvg(avg);

    }

    @Transactional
    public void deleteMasterReviewInfo (Review review) {
        Member master = review.getClub().getMaster();
        Integer rating = review.getRating();
        Integer reviewCount = master.getReviewCount();
        float avg = master.getRatingAvg();

        float total = avg*reviewCount;
        total -=rating; //평점 삭제
        reviewCount-=1; //리뷰 갯수 감소
        avg = total/reviewCount; //새로운 평점

        master.setReviewCount(reviewCount);
        master.setRatingAvg(avg);
    }

    @Transactional
    public void updateMasterReviewInfo (Review review) {
        deleteMasterReviewInfo(review);
        addMasterReviewInfo(review);
    }
}
