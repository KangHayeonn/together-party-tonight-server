package webProject.togetherPartyTonight.domain.club.info.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webProject.togetherPartyTonight.domain.club.info.dto.NewClubRequest;
import webProject.togetherPartyTonight.domain.club.info.service.ClubService;
import webProject.togetherPartyTonight.global.common.DefaultResponse;

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
    public ResponseEntity<DefaultResponse> addClub (@RequestBody NewClubRequest clubRequest, HttpServletRequest request) {
        log.info(request.getMethod()+" "+request.getRequestURI());
        return new ResponseEntity<>(clubService.addClub(clubRequest), HttpStatus.OK);
    }

    /**
     * 모임 상세 조회 api
     */
    @GetMapping("/{id}")
    public ResponseEntity<DefaultResponse> getClub(@PathVariable Long id, HttpServletRequest request) {
        log.info(request.getMethod()+" "+request.getRequestURI());
        return new ResponseEntity<>(clubService.getClub(id),HttpStatus.OK);
    }

    /**
     * 모임 삭제 api
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<DefaultResponse> deleteClub(@PathVariable Long id, HttpServletRequest request) {
        log.info(request.getMethod()+" "+request.getRequestURI());
        return new ResponseEntity<>(clubService.deleteClub(id),HttpStatus.OK);
    }

    /**
     * 모임 수정 api
     */
    @PutMapping("/{id}")
    public  ResponseEntity<DefaultResponse> modifyClub(
            @PathVariable Long id,@RequestBody NewClubRequest newClubRequest, HttpServletRequest request) {
        log.info(request.getMethod() + " " + request.getRequestURI());
        return new ResponseEntity<>(clubService.modifyClub(id, newClubRequest), HttpStatus.OK);
    }
}
