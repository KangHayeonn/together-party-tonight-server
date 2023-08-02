package webProject.togetherPartyTonight.domain.alert.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import webProject.togetherPartyTonight.domain.alert.dto.AlertListRequestDto;
import webProject.togetherPartyTonight.domain.alert.dto.AlertListResponseDto;
import webProject.togetherPartyTonight.domain.alert.dto.AlertUnreadCountDto;
import webProject.togetherPartyTonight.domain.alert.service.AlertService;
import webProject.togetherPartyTonight.global.common.response.SingleResponse;

import javax.servlet.http.HttpServletRequest;

@Api(tags = {"/alert"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/alert")
@Slf4j
public class AlertController {

    private AlertService alertService;

    @Autowired
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @ApiOperation(value = "알림 리스트 요청")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "멤버를 찾을 수 없습니다"),
            @ApiResponse(code =  500, message = "내부 서버 오류")
    })
    @PostMapping("")
    public SingleResponse<AlertListResponseDto> getAlertList(@RequestBody AlertListRequestDto alertListRequestDto, HttpServletRequest httpServletRequest) {
        return alertService.getAlertList(alertListRequestDto);
    }

    @ApiOperation(value = "알림 읽음 요청")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "알림 읽기에 성공하였습니다."),
            @ApiResponse(code = 400, message = "멤버를 찾을 수 없습니다\n알림이 없습니다\n이미 읽은 알림입니다"),
            @ApiResponse(code =  500, message = "내부 서버 오류")
    })
    @GetMapping("/read")
    public SingleResponse<String> readAlert(@ApiParam(value = "알림 아이디", required = true) @RequestParam long alertId, HttpServletRequest httpServletRequest) {
        return alertService.readAlert(alertId);
    }

    @ApiOperation(value = "알림 삭제 요청")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "알림 삭제에 성공하였습니다."),
            @ApiResponse(code = 400, message = "멤버를 찾을 수 없습니다\n알림이 없습니다\n이미 읽은 알림입니다"),
            @ApiResponse(code =  500, message = "내부 서버 오류")
    })
    @DeleteMapping("")
    public SingleResponse<String> deleteAlert(@ApiParam(value = "알림 아이디", required = true) @RequestParam long alertId, HttpServletRequest httpServletRequest) {
        return alertService.deleteAlert(alertId);
    }

    @ApiOperation(value = "안 읽은 알림 수 요청")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code =  500, message = "내부 서버 오류")
    })
    @GetMapping("/unreadCount")
    public SingleResponse<AlertUnreadCountDto> getAlertUnReadCount(HttpServletRequest httpServletRequest) {
        return alertService.getAlertCount();
    }
}
