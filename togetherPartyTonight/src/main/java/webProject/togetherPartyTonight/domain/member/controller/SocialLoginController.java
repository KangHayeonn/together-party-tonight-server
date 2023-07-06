package webProject.togetherPartyTonight.domain.member.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import webProject.togetherPartyTonight.domain.member.dto.request.GoogleLoginRequestDto;
import webProject.togetherPartyTonight.domain.member.dto.request.KakaoLoginRequestDto;
import webProject.togetherPartyTonight.domain.member.dto.request.NaverLoginRequestDto;
import webProject.togetherPartyTonight.domain.member.dto.response.LoginResponseDto;
import webProject.togetherPartyTonight.domain.member.oauth.google.GoogleLoginParam;
import webProject.togetherPartyTonight.domain.member.oauth.kakao.KakaoLoginParam;
import webProject.togetherPartyTonight.domain.member.oauth.naver.NaverLoginParam;
import webProject.togetherPartyTonight.domain.member.service.OAuthUserInfoService;
import webProject.togetherPartyTonight.domain.member.service.SocialLoginService;
import webProject.togetherPartyTonight.global.common.ResponseWithData;
import webProject.togetherPartyTonight.global.common.response.SingleResponse;
import webProject.togetherPartyTonight.global.common.service.ResponseService;
import webProject.togetherPartyTonight.global.error.ErrorCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Api(tags = {"/members"})
@Slf4j
public class SocialLoginController {

    private final SocialLoginService socialLoginService;
    private final ResponseService responseService;
    @PostMapping("/oauth/kakao/token")
    @ApiOperation(value = "카카오 로그인", notes = "카카오로그인 API 입니다.")
    public SingleResponse<LoginResponseDto> kakaoLogin(@RequestBody KakaoLoginRequestDto kakaoDto){

        LoginResponseDto responseDto = socialLoginService.login(KakaoLoginParam.of(kakaoDto));
        SingleResponse<LoginResponseDto> response = responseService.getSingleResponse(responseDto);
        return response;

    }

    @PostMapping("/oauth/naver/token")
    @ApiOperation(value = "네이버 로그인", notes = "네이버로그인 API입니다.")
    public SingleResponse<LoginResponseDto> naverLogin(@RequestBody NaverLoginRequestDto naverDto){

        LoginResponseDto responseDto = socialLoginService.login(NaverLoginParam.of(naverDto));
        SingleResponse<LoginResponseDto> response = responseService.getSingleResponse(responseDto);
        return response;
    }

    @PostMapping("/oauth/google/token")
    @ApiOperation(value = "구글 로그인", notes = "구글로그인 API입니다.")
    public SingleResponse<LoginResponseDto> googleLogin(@RequestBody GoogleLoginRequestDto googleDto){

        LoginResponseDto responseDto = socialLoginService.login(GoogleLoginParam.of(googleDto));
        SingleResponse<LoginResponseDto> response = responseService.getSingleResponse(responseDto);

        return response;
    }
}
