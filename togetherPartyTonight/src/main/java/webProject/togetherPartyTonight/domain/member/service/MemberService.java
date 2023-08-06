package webProject.togetherPartyTonight.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import webProject.togetherPartyTonight.domain.member.dto.request.MemberDetailsModifyDto;
import webProject.togetherPartyTonight.domain.member.dto.response.MemberModifyProfileImageDto;
import webProject.togetherPartyTonight.domain.member.dto.request.MemberNicknameModifyDto;
import webProject.togetherPartyTonight.domain.member.dto.request.PasswordChangeDto;
import webProject.togetherPartyTonight.domain.member.dto.response.MemberInfoResponseDto;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.domain.member.exception.errorCode.MemberErrorCode;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.infra.S3.S3Service;

import java.io.IOException;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final RedisTemplate<String,String> redisTemplate;

    private final String directory = "member/";

    private final S3Service s3Service;

    private static final String BASIC_PROFILE = "https://topato-github-actions-s3-bucket.s3.ap-northeast-2.amazonaws.com/member_default.png";

    @Transactional(readOnly = true)
    public MemberInfoResponseDto findById(Long userId){

        Member member = getMember(userId);

        return MemberInfoResponseDto.from(member);

    }

    public MemberNicknameModifyDto modifyMemberInfo(Long userId, MemberNicknameModifyDto modifyDto){
        Member member = getMember(userId);

        member.setNickname(modifyDto.getNickname());

        log.info("바꾸는 유저 - {}", member.getEmail());

        log.info("변경닉네임 - {}",member.getNickname());

        return new MemberNicknameModifyDto(member.getNickname());
    }

    public MemberDetailsModifyDto modifyMemberDetails(Long userId, MemberDetailsModifyDto memberInfoDto) {
        Member member = getMember(userId);

        member.setDetails(memberInfoDto.getDetails());

        log.info("바꾸는 유저 - {}", member.getEmail());

        log.info("변경 자기소개 - {}",member.getDetails());

        return new MemberDetailsModifyDto(member.getDetails());
    }

    public MemberModifyProfileImageDto modifyMemberProfileImage(Long userId, MultipartFile profileImage) throws IOException {
        Member member = getMember(userId);

        String url = member.getProfileImage();
        log.info("바꾸는 유저 - {}", member.getEmail());

        if (profileImage == null && !url.contains("default")){

            url = BASIC_PROFILE;
            member.setProfileImage(url);
            return new MemberModifyProfileImageDto(url);
        }

        if (profileImage == null && url.contains("default")){
            return new MemberModifyProfileImageDto(url);
        }
        assert profileImage != null;
        log.info("프로필 이미지 - {}",profileImage.getBytes());

        //파일의 크기가 0이 아니면 s3업로드
        if (profileImage.getBytes().length != 0 && url.contains("default")) {
            url = s3Service.uploadImage(profileImage, directory,member.getId());
        }else if(profileImage.getBytes().length != 0 && !url.contains("default")){
            s3Service.deleteImage(url);
            url = s3Service.uploadImage(profileImage, directory,member.getId());
        }else if(profileImage.getBytes().length == 0 && !url.contains("default")){
            s3Service.deleteImage(url);
            url = BASIC_PROFILE;
        }

        log.info("받아온 url - {}",url);
        member.setProfileImage(url);

        return new MemberModifyProfileImageDto(url);
    }

    public void changePassword(Long userId, PasswordChangeDto passwordChangeDto) {
        Member member = getMember(userId);
        boolean passwordMatch = passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), member.getPassword());
        if(passwordMatch){
            //현재 패스워드와 변경 패스워드 같은 경우
            if(passwordChangeDto.getCurrentPassword().equals(passwordChangeDto.getNewPassword())){
                throw new MemberException(MemberErrorCode.PASSWORD_DUPLICATED);
            }
        }else{
            throw new MemberException(MemberErrorCode.INVALID_PASSWORD);
        }

        member.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));

    }

    public void deleteMember(Long memberId) {

        Member member = getMember(memberId);
        
        //회원 삭제
        memberRepository.delete(member);
        
        //리프레시 토큰 삭제
        redisTemplate.delete(String.valueOf(memberId));
    }



    private Member getMember(Long userId) {
        return memberRepository.findById(userId).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }



}
