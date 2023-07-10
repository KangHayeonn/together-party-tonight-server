package webProject.togetherPartyTonight.domain.member.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import webProject.togetherPartyTonight.domain.member.auth.MemberDetails;
import webProject.togetherPartyTonight.domain.member.dto.request.MemberInfoModifyDto;
import webProject.togetherPartyTonight.domain.member.dto.request.PasswordChangeDto;
import webProject.togetherPartyTonight.domain.member.dto.response.MemberInfoResponseDto;
import webProject.togetherPartyTonight.domain.member.service.MemberService;
import webProject.togetherPartyTonight.global.common.ResponseWithData;
import webProject.togetherPartyTonight.global.common.response.CommonResponse;
import webProject.togetherPartyTonight.global.common.service.ResponseService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
@Api(tags = {"/members"})
public class MemberController {

    private final MemberService memberService;

    private final ResponseService responseService;


    @GetMapping("/{userId}")
    @ApiOperation(value = "회원 정보 조회", notes = "회원 정보 조회 API 입니다.")
    public ResponseEntity<ResponseWithData> requestInfo(@PathVariable Long userId){

        MemberInfoResponseDto memberInfoResponseDto = memberService.findById(userId);

        return new ResponseEntity<>(new ResponseWithData("true",200, memberInfoResponseDto),HttpStatus.OK);
    }


    @PutMapping("/{memberId}")
    @ApiOperation(value = "유저정보 변경", notes = "유저정보 변경 API입니다.")
    public CommonResponse modifyMemberInfo(@PathVariable Long memberId, @RequestBody MemberInfoModifyDto memberInfoDto) {
        // memberId와 memberInfoDto를 사용하여 회원 정보 수정 로직을 처리
        memberService.modifyMemberInfo(memberId,memberInfoDto);
        return responseService.getCommonResponse();
    }

    @PutMapping("/password/{memberId}")
    @ApiOperation(value = "비밀번호 변경", notes = "비밀번호 변경 API입니다.")
    public CommonResponse changePassword(@PathVariable Long memberId, @RequestBody PasswordChangeDto passwordChangeDto) {
        // memberId와 passwordChangeDto를 사용하여 비밀번호 변경 로직을 처리
        memberService.changePassword(memberId, passwordChangeDto);
        return responseService.getCommonResponse();
    }


}
