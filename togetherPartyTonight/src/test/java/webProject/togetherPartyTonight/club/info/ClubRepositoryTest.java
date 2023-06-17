package webProject.togetherPartyTonight.club.info;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.geo.Point;
import org.springframework.transaction.annotation.Transactional;
import webProject.togetherPartyTonight.domain.club.info.entity.Club;
import webProject.togetherPartyTonight.domain.club.info.repository.ClubRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ClubRepositoryTest {
    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    TestEntityManager testEntityManager;


    @BeforeEach
    void beforeEach () {
        EntityManager em = testEntityManager.getEntityManager();
        //각 테스트 실행 전에 auto generate되는 PK값을 1로 초기화
        em.createNativeQuery("ALTER TABLE CLUB ALTER COLUMN club_id RESTART WITH 1").executeUpdate();
        //h2 설정이 mysql모드이지만 일부 호환되지 않는 문법이 있어서 auto_increment 초기화는 h2문법으로 작성
    }

    @DisplayName("모임 추가")
    @Test
    void addClub () {
        //given
        Club club = Club.builder()
                .clubName("test")
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
        //when
        Club save = clubRepository.save(club);

        //then
        assertThat(save.getClubName().equals(club.getClubName()));
        assertThat(save.getClubState()==true);
        // 저장 전 엔티티와 저장 후 엔티티가 같은지 비교
        /*
        assertThat (...)
         */
    }

    @DisplayName("모임 상세 조회")
    @Test
    void getClub () {
        //given
        Long clubId = 1L;
        Club club = Club.builder()
                .clubName("test")
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

        Club save = clubRepository.save(club);


        //when
        Optional<Club> optionalClub = clubRepository.findById(clubId);
        assertThat(optionalClub.isPresent());
        //then

        assertThat(optionalClub.get().getClubId().equals(save.getClubId()));
        //찾아온 데이터의 id값이 given의 id와 같은지 비교
    }

    @DisplayName("모임 삭제")
    @Test
    void deleteClub() {
        //given
        Long clubId = 1L;
        Club club = Club.builder()
                .clubName("test")
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
        clubRepository.save(club);

        //when
        clubRepository.deleteById(1L);

        //then
        Optional<Club> afterDelete = clubRepository.findById(clubId);
        assertThat(afterDelete.isEmpty());
        //삭제하고 나서 해당 데이터가 존재하지 않는 것을 검증
    }
}
