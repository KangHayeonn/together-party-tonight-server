package webProject.togetherPartyTonight.domain.comment.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geolatte.geom.M;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;
import webProject.togetherPartyTonight.domain.club.exception.ClubException;
import webProject.togetherPartyTonight.domain.comment.dto.request.CreateCommentRequestDto;
import webProject.togetherPartyTonight.domain.comment.dto.request.DeleteCommentRequestDto;
import webProject.togetherPartyTonight.domain.comment.dto.request.UpdateCommentRequestDto;
import webProject.togetherPartyTonight.domain.comment.dto.response.CreateCommentResponseDto;
import webProject.togetherPartyTonight.domain.comment.dto.response.DeleteCommentResponseDto;
import webProject.togetherPartyTonight.domain.comment.dto.response.GetCommentResponseDto;
import webProject.togetherPartyTonight.domain.comment.dto.response.UpdateCommentResponseDto;
import webProject.togetherPartyTonight.domain.comment.service.CommentService;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.exception.errorCode.MemberErrorCode;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.global.common.response.CommonResponse;
import webProject.togetherPartyTonight.global.common.response.ListResponse;
import webProject.togetherPartyTonight.global.common.response.SingleResponse;
import webProject.togetherPartyTonight.global.common.service.ResponseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"/comment"})
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final ResponseService responseService;
    private final MemberRepository memberRepository;


    @ApiOperation(value = "댓글 조회(입장)")
    @GetMapping(value = "/{clubId}")
    public ListResponse<GetCommentResponseDto> getCommentList (@ApiParam(required = true,example = "1") @PathVariable Long clubId, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();
        List<GetCommentResponseDto> commentList = commentService.getCommentList(clubId,member);
        return responseService.getListResponse(commentList);
    }



    @ApiOperation(value = "댓글 작성하기")
    @PostMapping(value = "")
    public SingleResponse<CreateCommentResponseDto> writeComment (@ApiParam(required = true) @RequestBody CreateCommentRequestDto requestDto, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();

        CreateCommentResponseDto res = commentService.writeComment(member, requestDto);
        return responseService.getSingleResponse(res);

    }

    @ApiOperation(value = "댓글 수정하기")
    @PutMapping(value = "")
    public SingleResponse<UpdateCommentResponseDto> updateComment (@ApiParam(required = true) @RequestBody UpdateCommentRequestDto requestDto, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();
        UpdateCommentResponseDto res = commentService.updateComment(member, requestDto);
        return responseService.getSingleResponse(res);
    }

    @ApiOperation(value = "댓글 삭제하기")
    @DeleteMapping(value = "")
    public SingleResponse<DeleteCommentResponseDto> deleteComment (@ApiParam(required = true) @RequestBody DeleteCommentRequestDto requestDto, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();
        DeleteCommentResponseDto res = commentService.deleteComment(member, requestDto);
        return responseService.getSingleResponse(res);
    }

    @ApiOperation(value="퇴장하기")
    @DeleteMapping(value = "/{clubId}")
    public CommonResponse exitFromClubPost (@ApiParam(required = true) @PathVariable Long clubId, HttpServletRequest request) {
        log.info("[{}] {}",request.getMethod(),request.getRequestURI());
        Member member = getMemberBySecurityContextHolder();
        commentService.exitFromClubPost(member, clubId);
        return responseService.getCommonResponse();
    }

    public Member getMemberBySecurityContextHolder() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principle.equals("anonymousUser")) {
            return null;
        } else {
            UserDetails userDetails = (UserDetails) principle;
            String username = userDetails.getUsername();
            return memberRepository.findById(Long.parseLong(username))
                    .orElseThrow(() -> {
                        throw new ClubException(MemberErrorCode.MEMBER_NOT_FOUND);
                    });
        }
    }
}
