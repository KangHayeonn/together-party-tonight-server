package webProject.togetherPartyTonight.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import webProject.togetherPartyTonight.domain.member.dto.LoginRequestDto;
import webProject.togetherPartyTonight.domain.member.dto.TokenDto;
import webProject.togetherPartyTonight.domain.member.dto.TokenWithIdDto;
import webProject.togetherPartyTonight.domain.member.service.AuthService;
import webProject.togetherPartyTonight.global.common.ResponseWithData;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class AuthController {


    private final AuthService authService;



    @PostMapping("/login")
    public ResponseWithData requestLogin(@RequestBody LoginRequestDto loginRequestDto){
        log.info("Dto정보 - {}",loginRequestDto.getEmail());
        TokenWithIdDto tokenWithIdDto = authService.login(loginRequestDto);

        return new ResponseWithData("true",200, tokenWithIdDto);
    }

    @PostMapping("/reissue")
    public ResponseWithData requestReissue(@RequestHeader("Authorization") String refreshToken){

        System.out.println(refreshToken);
        TokenDto tokenDto = authService.reissue(refreshToken.substring(7));

        return new ResponseWithData("true",200,tokenDto);
    }



}
