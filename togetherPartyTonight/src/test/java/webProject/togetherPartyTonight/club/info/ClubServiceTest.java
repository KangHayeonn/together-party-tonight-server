package webProject.togetherPartyTonight.club.info;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.geo.Point;
import webProject.togetherPartyTonight.domain.club.info.dto.ClubRequestDto;
import webProject.togetherPartyTonight.domain.club.info.dto.ClubResponseDto;
import webProject.togetherPartyTonight.domain.club.info.entity.Club;
import webProject.togetherPartyTonight.domain.club.info.exception.ClubException;
import webProject.togetherPartyTonight.domain.club.info.repository.ClubRepository;
import webProject.togetherPartyTonight.domain.club.info.service.ClubService;
import webProject.togetherPartyTonight.global.error.ErrorCode;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
//        ClubRequestDto request = getRequest();
//        Club requestEntity = new Club().toEntity(request);
//        doReturn(new ClubResponseDto().toDto(getResponse())).when(clubRepository).save(requestEntity);
//
//        //when
//        ClubResponseDto clubResponseDto = clubService.addClub(any(request.getClass()));
//
//        //then
//        assertThat(request.getName().equals(clubResponseDto.getName()));
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
        assertThat(responseDto.getName().equals("test"));
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
                .name("test")
                .clubDetails("test-details")
                .clubCategory("test-category")
                .clubState("test-state")
//                .clubPoint(new Point(10,20))
                .maximum(6)
                .minimum(2)
                .createdDate(LocalDateTime.now())
                .build();
    }

    Club getResponse () {
        return Club.builder()
                .id(1L)
                .name("test")
                .clubDetails("test-details")
                .clubCategory("test-category")
                .clubState("test-state")
//                .clubPoint(new Point(10,20))
                .maximum(6)
                .minimum(2)
                .createdDate(LocalDateTime.now())
                .build();
    }

}
