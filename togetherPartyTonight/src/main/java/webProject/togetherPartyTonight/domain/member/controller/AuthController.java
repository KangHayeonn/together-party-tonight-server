package webProject.togetherPartyTonight.domain.member.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import webProject.togetherPartyTonight.domain.member.auth.mail.MailService;
import webProject.togetherPartyTonight.domain.member.auth.mail.TitleType;
import webProject.togetherPartyTonight.domain.member.dto.request.*;

import org.springframework.web.bind.annotation.*;
import webProject.togetherPartyTonight.domain.member.dto.request.LoginRequestDto;
import webProject.togetherPartyTonight.domain.member.dto.request.ReissueRequestDto;
import webProject.togetherPartyTonight.domain.member.dto.request.SignupRequestDto;

import webProject.togetherPartyTonight.domain.member.dto.response.LoginResponseDto;
import webProject.togetherPartyTonight.domain.member.dto.response.ReissueResponseDto;
import webProject.togetherPartyTonight.domain.member.service.AuthService;
import webProject.togetherPartyTonight.global.common.ResponseWithData;
import webProject.togetherPartyTonight.global.common.response.CommonResponse;

import webProject.togetherPartyTonight.global.common.response.SingleResponse;
import webProject.togetherPartyTonight.global.common.service.ResponseService;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
@Api(tags = {"/members"})
public class AuthController {

    private final AuthService authService;
    

    private final ResponseService responseService;

    private final MailService mailService;

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입", notes = "회원가입 API 입니다.")
    public CommonResponse requestSignup(@RequestBody @Valid SignupRequestDto signupRequestDto){

        authService.signup(signupRequestDto);

        return responseService.getCommonResponse();
    }

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

    @PostMapping("/signup/email")
    @ApiOperation(value = "회원가입 이메일 인증번호 메일링", notes = "회원가입 시 인증번호 메일링 API입니다.")
    public CommonResponse authEmailForSignup(@RequestBody @Valid EmailAuthRequestDto authRequestDto){
        mailService.sendEmailForEmailAuth(authRequestDto.getEmail(), TitleType.SIGN_UP.getType());

        return responseService.getCommonResponse();
    }



    @PostMapping("/password/email")
    @ApiOperation(value = "비밀번호 찾기 시 인증번호 메일링", notes = "비밀번호 찾기 메일링 API입니다.")
    public CommonResponse authEmailForFindPassword(@RequestBody @Valid EmailAuthRequestDto authRequestDto){

        mailService.sendEmailForEmailAuth(authRequestDto.getEmail(), TitleType.FOR_FIND_PASSWORD.getType());

        return responseService.getCommonResponse();
    }

    @PostMapping("/password/reset")
    @ApiOperation(value = "비밀번호 찾기 시 인증번호 인증과 비밀번호 변경", notes = "비밀번호 찾기 시 인증번호 인증과 비밀번호 변경 API입니다.")
    public CommonResponse resetPassword(@RequestBody @Valid PasswordResetRequestDto resetRequestDto){

        authService.resetPassword(resetRequestDto);

        return responseService.getCommonResponse();
    }

    @PostMapping("/emailCheck")
    @ApiOperation(value = "이메일 중복체크", notes = "이메일 중복체크 API 입니다.")
    public CommonResponse requestCheckEmailDuplicate(@RequestBody @Valid EmailCheckRequestDto emailCheckRequestDto){
        authService.checkDuplicateEmail(emailCheckRequestDto);
        return responseService.getCommonResponse();
    }

    @GetMapping("/logout/{userId}")
    @ApiOperation(value = "로그아웃", notes = "로그아웃 API입니다.")
    public CommonResponse logout(@PathVariable Long userId){
        authService.logout(userId);
        return responseService.getCommonResponse();
    }



}
