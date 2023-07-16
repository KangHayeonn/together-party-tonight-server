package webProject.togetherPartyTonight.club.info;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import webProject.togetherPartyTonight.domain.club.dto.request.CreateClubRequestDto;
import webProject.togetherPartyTonight.domain.club.dto.request.DeleteClubAndSignupRequestDto;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.club.exception.ClubException;
import webProject.togetherPartyTonight.domain.club.repository.ClubRepository;
import webProject.togetherPartyTonight.domain.club.service.ClubService;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.global.error.ErrorCode;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClubServiceTest {

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private ClubService clubService;

    @Test
    @DisplayName("모임 추가 성공")
    void addClubSuccess () {

    }

    @Test
    @DisplayName("모임 상세 조회 성공")
    void getClubSuccess () {

    }

    @Test
    @DisplayName("모임 삭제 성공")
    void deleteClubSuccess () {

    }

    /**
     * 예외처리 테스트 코드
     */
    @Test
    @DisplayName("모임 상세 조회 실패")
    void getClubFail () {

    }



}
