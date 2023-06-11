package webProject.togetherPartyTonight.domain.club.info.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.PropertyValueException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import webProject.togetherPartyTonight.domain.club.info.dto.ClubResponseDto;
import webProject.togetherPartyTonight.domain.club.info.dto.ClubRequestDto;
import webProject.togetherPartyTonight.domain.club.info.entity.Club;
import webProject.togetherPartyTonight.domain.club.info.exception.ClubException;
import webProject.togetherPartyTonight.domain.club.info.repository.ClubRepository;
import webProject.togetherPartyTonight.global.error.ErrorCode;

import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    public void addClub (ClubRequestDto clubRequest) {
        Club club = new Club(clubRequest);
        System.out.println("club.getClubPoint() = " + club.getClubPoint());
        System.out.println("club.getAddress() = " + club.getAddress());
        System.out.println("clubRequest = " + club.getClubTags());
//        try {
            clubRepository.save(club);
//        } catch (Exception e) {
//            throw new ClubException(ErrorCode.INTERNAL_SERVER_ERROR);
//        }
    }

    /**
     *
     * @param id 조회하고자 하는 club id
     * 해당 모임이 없으면 throw exception
     * @return ClubResponseDto
     */
    public ClubResponseDto getClub(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ClubException(ErrorCode.NOT_FOUND));
        return new ClubResponseDto().toDto(club);
    }

    public void deleteClub (Long id) {
        try {
            clubRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e) {
            throw new ClubException(ErrorCode.NOT_FOUND);
        }
    }

    public ClubResponseDto modifyClub(Long id, ClubRequestDto clubRequest) {
        Club club = clubRepository.findById(id)
                .orElseThrow(()-> new ClubException(ErrorCode.NOT_FOUND));
        //수정하는 코드
        return new ClubResponseDto().toDto(club);

    }
}
