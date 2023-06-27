package webProject.togetherPartyTonight.domain.club.service;


import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import webProject.togetherPartyTonight.domain.club.dto.request.*;
import webProject.togetherPartyTonight.domain.club.dto.response.*;
import webProject.togetherPartyTonight.domain.club.entity.*;
import webProject.togetherPartyTonight.domain.club.exception.ClubErrorCode;
import webProject.togetherPartyTonight.domain.club.exception.ClubException;
import webProject.togetherPartyTonight.domain.club.repository.ClubMemberRepository;
import webProject.togetherPartyTonight.domain.club.repository.ClubRepository;
import webProject.togetherPartyTonight.domain.club.repository.ClubSignupRepository;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.global.error.ErrorCode;
import webProject.togetherPartyTonight.infra.S3.S3Service;

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

    private final S3Service s3Service;

    private final String directory = "club/";


// TODO: 2023/06/18 meetingDate가 지나면 clubState=false -> 스케줄러


    @Transactional
    public void addClub (CreateClubRequestDto clubRequest, MultipartFile image) {
        Member master = memberRepository.getReferenceById(clubRequest.getUserId());
        // TODO: 2023/06/17 JWT 내부 유저정보와 requestBody의 userId가 다르면 '권한 없음' exception

        Point point = makePoint(clubRequest.getLatitude(), clubRequest.getLongitude());
        //point 생성

        String url="";
        if (image != null) {
            url = s3Service.uploadImage(image, directory, clubRequest.getUserId());
        }
        else {
            //카테고리별 디폴트 이미지
            url = getDefaultImage(clubRequest.getClubCategory());
        }

        Club club = clubRequest.toClub(master, point, url);
        //엔티티 생성

        clubRepository.save(club);
        //저장

    }

    /**
     *
     * @param id 조회하고자 하는 club id
     * 해당 모임이 없으면 throw exception
     * @return ClubResponseDto
     */
    public GetClubResponseDto getClub(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ClubException(ClubErrorCode.INVALID_CLUB_ID));
        return new GetClubResponseDto().toDto(club);
    }

    @Transactional
    public void deleteClub (DeleteClubAndSignupRequestDto requestDto) {
        Long clubId = requestDto.getClubId();
        Long userId = requestDto.getUserId();
        // TODO: 2023/06/17 JWT 내부 유저정보와 requestBody의 userId가 다르면 '권한 없음' exception
        Club club = clubRepository.findById(requestDto.getClubId()).orElseThrow(
                ()-> new ClubException(ClubErrorCode.INVALID_CLUB_ID)
        );

        if (!club.getImage().contains("default"))
            s3Service.deleteImage(club.getImage()); //s3에서 이미지삭제
        clubRepository.deleteById(clubId);

        //단방향 매핑이기 때문에 cascade 옵션을 주지 않고, 개별로 club_member, club_signup 엔티티 삭제
        clubMemberRepository.deleteByClubClubId(clubId);
        clubSignupRepository.deleteByClubClubId(clubId);
        }

    @Transactional
    public void modifyClub(UpdateClubRequestDto clubRequest, MultipartFile image) {
        Long clubId = clubRequest.getClubId();
        Long userId = clubRequest.getUserId();
        // TODO: 2023/06/17 JWT 내부 유저정보와 requestBody의 userId가 다르면 '권한 없음' exception
        Club club = clubRepository.findByClubIdAndMasterId(clubId, userId)
                .orElseThrow(()-> new ClubException(ClubErrorCode.INVALID_CLUB_ID));
        compareChange(clubRequest, club, image);

    }

    @Transactional
    public void compareChange (UpdateClubRequestDto clubDto, Club clubEntity, MultipartFile image) {
        Point point = makePoint(clubDto.getLatitude(), clubDto.getLongitude());
        Integer flag ;

        if (clubMemberRepository.getMemberCnt(clubDto.getClubId()) > clubDto.getClubMaximum())  flag = -1;
        else if (clubMemberRepository.getMemberCnt(clubDto.getClubId()) == clubDto.getClubMaximum()) flag =0;
        else flag =1;

        String imageUrl;
        if(image!=null){ //수정할 새로운 이미지가 있으면
            imageUrl = s3Service.uploadImage(image, directory, clubDto.getUserId()); //s3에 업로드하고 url받아옴
            if(!clubEntity.getImage().contains("default")) { //기존 이미지가 default 이미지가 아니라면
                s3Service.deleteImage(clubEntity.getImage()); //s3에서 삭제
            }
        }
        else {
            imageUrl = getDefaultImage(clubDto.getClubCategory());
        }

        clubDto.toEntity(clubEntity, flag, point, imageUrl);

    }

    @Scheduled(cron = "0 0 0/1 * * *") //1시간마다 실행
    public void checkMeetingDateIsPassed() {
        List<Club> clubList = clubRepository.findAll();
        for (Club c : clubList) {
            if (c.getMeetingDate().isBefore(LocalDate.now()))
                clubRepository.updateClubState();
        }
    }



    public Point makePoint (Float latitude, Float longitude) {
        GeometryFactory gf = new GeometryFactory();
        return gf.createPoint(new Coordinate(latitude, longitude));
    }

    public String  getDefaultImage (String category) {
        return s3Service.getImage(category+"_default.jpg");
    }

}
