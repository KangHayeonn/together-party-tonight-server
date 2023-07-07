package webProject.togetherPartyTonight.domain.member.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import webProject.togetherPartyTonight.domain.member.auth.MemberDetails;
import webProject.togetherPartyTonight.domain.member.dto.response.MemberInfoResponseDto;
import webProject.togetherPartyTonight.domain.member.service.MemberService;
import webProject.togetherPartyTonight.global.common.ResponseWithData;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
@Api(tags = {"/members"})
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/{userId}")
    @ApiOperation(value = "회원 정보 조회", notes = "회원 정보 조회 API 입니다.")
    public ResponseEntity<ResponseWithData> requestInfo(@PathVariable Long userId){

        MemberInfoResponseDto memberInfoResponseDto = memberService.findById(userId);

        return new ResponseEntity<>(new ResponseWithData("true",200, memberInfoResponseDto),HttpStatus.OK);

    }

}
