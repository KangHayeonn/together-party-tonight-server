package webProject.togetherPartyTonight.domain.billing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import webProject.togetherPartyTonight.domain.billing.dto.*;
import webProject.togetherPartyTonight.domain.billing.service.BillingService;
import webProject.togetherPartyTonight.global.common.response.SingleResponse;

import javax.servlet.http.HttpServletRequest;

@Api(tags = {"/billing"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/billing")
@Slf4j
public class BillingController {

    BillingService billingService;

    @Autowired
    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @ApiOperation(value = "정산 요청")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "모임을 찾을 수 없습니다\n모임장만이 정산 요청을 할 수 있습니다"),
            @ApiResponse(code = 500, message = "내부 서버 오류")
    })
    @PostMapping("/")
    public SingleResponse<CreateBillingResponseDto> createBilling(@RequestBody CreateBillingRequestDto createBillingRequestDto , HttpServletRequest request) {
        return billingService.createBilling(createBillingRequestDto);
    }

    @ApiOperation(value = "모임 정산 내역 요청")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "모임을 찾을 수 없습니다\n클럽에 해당 멤버가 없습니다\n정산에 해당하는 정산내역이 없습니다"),
            @ApiResponse(code = 500, message = "내부 서버 오류")
    })
    @GetMapping("/club")
    public SingleResponse<ClubBillingHistoryResponseDto> getClubBilling(@RequestBody ClubBillingHistoryRequestDto clubBillingHistoryRequestDto, HttpServletRequest request) {
        return billingService.getClubBillingHistory(clubBillingHistoryRequestDto);
    }

    @ApiOperation(value = "나의 정산 내역 요청")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "내부 서버 오류")
    })
    @GetMapping("/myBilling")
    public SingleResponse<MyBillingHistoryResponseDto> getMyBilling(HttpServletRequest request) {
        return billingService.getMyBillingHistory();
    }

    @ApiOperation(value = "정산 결제 요청")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "정산에 해당하는 정산내역이 없습니다\n정산내역에 해당하는 멤버와 일치하지 않습니다\n이미 결제 완료한 내역입니다"),
            @ApiResponse(code = 500, message = "내부 서버 오류")
    })
    @PostMapping("/payment")
    public SingleResponse<BillingPaymentResponseDto> processPayment(BillingPaymentRequestDto billingPaymentRequestDto, HttpServletRequest request) {
        return billingService.processPayment(billingPaymentRequestDto);
    }
}
