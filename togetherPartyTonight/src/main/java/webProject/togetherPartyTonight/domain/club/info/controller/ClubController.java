package webProject.togetherPartyTonight.domain.club.info.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import webProject.togetherPartyTonight.global.common.CommonResponse;
import webProject.togetherPartyTonight.global.common.ResponseWithData;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
@Slf4j
@Api(tags = {"모임 기본 CRUD API"})
public class ClubController {
    private final ClubService clubService;
    private final String SUCCESS = "success";

    Logger logger = LoggerFactory.getLogger(ClubController.class);

    /**
     * 모임 추가 api
     */
    @PostMapping("")
    @ApiOperation(value = "모임 만들기")
    public ResponseEntity<CommonResponse> addClub (@RequestBody ClubRequestDto clubRequest, HttpServletRequest request) {
        logger.info(request.getMethod()+" "+request.getRequestURI());
        clubService.addClub(clubRequest);
        CommonResponse response = new CommonResponse(SUCCESS, HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 모임 상세 조회 api
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "모임 상세 조회", response = ClubResponseDto.class)
    public ResponseEntity<ResponseWithData> getClub(@PathVariable Long id, HttpServletRequest request) {
        logger.info(request.getMethod()+" "+request.getRequestURI());
        ClubResponseDto responseDto = clubService.getClub(id);
        ResponseWithData response = new ResponseWithData(SUCCESS,HttpStatus.OK.value(),responseDto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**
     * 모임 삭제 api
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "모임 삭제")
    public ResponseEntity<CommonResponse> deleteClub(@PathVariable Long id, HttpServletRequest request) {
        logger.info(request.getMethod()+" "+request.getRequestURI());
        clubService.deleteClub(id);
        CommonResponse response = new CommonResponse(SUCCESS, HttpStatus.OK.value());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**
     * 모임 수정 api
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "모임 변경")
    public  ResponseEntity<CommonResponse> modifyClub(
            @PathVariable Long id, @RequestBody ClubRequestDto newClubRequest, HttpServletRequest request) {
        logger.info(request.getMethod() + " " + request.getRequestURI());
        clubService.modifyClub(id, newClubRequest);
        CommonResponse response = new CommonResponse(SUCCESS, HttpStatus.OK.value());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
