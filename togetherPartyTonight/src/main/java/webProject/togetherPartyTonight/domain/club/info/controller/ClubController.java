package webProject.togetherPartyTonight.domain.club.info.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webProject.togetherPartyTonight.domain.club.info.dto.ClubRequestDto;
import webProject.togetherPartyTonight.domain.club.info.dto.ClubResponseDto;
import webProject.togetherPartyTonight.domain.club.info.service.ClubService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/club")
@Slf4j
public class ClubController {
    private final ClubService clubService;
    Logger logger = LoggerFactory.getLogger(ClubController.class);

    /**
     * 모임 추가 api
     */
    @PostMapping("")
    public ResponseEntity<ClubResponseDto> addClub (@RequestBody ClubRequestDto clubRequest, HttpServletRequest request) {
        logger.info(request.getMethod()+" "+request.getRequestURI());
        return new ResponseEntity<>(clubService.addClub(clubRequest), HttpStatus.OK);
    }

    /**
     * 모임 상세 조회 api
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClubResponseDto> getClub(@PathVariable Long id, HttpServletRequest request) {
        logger.info(request.getMethod()+" "+request.getRequestURI());
        return new ResponseEntity<>(clubService.getClub(id),HttpStatus.OK);
    }

    /**
     * 모임 삭제 api
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClub(@PathVariable Long id, HttpServletRequest request) {
        logger.info(request.getMethod()+" "+request.getRequestURI());
        clubService.deleteClub(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 모임 수정 api
     */
    @PutMapping("/{id}")
    public  ResponseEntity<ClubResponseDto> modifyClub(
            @PathVariable Long id, @RequestBody ClubRequestDto newClubRequest, HttpServletRequest request) {
        logger.info(request.getMethod() + " " + request.getRequestURI());
        return new ResponseEntity<>(clubService.modifyClub(id, newClubRequest), HttpStatus.OK);
    }
}
