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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import webProject.togetherPartyTonight.domain.club.info.controller.ClubController;
import webProject.togetherPartyTonight.domain.club.info.dto.ClubResponseDto;
import webProject.togetherPartyTonight.domain.club.info.dto.ClubRequestDto;
import webProject.togetherPartyTonight.domain.club.info.service.ClubService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Mockito를 이용한 controller test
 */
@WebMvcTest(ClubControllerTest.class)
@ExtendWith(MockitoExtension.class)
public class ClubControllerTest {
    @InjectMocks
    private ClubController clubController;

    @Mock
    private ClubService clubService;

    @Autowired
    private MockMvc mockMvc; //http 호출을 위한 MockMVC

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(clubController).build();
    }

    @Test
    @DisplayName("모임 상세 조회 성공")
    void getDetailSuccess () throws Exception{
        //given
        ClubResponseDto response = getResponse();
        Long clubId = 1L;

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/club/" + clubId)
        );
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"));

        //then

    }

    @Test
    @DisplayName("모임 추가 성공")
    void addSuccess () throws Exception{
        //given
        ClubRequestDto request = getRequest();
        ClubResponseDto response = getResponse();

        doReturn(response).when(clubService)
                .addClub(any(ClubRequestDto.class));

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/club")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

//        resultActions.andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("test"));

        //then
        verify(clubService).addClub(request);
    }

    @Test
    @DisplayName("모임 삭제 성공")
    void deleteSuccess () throws Exception{
        Long clubId = 1L;

        doNothing().when(clubService)
                .deleteClub(clubId);
        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/club/1")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    ClubRequestDto getRequest() {
        return ClubRequestDto.builder()
                .name("test")
                .clubCategory("test-category")
                .clubState("test-state")
                .minimum(2)
                .maximum(6)
                .clubDetails("test-details")
                .build();
    }

    ClubResponseDto getResponse () {
        return ClubResponseDto.builder()
                .name("test")
                .clubCategory("test-category")
                .clubState("test-state")
                .minimum(2)
                .maximum(6)
                .clubDetails("test-details")
                .build();
    }




}
