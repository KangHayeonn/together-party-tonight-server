package webProject.togetherPartyTonight.club.info;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import webProject.togetherPartyTonight.domain.club.info.dto.AddClubRequest;
import webProject.togetherPartyTonight.domain.club.info.dto.ClubDetailResponse;
import webProject.togetherPartyTonight.domain.club.info.dto.DeleteAndSignupRequestDto;
import webProject.togetherPartyTonight.domain.club.info.entity.Club;
import webProject.togetherPartyTonight.domain.club.info.exception.ClubException;
import webProject.togetherPartyTonight.domain.club.info.repository.ClubRepository;
import webProject.togetherPartyTonight.domain.club.info.service.ClubService;
import webProject.togetherPartyTonight.global.error.ErrorCode;

import java.time.LocalDate;
import java.util.Optional;

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
//        //given
        ClubRequestDto request = getRequest();
        Club requestEntity = new Club(request);
        Club response = getResponse();
        doReturn(response).when(clubRepository).save(any(requestEntity.getClass()));

//        //when
        clubService.addClub(request);

//        //then
        assertThatNoException();
//        /*
//        assertThat (...)
//         */
    }

    @Test
    @DisplayName("모임 상세 조회 성공")
    void getClubSuccess () {
        //given
        Long clubId = 1L;

        doReturn(Optional.ofNullable(getResponse())).when(clubRepository).findById(clubId);
        //when
        ClubResponseDto responseDto = clubService.getClub(clubId);
        //then
        assertThat(responseDto.getClubName().equals("test"));
        /*
        assertThat ...
         */
    }

    @Test
    @DisplayName("모임 삭제 성공")
    void deleteClubSuccess () {
        //given
        Long clubId = 1L;
        doNothing().when(clubRepository).deleteById(clubId);
        //when
        clubService.deleteClub(clubId);
        //then
        assertDoesNotThrow(() -> new ClubException(ErrorCode.NOT_FOUND));
    }

    /**
     * 예외처리 테스트 코드
     */
    @Test
    @DisplayName("모임 상세 조회 실패")
    void getClubFail () {
        //given
        Long clubId = 999L;
        doThrow(new ClubException(ErrorCode.NOT_FOUND)).when(clubRepository).findById(clubId);
        //when
        //then
        assertThatThrownBy(() -> {
            clubService.getClub(clubId);
        })
                .isInstanceOf(ClubException.class)
                .hasMessage(ErrorCode.NOT_FOUND.getMessage());
    }

    ClubRequestDto getRequest () {
        return ClubRequestDto.builder()
                .clubName("test")
                .clubContent("test-content")
                .clubCategory("test-category")
                .clubTags("test-tags")
                .address("test-address")
                .meetingDate(LocalDate.parse("2023-06-11"))
                .userId(1L)
                .clubMinimum(2)
                .clubMaximum(6)
                .build();
    }

    Club getResponse () {
        return Club.builder()
                .clubName("test")
                .clubId(1L)
                .clubPoint(null)
                .clubContent("test-content")
                .clubCategory("test-category")
                .clubTags("test-tags")
                .address("test-address")
                .meetingDate(LocalDate.parse("2023-06-11"))
                .masterId(2L)
                .clubMinimum(2)
                .clubMaximum(6)
                .clubState(true)
                .build();
    }

}
