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
import webProject.togetherPartyTonight.domain.club.controller.ClubController;
import webProject.togetherPartyTonight.domain.club.dto.response.GetClubResponseDto;
import webProject.togetherPartyTonight.domain.club.dto.request.CreateClubRequestDto;
import webProject.togetherPartyTonight.domain.club.repository.ClubRepository;
import webProject.togetherPartyTonight.domain.club.service.ClubService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
    void getDetailSuccess() throws Exception {
    }

    @Test
    @DisplayName("모임 추가 성공")
    void addSuccess() throws Exception {

    }

    @Test
    @DisplayName("모임 삭제 성공")
    void deleteSuccess () throws Exception{

    }

}