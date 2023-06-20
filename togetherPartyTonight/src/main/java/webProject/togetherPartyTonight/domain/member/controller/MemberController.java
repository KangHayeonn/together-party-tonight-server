package webProject.togetherPartyTonight.domain.member.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webProject.togetherPartyTonight.domain.member.dto.MemberInfoDto;
import webProject.togetherPartyTonight.domain.member.service.MemberService;
import webProject.togetherPartyTonight.global.common.ResponseWithData;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{userId}")
    public ResponseWithData requestInfo(@PathVariable Long userId){

        MemberInfoDto memberInfoDto = memberService.findById(userId);

        return new ResponseWithData("true",200,memberInfoDto);
    }
}
