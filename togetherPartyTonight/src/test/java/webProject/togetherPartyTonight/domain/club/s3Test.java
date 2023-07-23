package webProject.togetherPartyTonight.domain.club;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import webProject.togetherPartyTonight.domain.club.dto.request.CreateClubRequestDto;
import webProject.togetherPartyTonight.domain.club.service.ClubService;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.infra.S3.S3Service;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@Slf4j
public class s3Test {

    @Autowired
    S3Service s3Service;

    @Autowired
    ClubService clubService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void defaultImageTest () {
        CreateClubRequestDto dto = CreateClubRequestDto.builder()
                .clubName("default 테스트")
                .clubContent("테스트입니다")
                .clubMaximum(10)
                .clubCategory("맛집")
                .latitude(37.5F)
                .longitude(126.5F)
                .clubTags("태그")
                .meetingDate(LocalDateTime.parse("2023-08-23T12:30"))
                .address("테스트 주소")
                .build();

        Optional<Member> member = memberRepository.findById(2L);

        clubService.addClub(dto,null,member.get());


    }
}
