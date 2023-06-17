package webProject.togetherPartyTonight.domain.club.info.service;


import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.stereotype.Service;
import webProject.togetherPartyTonight.domain.club.info.dto.*;
import webProject.togetherPartyTonight.domain.club.info.entity.ApprovalState;
import webProject.togetherPartyTonight.domain.club.info.entity.Club;
import webProject.togetherPartyTonight.domain.club.info.entity.ClubMember;
import webProject.togetherPartyTonight.domain.club.info.entity.ClubSignup;
import webProject.togetherPartyTonight.domain.club.info.exception.ClubException;
import webProject.togetherPartyTonight.domain.club.info.repository.ClubMemberRepository;
import webProject.togetherPartyTonight.domain.club.info.repository.ClubRepository;
import webProject.togetherPartyTonight.domain.club.info.repository.ClubSignupRepository;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.global.error.ErrorCode;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final ClubSignupRepository clubSignupRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final MemberRepository memberRepository;


// TODO: 2023/06/18 meetingDate가 지나면 clubState=false, member수가 clubMaximum이 되면 clubState=false


    @Transactional
    public void addClub (AddClubRequest clubRequest) {
        Member master = memberRepository.getReferenceById(clubRequest.getUserId());
        // TODO: 2023/06/17 JWT 내부 유저정보와 requestBody의 userId가 다르면 '권한 없음' exception
        Club club = clubRequest.toClub(master);
        try {
            GeometryFactory gf = new GeometryFactory();
            Point point = gf.createPoint(new Coordinate(clubRequest.getLatitude(), clubRequest.getLongitude()));
            clubRepository.save(club);
            clubRepository.savePoint(point, club.getClubId());

        } catch (Exception e) {
            e.printStackTrace();
            throw new ClubException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *
     * @param id 조회하고자 하는 club id
     * 해당 모임이 없으면 throw exception
     * @return ClubResponseDto
     */
    public ClubDetailResponse getClub(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ClubException(ErrorCode.INVALID_CLUB_ID));
        return new ClubDetailResponse().toDto(club);
    }

    public void deleteClub (DeleteAndSignupRequestDto requestDto) {
        try {
            Long clubId = requestDto.getClubId();
            Long userId = requestDto.getUserId();
            // TODO: 2023/06/17 JWT 내부 유저정보와 requestBody의 userId가 다르면 '권한 없음' exception
            clubRepository.deleteById(clubId);
        }catch (EmptyResultDataAccessException e) {
            throw new ClubException(ErrorCode.INVALID_CLUB_ID);
        }
    }

    @Transactional
    public ClubDetailResponse modifyClub(ModifyClubRequest clubRequest) {
        Long clubId = clubRequest.getClubId();
        Long userId = clubRequest.getUserId();
        // TODO: 2023/06/17 JWT 내부 유저정보와 requestBody의 userId가 다르면 '권한 없음' exception
        Club club = clubRepository.findById(clubId)
                .orElseThrow(()-> new ClubException(ErrorCode.INVALID_CLUB_ID));
        compareChange(clubRequest, club);
        return new ClubDetailResponse().toDto(club);

    }

    public Club compareChange (ModifyClubRequest clubDto, Club clubEntity) {
        clubEntity.setClubName(clubDto.getClubName());
        clubEntity.setClubCategory(clubDto.getClubCategory());
        clubEntity.setClubContent(clubDto.getClubContent());
        clubEntity.setClubMaximum(clubDto.getClubMaximum());
        clubEntity.setClubMinimum(clubDto.getClubMinimum());
        clubEntity.setClubTags(clubDto.getClubTags());
        clubEntity.setAddress(clubDto.getAddress());
        clubEntity.setMeetingDate(LocalDate.parse(clubDto.getMeetingDate()));

        // TODO: 2023/06/18  clubEntity.setClubPoint(point);
        // clubEntity.setClubState(clubDto.getClubState());
        return clubEntity;

    }

    /**
     * 모임 신청
     * @param requestDto clubId : 신청하는 모임, userId : 신청하는 사람
     */
    public void signup (DeleteAndSignupRequestDto requestDto) {
        Club club = clubRepository.getReferenceById(requestDto.getClubId());
        if (club==null) throw new ClubException(ErrorCode.INVALID_CLUB_ID);
        Member member = memberRepository.getReferenceById(requestDto.getUserId());
        // TODO: 2023/06/17 JWT 내부 유저정보와 requestBody의 userId가 다르면 '권한 없음' exception

        ClubSignup clubSignup = requestDto.toClubSignup(club, member, club.getMaster());
        clubSignupRepository.save(clubSignup);
    }

    /**
     * 모임 신청 취소
     * @param requestDto clubSignupId : 취소하려는 모임, userId : 취소하는 사람
     */

    @Transactional
    public void deleteAppliedClub (DeleteMyAppliedRequest requestDto) {
        Long clubSignupId = requestDto.getClubSignupId();
        Long userId = requestDto.getUserId();

        memberRepository.findById(userId).orElseThrow(
                () -> new MemberException(ErrorCode.INVALID_MEMBER_ID)
        );

        // TODO: 2023/06/17 JWT 내부 유저정보와 userId가 다르면 '권한 없음' exception

        ClubSignup clubSignup = clubSignupRepository.findById(clubSignupId).orElseThrow(
                () -> new ClubException(ErrorCode.INVALID_CLUB_SIGNUP_ID)
        );

        if (clubSignup.getClubSignupApprovalState().equals(ApprovalState.PENDING)) {
            clubSignupRepository.deleteById(clubSignupId);
        }
        else if (clubSignup.getClubSignupApprovalState().equals(ApprovalState.APPROVE)){
            //throw new ClubException()
        }
        else {
            //이미 거절당했습니다
        }
    }


    @Transactional
    public void approve (ApproveRequest requestDto) {

        Long clubSignupId = requestDto.getClubSignupId();
        // TODO: 2023/06/17 JWT 내부 유저정보와 masterId가 다르면 '권한 없음' exception

        ClubSignup clubSignup = clubSignupRepository.findById(clubSignupId).orElseThrow(
                () -> new ClubException(ErrorCode.INVALID_CLUB_SIGNUP_ID)
        );
        if (clubSignup.getClubSignupApprovalState().equals(ApprovalState.PENDING)) {
            //아직 수락/거절을 하지 않았을 경우
            if (requestDto.getApprove()) { //true 이면
                clubSignup.setClubSignupApprovalState(ApprovalState.APPROVE);
                //clubSignup 의 approvalState 를 APPROVE 로 변경
                clubSignup.setClubSignupApprovalDate(LocalDateTime.now());
                //clubSignupApprovalDate 를 현재 시각으로 update
                ClubMember clubMember = new ClubMember(clubSignup.getClub(), clubSignup.getClubMember());
                clubMemberRepository.save(clubMember);
                //ClubMember 에 추가
            } else {
                clubSignup.setClubSignupApprovalState(ApprovalState.REFUSE);
                //clubSignup 의 approvalState 를 REFUSE 로 변경
            }
        }
        else {
            //이미 수락/거절을 한 경우
            throw new ClubException(ErrorCode.ALREADY_APPROVED);
        }


    }

    //모임별 신청한 사람 조회
    public ReceivedApplicationList getRequestListPerClub (Long userId, Long clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(
                () -> new ClubException(ErrorCode.INVALID_CLUB_ID)
        );

        memberRepository.findById(userId).orElseThrow(
                () -> new MemberException(ErrorCode.INVALID_MEMBER_ID)
        );

        if (club.getMaster().getId() != userId) throw new ClubException(ErrorCode.FORBIDDEN);

        List<ClubSignup> clubSignups = clubSignupRepository.findAllByClubClubId(clubId);

        List<Application> list = new ArrayList<>();
        for (ClubSignup c : clubSignups) {
            Application application = Application.builder()
                    .clubSignupId(c.getClubSignupId())
                    .clubId(c.getClub().getClubId())
                    .clubName(c.getClub().getClubName())
                    .clubId(c.getClub().getClubId())
                    .userId(c.getClubMember().getId())
            //        .userName(c.getClubMember().getName())
                    .signupDate(c.getClubSignupDate().toString())
                    .approvalStatus(c.getClubSignupApprovalState().name())
                    .build();

            list.add(application);
        }
        return new ReceivedApplicationList(list);
    }

    //내가 신청한 모임 조회
    public MyAppliedClubList getAppliedList(Long userId) {
        memberRepository.findById(userId).orElseThrow(
                () -> new MemberException(ErrorCode.INVALID_MEMBER_ID)
        );

        // TODO: 2023/06/17 JWT 내부 유저정보와 userId가 다르면 '권한 없음' exception

        List<ClubSignup> signupMemberId = clubSignupRepository.findAllByClubMemberId(userId);
        List<Application> list = new ArrayList<>();
        for (ClubSignup c : signupMemberId) {
            Application application = Application.builder()
                    .clubSignupId(c.getClubSignupId())
                    .clubId(c.getClub().getClubId())
                    .clubName(c.getClub().getClubName())
                    .clubId(c.getClub().getClubId())
                    .userId(c.getClub().getMaster().getId())
                    //        .userName(c.getClubMember().getName())
                    .signupDate(c.getClubSignupDate().toString())
                    .approvalStatus(c.getClubSignupApprovalState().name())
                    .build();

            list.add(application);
        }
        return new MyAppliedClubList(list);
    }

    //내가 만든 모임 조회
    public MyOwnedClubList getOwnedList(Long userId) {
        memberRepository.findById(userId).orElseThrow(
                () -> new MemberException(ErrorCode.INVALID_MEMBER_ID)
        );

        // TODO: 2023/06/17 JWT 내부 유저정보와 userId가 다르면 '권한 없음' exception

        List<Club> clubs = clubRepository.findClubByMasterId(userId);
        List<MyOwnedClub> list = new ArrayList<>();
        for (Club c : clubs) {

            Point point = c.getClubPoint();
            List<String> tags = splitTags(c.getClubTags());
            MyOwnedClub myOwnedClub = MyOwnedClub.builder()
                    .clubName(c.getClubName())
                    .clubCategory(c.getClubCategory())
                    .clubMinimum(c.getClubMinimum())
                    .clubMaximum(c.getClubMaximum())
                    .clubContent(c.getClubContent())
                    .clubTags(tags)
                    .latitude((float) point.getX())
                    .longitude((float) point.getY())
                    .address(c.getAddress())
                    .meetingDate(String.valueOf(c.getMeetingDate()))
                    .build();

            list.add(myOwnedClub);
        }
        return new MyOwnedClubList(list);
    }

    public List<String> splitTags (String tags) {
        String[] split = tags.split(",");
        return Arrays.stream(split).collect(Collectors.toList());
    }


}
