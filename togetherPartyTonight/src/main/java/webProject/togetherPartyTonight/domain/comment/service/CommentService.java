package webProject.togetherPartyTonight.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import webProject.togetherPartyTonight.domain.club.repository.ClubRepository;
import webProject.togetherPartyTonight.domain.comment.dto.request.CreateCommentRequestDto;
import webProject.togetherPartyTonight.domain.comment.dto.request.DeleteCommentRequestDto;
import webProject.togetherPartyTonight.domain.comment.dto.request.UpdateCommentRequestDto;
import webProject.togetherPartyTonight.domain.comment.dto.response.CreateCommentResponseDto;
import webProject.togetherPartyTonight.domain.comment.dto.response.DeleteCommentResponseDto;
import webProject.togetherPartyTonight.domain.comment.dto.response.GetCommentResponseDto;
import webProject.togetherPartyTonight.domain.comment.dto.response.UpdateCommentResponseDto;
import webProject.togetherPartyTonight.domain.comment.entity.Comment;
import webProject.togetherPartyTonight.domain.comment.exception.CommentErrorCode;
import webProject.togetherPartyTonight.domain.comment.exception.CommentException;
import webProject.togetherPartyTonight.domain.comment.repository.CommentRepository;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.global.error.ErrorCode;
import webProject.togetherPartyTonight.global.websocket.WebSocketService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ClubRepository clubRepository;
    private final WebSocketService webSocketService;

    public List<GetCommentResponseDto> getCommentList(Long clubId, Member member) {
        Optional<List<Comment>> commentList = commentRepository.findByClubClubId(clubId);
        List<GetCommentResponseDto> res = new ArrayList<>();
        if (commentList.isPresent()) {
            for (Comment c : commentList.get()) {
                res.add(new GetCommentResponseDto().toDto(c));
            }
        }
        //소켓에 세션 저장
        webSocketService.addMemberToCommentSession(clubId,member);
        return res;
    }

    public CreateCommentResponseDto writeComment (Member member, CreateCommentRequestDto requestDto){

        Comment comment = Comment.builder()
                .club(clubRepository.getReferenceById(requestDto.getClubId()))
                .commentContent(requestDto.getCommentContent())
                .member(member)
                .build();
        Comment save = commentRepository.save(comment);
        CreateCommentResponseDto responseDto = new CreateCommentResponseDto().toDto(save);

        //웹소켓 발송
        String message = responseDto.toSocketMessage();
        webSocketService.broadcastComment(message, requestDto.getClubId());

        return responseDto;

    }

    @Transactional
    public UpdateCommentResponseDto updateComment (Member member, UpdateCommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(requestDto.getCommentId()).orElseThrow(
                () -> new CommentException(CommentErrorCode.INVALID_COMMENT_ID)
        );
        comment.setCommentContent(requestDto.getCommentContent());

        UpdateCommentResponseDto responseDto = new UpdateCommentResponseDto().toDto(comment);
        String message = responseDto.toSocketMessage();

        //웹소켓 발송
        webSocketService.broadcastComment(message,comment.getClub().getClubId());

        return responseDto;

    }

    @Transactional
    public DeleteCommentResponseDto deleteComment (Member member, DeleteCommentRequestDto requestDto) {
        Long commentId = requestDto.getCommentId();
        Comment reference = commentRepository.getReferenceById(commentId);
        if (reference==null) throw new CommentException(CommentErrorCode.INVALID_COMMENT_ID);
        else if(!Objects.equals(reference.getMember().getId(), member.getId())) throw new CommentException(ErrorCode.FORBIDDEN);

        commentRepository.deleteById(commentId);

        DeleteCommentResponseDto responseDto = new DeleteCommentResponseDto().toDto(requestDto.getCommentId());
        String message = responseDto.toSocketMessage();

        webSocketService.broadcastComment(message, reference.getClub().getClubId());
        return responseDto;
    }

    public void exitFromClubPost ( Member member, Long clubId) {
        webSocketService.deleteMemberFromCommentSession(clubId, member);
    }

}
