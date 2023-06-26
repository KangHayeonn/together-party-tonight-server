package webProject.togetherPartyTonight.club.info;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import webProject.togetherPartyTonight.domain.club.repository.ClubRepository;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ClubRepositoryTest {
    // TODO: 2023/06/18 h2 DB에서 POINT type 사용 방법 숙지. 현재 unknown data type이라고 떠서 table 생성이 안돼서 테스트 불가. 빌드를 위해 주석처리
    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    TestEntityManager testEntityManager;


    @BeforeEach
    void beforeEach () {
        EntityManager em = testEntityManager.getEntityManager();
        //각 테스트 실행 전에 auto generate되는 PK값을 1로 초기화
        em.createNativeQuery("ALTER TABLE club ALTER COLUMN club_id RESTART WITH 1").executeUpdate();
        //h2 설정이 mysql모드이지만 일부 호환되지 않는 문법이 있어서 auto_increment 초기화는 h2문법으로 작성
    }

//    @DisplayName("모임 추가")
//    @Test
//    void addClub () {
//        //given
//        Member master = new Member();
//        master.setId(1L);
//        GeometryFactory gf = new GeometryFactory();
//        Point point = gf.createPoint(new Coordinate(10, 20));
//        Club club = Club.builder()
//                .master(master)
//                .clubName("name")
//                .clubCategory("category")
//                .clubMinimum(2)
//                .clubMaximum(6)
//                .clubContent("content")
//                .clubTags("tags")
//                .clubPoint(null)
//                .address("address")
//                .meetingDate(LocalDate.parse("2023-06-11"))
//                .clubState(true)
//                .build();
//        //when
//        Club save = clubRepository.save(club);
//        clubRepository.savePoint(point, save.getClubId() );
//
//        //then
//        assertThat(save.getClubName().equals(club.getClubName()));
//        assertThat(save.getClubState()==true);
//        // 저장 전 엔티티와 저장 후 엔티티가 같은지 비교
//        /*
//        assertThat (...)
//         */
//    }
//
//    @DisplayName("모임 상세 조회")
//    @Test
//    void getClub () {
//        //given
//        Member master = new Member();
//        master.setId(1L);
//        GeometryFactory gf = new GeometryFactory();
//        Point point = gf.createPoint(new Coordinate(10, 20));
//        Club club = Club.builder()
//                .master(master)
//                .clubName("name")
//                .clubCategory("category")
//                .clubMinimum(2)
//                .clubMaximum(6)
//                .clubContent("content")
//                .clubTags("tags")
//                .clubPoint(null)
//                .address("address")
//                .meetingDate(LocalDate.parse("2023-06-11"))
//                .clubState(true)
//                .build();
//
//        Club save = clubRepository.save(club);
//        clubRepository.savePoint(point, save.getClubId() );
//
//        //when
//        Club find = clubRepository.findById(1L).get();
//
//        //then
//
//        assertThat(find.getClubName().equals(save.getClubName()));
//    }
//
//    @DisplayName("모임 삭제")
//    @Test
//    void deleteClub() {
//        //given
//        Member master = new Member();
//        master.setId(1L);
//        GeometryFactory gf = new GeometryFactory();
//        Point point = gf.createPoint(new Coordinate(10, 20));
//        Club club = Club.builder()
//                .master(master)
//                .clubName("name")
//                .clubCategory("category")
//                .clubMinimum(2)
//                .clubMaximum(6)
//                .clubContent("content")
//                .clubTags("tags")
//                .clubPoint(null)
//                .address("address")
//                .meetingDate(LocalDate.parse("2023-06-11"))
//                .clubState(true)
//                .build();
//
//        Club save = clubRepository.save(club);
//        clubRepository.savePoint(point, save.getClubId() );
//
//        //when
//        clubRepository.deleteById(1L);
//
//        //then
//        Optional<Club> afterDelete = clubRepository.findById(1L);
//        assertThat(afterDelete.isEmpty());
//        //삭제하고 나서 해당 데이터가 존재하지 않는 것을 검증
//    }
}
