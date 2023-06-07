package webProject.togetherPartyTonight.club.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.geo.Point;
import webProject.togetherPartyTonight.domain.club.info.entity.Club;
import webProject.togetherPartyTonight.domain.club.info.repository.ClubRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DB에는 아래와 같은 정보가 저장되어 있다고 가정
 * id : 1
 * name : test
 * clubState : test-state
 * clubDetails : test-details
 * clubCategory : test-category
 * createdDate : 2023-06-06
 * maximum : 6
 * minimum :2
 */
@DataJpaTest
public class ClubRepositoryTest {
    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    TestEntityManager testEntityManager;

    EntityManager em;

    @BeforeEach
    void beforeEach () {
        em = testEntityManager.getEntityManager();
        em.createNativeQuery("ALTER TABLE CLUB AUTO_INCREMENT=1;");
    }

    @DisplayName("모임 추가")
    @Test
    void addClub () {
        //given
        Club club = Club.builder()
                .name("test")
                .clubState("test-state")
                .clubDetails("test-details")
                .clubCategory("test-category")
                .createdDate(LocalDateTime.now())
                .minimum(2)
                .maximum(6)
                .build();
        //when
        Club save = clubRepository.save(club);

        //then
        assertThat(save.getName().equals(club.getName()));
        // 저장 전 엔티티와 저장 후 엔티티가 같은지 비교
        /*
        assertThat (...)
         */
    }

    @DisplayName("모임 상세 조회")
    @Test
    void getClub () {
        //given
        Long clubId = 2L;
        Club club = Club.builder()
                .name("test")
                .clubState("test-state")
                .clubDetails("test-details")
                .clubCategory("test-category")
                .createdDate(LocalDateTime.now())
                .minimum(2)
                .maximum(6)
                .build();
        clubRepository.save(club);


        //when
        Optional<Club> optionalClub = clubRepository.findById(clubId);

        //then
        assertThat(optionalClub.get().getId().equals(clubId));
        //찾아온 데이터의 id값이 given의 id와 같은지 비교
    }

    @DisplayName("모임 삭제")
    @Test
    void deleteClub() {
        //given
        Long clubId = 3L;
        Club club = Club.builder()
                .name("test")
                .clubState("test-state")
                .clubDetails("test-details")
                .clubCategory("test-category")
                .createdDate(LocalDateTime.now())
                .minimum(2)
                .maximum(6)
                .build();
        clubRepository.save(club);

        //when
        clubRepository.deleteById(clubId);

        //then
        Optional<Club> afterDelete = clubRepository.findById(clubId);
        assertThat(afterDelete.isEmpty());
        //삭제하고 나서 해당 데이터가 존재하지 않는 것을 검증
    }
}
