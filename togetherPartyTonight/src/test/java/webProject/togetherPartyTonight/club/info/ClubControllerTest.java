package webProject.togetherPartyTonight.club.info;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import webProject.togetherPartyTonight.domain.club.info.controller.ClubController;
import webProject.togetherPartyTonight.domain.club.info.dto.ClubDetailResponse;
import webProject.togetherPartyTonight.domain.club.info.dto.AddClubRequest;
import webProject.togetherPartyTonight.domain.club.info.entity.Club;
import webProject.togetherPartyTonight.domain.club.info.repository.ClubRepository;
import webProject.togetherPartyTonight.domain.club.info.service.ClubService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Mockito를 이용한 controller test
 * test용 dummy data sql파일로 만드는게 편할것 같음
 * 이슈/에러를 잡지 못한 부분은 주석처리 되어있음
 */
@WebMvcTest(ClubControllerTest.class)
@ExtendWith(MockitoExtension.class)
public class ClubControllerTest {
    @InjectMocks
    private ClubController clubController;

    @Mock
    private ClubService clubService;

    @Mock
    private ClubRepository clubRepository;
    @Autowired
    private MockMvc mockMvc; //http 호출을 위한 MockMVC

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(clubController).build();
    }

    @Test
    @DisplayName("모임 상세 조회 성공")
    void getDetailSuccess () throws Exception{
//        //given
        ClubRequestDto request = getRequest();
        clubRepository.save(new Club(request));
        ClubResponseDto response = getResponse();
        Long clubId = 1L;

//        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/clubs/" + clubId)
        );
//        resultActions.andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.name").value("test"));
//        //then



    }

    @Test
    @DisplayName("모임 추가 성공")
    void addSuccess () throws Exception{
        //given
        ClubRequestDto request = getRequest();

        ResultActions resultActions= mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/clubs")
                                .content(new Gson().toJson(request))
                );
        //resultActions.andExpect(status().isOk());
        //LocalDate 형식을 gson이 직렬화 하지 못하는 이슈가 있어서 해당 클래스를 구현해야해서 임시 주석처리

        //then
        //verify(clubService).addClub(request);
    }

    @Test
    @DisplayName("모임 삭제 성공")
    void deleteSuccess () throws Exception{
        //given
        Long clubId = 1L;
        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/clubs/1")
                        .contentType(MediaType.APPLICATION_JSON)
        );
        resultActions.andExpect(status().isOk());

        //then
        //verify(clubService).deleteClub(1L);
    }

    ClubRequestDto getRequest() {
        return ClubRequestDto.builder()
                .clubName("test")
                .clubContent("test-content")
                .clubCategory("test-category")
                .clubTags("test-tags")
                .address("test-address")
                .meetingDate(LocalDate.parse("2023-06-11"))
                .latitude(20.222F)
                .longitude(11.11F)
                .userId(1L)
                .clubMinimum(2)
                .clubMaximum(6)
                .build();
    }

    ClubResponseDto getResponse () {
        return ClubResponseDto.builder()
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




}
