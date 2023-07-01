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
import webProject.togetherPartyTonight.domain.member.dto.request.KakaoLoginRequestDto;
import webProject.togetherPartyTonight.domain.member.dto.response.LoginResponseDto;
import webProject.togetherPartyTonight.domain.member.oauth.kakao.KakaoLoginParam;
import webProject.togetherPartyTonight.domain.member.service.OAuthUserInfoService;
import webProject.togetherPartyTonight.domain.member.service.SocialLoginService;
import webProject.togetherPartyTonight.global.common.ResponseWithData;
import webProject.togetherPartyTonight.global.error.ErrorCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Api(tags = {"/members"})
@Slf4j
public class SocialLoginController {

    private final SocialLoginService socialLoginService;
    @PostMapping("/oauth/kakao/token")
    @ApiOperation(value = "카카오 로그인", notes = "카카오로그인 API 입니다.")
    public ResponseEntity<ResponseWithData> kakaoLogin(@RequestBody KakaoLoginRequestDto kakaoDto){
        try{
            LoginResponseDto response = socialLoginService.login(KakaoLoginParam.of(kakaoDto));
            return new ResponseEntity<>(new ResponseWithData("true",200, response),HttpStatus.OK);
        }catch (RestClientException e){
            return new ResponseEntity<>(new ResponseWithData("false",500,ErrorCode.INTERNAL_SERVER_ERROR.getErrorMessage()),HttpStatus.OK);
        }

    }
}
