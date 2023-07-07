package webProject.togetherPartyTonight.domain.member.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webProject.togetherPartyTonight.domain.member.dto.request.LoginRequestDto;
import webProject.togetherPartyTonight.domain.member.dto.request.ReissueRequestDto;
import webProject.togetherPartyTonight.domain.member.dto.request.SignupRequestDto;
import webProject.togetherPartyTonight.domain.member.dto.response.LoginResponseDto;
import webProject.togetherPartyTonight.domain.member.dto.response.ReissueResponseDto;
import webProject.togetherPartyTonight.domain.member.service.AuthService;
import webProject.togetherPartyTonight.global.common.ResponseWithData;
import webProject.togetherPartyTonight.global.common.response.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
@Api(tags = {"/members"})
public class AuthController {

    private final AuthService authService;
    

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인 API 입니다.")
    public ResponseWithData requestLogin(@RequestBody LoginRequestDto loginRequestDto){

        log.info("Dto정보 - {}",loginRequestDto.getEmail());

        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);

        return new ResponseWithData("true",200, loginResponseDto);
    }

    @PostMapping("/reissue")
    @ApiOperation(value = "토큰 재발급", notes = "토큰 재발급 API 입니다.")
    public ResponseWithData requestReissue(@RequestBody ReissueRequestDto reissueRequestDto){

        ReissueResponseDto reissueResponseDto = authService.reissue(reissueRequestDto);

        return new ResponseWithData("true",200,reissueResponseDto);
    }



}
