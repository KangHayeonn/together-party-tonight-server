package webProject.togetherPartyTonight.domain.club.info.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import webProject.togetherPartyTonight.domain.club.info.dto.ClubResponseDto;
import webProject.togetherPartyTonight.domain.club.info.dto.ClubResponseMessage;
import webProject.togetherPartyTonight.domain.club.info.dto.NewClubRequest;
import webProject.togetherPartyTonight.domain.club.info.entity.Club;
import webProject.togetherPartyTonight.domain.club.info.exception.ClubException;
import webProject.togetherPartyTonight.domain.club.info.repository.ClubRepository;
import webProject.togetherPartyTonight.global.common.DefaultResponse;
import webProject.togetherPartyTonight.global.common.ResponseMessage;
import webProject.togetherPartyTonight.global.error.ErrorCode;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    public DefaultResponse addClub (NewClubRequest clubRequest) {
        Club club = new Club(clubRequest);
        clubRepository.save(club);
        return new DefaultResponse(HttpStatus.OK, ClubResponseMessage.ADD_CLUB_SUCCESS);
    }

    /**
     *
     * @param id 조회하고자 하는 club id
     * 해당 모임이 없으면 throw exception
     * @return 상태코드, 메세지, 데이터
     */
    public DefaultResponse getClub(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ClubException(ErrorCode.NOT_FOUND));
        return new DefaultResponse(HttpStatus.OK,ClubResponseMessage.GET_CLUB_SUCCESS,new ClubResponseDto(club));
    }

    public DefaultResponse deleteClub (Long id) {
        clubRepository.deleteById(id);
        return new DefaultResponse(HttpStatus.OK, ClubResponseMessage.DELETE_CLUB_SUCCESS);
    }

    public DefaultResponse modifyClub(Long id, NewClubRequest clubRequest) {
        Club club = clubRepository.findById(id)
                .orElseThrow(()-> new ClubException(ErrorCode.NOT_FOUND));
        //수정
        return new DefaultResponse(HttpStatus.OK,ClubResponseMessage.MODIFY_CLUB_SUCCESS);

    }
}
