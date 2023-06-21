package webProject.togetherPartyTonight.domain.member.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import webProject.togetherPartyTonight.domain.member.dto.LoginRequestDto;
import webProject.togetherPartyTonight.domain.member.dto.RefreshTokenDto;
import webProject.togetherPartyTonight.domain.member.dto.TokenDto;
import webProject.togetherPartyTonight.domain.member.dto.TokenWithIdDto;
import webProject.togetherPartyTonight.domain.member.service.AuthService;
import webProject.togetherPartyTonight.global.common.ResponseWithData;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
@Api(tags = {"/members"})
public class AuthController {


    private final AuthService authService;



    @PostMapping("/login")
    @ApiOperation(value = "토큰 발급", notes = "JWT AccessToken, RefreshToken 을 발급한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "No param")
            //Other Http Status code..
    })
    @ApiImplicitParam(
            name = "token"
            , value = "카카오 엑세스 토큰"
            , defaultValue = "None")
    public ResponseWithData requestLogin(@RequestBody LoginRequestDto loginRequestDto){
        log.info("Dto정보 - {}",loginRequestDto.getEmail());
        TokenWithIdDto tokenWithIdDto = authService.login(loginRequestDto);

        return new ResponseWithData("true",200, tokenWithIdDto);
    }

    @PostMapping("/reissue")
    public ResponseWithData requestReissue(@RequestBody RefreshTokenDto refreshTokenDto){


        TokenDto tokenDto = authService.reissue(refreshTokenDto);

        return new ResponseWithData("true",200,tokenDto);
    }



}
