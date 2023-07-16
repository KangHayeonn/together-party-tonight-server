package webProject.togetherPartyTonight.domain.member.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import webProject.togetherPartyTonight.domain.member.dto.request.MemberDetailsModifyDto;
import webProject.togetherPartyTonight.domain.member.dto.response.MemberModifyProfileImageDto;
import webProject.togetherPartyTonight.domain.member.dto.request.MemberNicknameModifyDto;
import webProject.togetherPartyTonight.domain.member.dto.request.PasswordChangeDto;
import webProject.togetherPartyTonight.domain.member.dto.response.MemberInfoResponseDto;
import webProject.togetherPartyTonight.domain.member.service.MemberService;
import webProject.togetherPartyTonight.global.common.ResponseWithData;
import webProject.togetherPartyTonight.global.common.response.CommonResponse;
import webProject.togetherPartyTonight.global.common.response.SingleResponse;
import webProject.togetherPartyTonight.global.common.service.ResponseService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

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


    @PutMapping("/nickname/{memberId}")
    @ApiOperation(value = "닉네임 변경", notes = "닉네임 변경 API입니다.")
    public SingleResponse<MemberNicknameModifyDto> modifyMemberInfo(@PathVariable Long memberId, @RequestBody MemberNicknameModifyDto memberInfoDto) {

        MemberNicknameModifyDto memberNicknameModifyDto = memberService.modifyMemberInfo(memberId, memberInfoDto);
        return responseService.getSingleResponse(memberNicknameModifyDto);
    }
    
    @PutMapping("/memberDetail/{memberId}")
    @ApiOperation(value = "자기소개 변경", notes = "자기소개 변경 API입니다.")
    public SingleResponse<MemberDetailsModifyDto> modifyMemberDetails(@PathVariable Long memberId, @RequestBody @Valid MemberDetailsModifyDto memberInfoDto) {

        MemberDetailsModifyDto memberDetailsModifyDto = memberService.modifyMemberDetails(memberId, memberInfoDto);
        return responseService.getSingleResponse(memberDetailsModifyDto);
    }

    @PutMapping("profileImages/{memberId}")
    @ApiOperation(value = "프로필 사진 변경", notes = "프로필 사진 변경 API입니다.")
    public SingleResponse<MemberModifyProfileImageDto> modifyMemberProfileImage(@PathVariable Long memberId,
                                                   @RequestPart(value = "profileImage",required = false) @ApiParam("프로필이미지") MultipartFile profileImage,
                                                   HttpServletRequest request) throws IOException {

        MemberModifyProfileImageDto memberModifyProfileImageDto = memberService.modifyMemberProfileImage(memberId, profileImage);

        return responseService.getSingleResponse(memberModifyProfileImageDto);
    }


    @PutMapping("/password/{memberId}")
    @ApiOperation(value = "비밀번호 변경", notes = "비밀번호 변경 API입니다.")
    public CommonResponse changePassword(@PathVariable Long memberId, @RequestBody PasswordChangeDto passwordChangeDto) {
        // memberId와 passwordChangeDto를 사용하여 비밀번호 변경 로직을 처리
        memberService.changePassword(memberId, passwordChangeDto);
        return responseService.getCommonResponse();
    }



    @DeleteMapping("{/memberId}")
    @ApiOperation(value = "회원탈퇴", notes = "회원탈퇴 API입니다.")
    public CommonResponse deleteMember(@PathVariable Long memberId){

        memberService.deleteMember(memberId);

        return responseService.getCommonResponse();
    }


}
