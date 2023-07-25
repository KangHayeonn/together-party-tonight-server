package webProject.togetherPartyTonight.domain.club.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import webProject.togetherPartyTonight.domain.member.exception.errorCode.MemberErrorCode;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.global.error.ErrorCode;
import webProject.togetherPartyTonight.global.util.ClubUtils;
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
@Slf4j
public class ClubService {
    private final ClubRepository clubRepository;
    private final ClubSignupRepository clubSignupRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final MemberRepository memberRepository;

    private final S3Service s3Service;

    private final String directory = "club/";
    private final ClubUtils clubUtils;


    @Transactional
    public void addClub (CreateClubRequestDto clubRequest, MultipartFile image, Member member) {
        Point point = makePoint(clubRequest.getLatitude(), clubRequest.getLongitude());

        String url="";
        if (image != null) {
            url = s3Service.uploadImage(image, directory,member.getId());
        }
        else {
            url = getDefaultImage(clubRequest.getClubCategory());
        }
        log.info("[ClubService]: url : {}", url);

        Club club = clubRequest.toClub(member, point, url);

        clubRepository.save(club);

    }

    public GetClubResponseDto getClub(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ClubException(ClubErrorCode.INVALID_CLUB_ID));
        List<String> tags = clubUtils.splitTags(club.getClubTags());
        return new GetClubResponseDto().toDto(club, tags);
    }

    @Transactional
    public void deleteClub (DeleteClubAndSignupRequestDto requestDto, Member member) {
        Long clubId = requestDto.getClubId();
        Club club = clubRepository.findById(requestDto.getClubId()).orElseThrow(
                ()-> new ClubException(ClubErrorCode.INVALID_CLUB_ID)
        );

       checkAuthority(club, member);

        if (!club.getImage().contains("default"))
            s3Service.deleteImage(club.getImage());
        clubRepository.deleteById(clubId);

        clubMemberRepository.deleteByClubClubId(clubId);
        clubSignupRepository.deleteByClubClubId(clubId);
        }

    @Transactional
    public void modifyClub(UpdateClubRequestDto clubRequest, MultipartFile image, Member member) {
        Long clubId = clubRequest.getClubId();
        Club club = clubRepository.findByClubIdAndMasterId(clubId, member.getId())
                .orElseThrow(()-> new ClubException(ClubErrorCode.INVALID_CLUB_ID));
       checkAuthority(club, member);
        compareChange(clubRequest, club, image, member);

    }

    @Transactional
    public void compareChange (UpdateClubRequestDto clubDto, Club clubEntity, MultipartFile image, Member member) {
        Point point = makePoint(clubDto.getLatitude(), clubDto.getLongitude());
        Integer flag ;

        if (clubMemberRepository.getMemberCnt(clubDto.getClubId()) > clubDto.getClubMaximum())  flag = -1;
        else if (clubMemberRepository.getMemberCnt(clubDto.getClubId()) == clubDto.getClubMaximum()) flag =0;
        else flag =1;

        String imageUrl;
        if(image!=null){
            imageUrl = s3Service.uploadImage(image, directory, member.getId());
            if(!clubEntity.getImage().contains("default")) {
                s3Service.deleteImage(clubEntity.getImage());
            }
        }
        else {
            imageUrl = getDefaultImage(clubDto.getClubCategory());
        }

        clubDto.toEntity(clubEntity, flag, point, imageUrl);

    }

    @Scheduled(cron = "0 0 0/1 * * *") //1시간마다 실행
    @Transactional
    public void checkMeetingDateIsPassed() {
        List<Club> clubList = clubRepository.findAll();
        for (Club c : clubList) {
            if (c.getMeetingDate().isBefore(LocalDateTime.now()))
                clubRepository.updateClubState(c.getClubId());
        }
    }



    public Point makePoint (Float latitude, Float longitude) {
        GeometryFactory gf = new GeometryFactory();
        return gf.createPoint(new Coordinate(latitude, longitude));
    }

    public String  getDefaultImage (String category) {
        try {
            ClubCategory categoryImage = ClubCategory.valueOf(category);
            return s3Service.getImage(categoryImage+"_default.svg");
        }catch (IllegalArgumentException e) {
            throw new ClubException(ClubErrorCode.INVALID_CATEGORY);
        }
    }

    public void checkAuthority(Club club, Member member) {
        if(club.getMaster().getId() != member.getId()) throw new ClubException(ErrorCode.FORBIDDEN);
    }

}
