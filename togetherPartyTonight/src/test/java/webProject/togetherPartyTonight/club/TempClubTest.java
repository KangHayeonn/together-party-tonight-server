package webProject.togetherPartyTonight.club;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import webProject.togetherPartyTonight.domain.club.info.dto.ClubResponseDto;
import webProject.togetherPartyTonight.domain.club.info.dto.NewClubRequest;
import webProject.togetherPartyTonight.domain.club.info.service.ClubService;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Mockito를 이용한 controller test
 */
@WebMvcTest(TempClubTest.class)
public class TempClubTest {
//    @InjectMocks //mock 객체를 주입할 곳
//    private ClubController clubController;
    @MockBean //mock 객체 생성
    private ClubService clubService;


    @Autowired
    private MockMvc mockMvc; //http 호출을 위한 MockMVC

    @Test
    @DisplayName("모임 상세 조회 성공")
    void getClubSuccess () throws Exception{

    }

    @Test
    @DisplayName("모임 추가 성공")
    void addClubSuccess () throws Exception {

    }

    private NewClubRequest newClubRequest () {
        return NewClubRequest.builder()
                .name("test")
                .clubDetails("this is test")
                .maximum(6)
                .minimum(2)
                .clubState("test state")
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

    }

    private ClubResponseDto clubResponseDto() {

        return null;
    }
}
