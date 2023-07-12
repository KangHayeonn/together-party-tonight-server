package webProject.togetherPartyTonight.domain.chat.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import webProject.togetherPartyTonight.domain.chat.dto.*;
import webProject.togetherPartyTonight.domain.chat.service.ChatService;
import webProject.togetherPartyTonight.global.websocket.WebSocketService;
import webProject.togetherPartyTonight.global.common.response.ListResponse;
import webProject.togetherPartyTonight.global.common.response.SingleResponse;

import javax.servlet.http.HttpServletRequest;


@Api(value = "/chat")
@SwaggerDefinition(tags = {@Tag(name = "/chat", description = "채팅 컨트롤러")})
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    private WebSocketService webSocketService;
    private ChatService chatService;

    @Autowired
    public ChatController(WebSocketService webSocketService, ChatService chatService) {
        this.webSocketService = webSocketService;
        this.chatService = chatService;
    }

    @ApiOperation(value = "채팅방 만들기")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 409, message = "이미 채팅방이 존재합니다\n채팅 회원이 존재하지 않습니다\n상대 채팅 회원이 존재하지 않습니다"),
            @ApiResponse(code = 500, message = "내부 서버 오류")
    })
    @PostMapping("/chatRoom")
    public SingleResponse<CreateChatRoomResponseDto> createChatRoom(@RequestBody CreateChatRoomRequest createChatRoomRequestDto, HttpServletRequest request) {
        requestLog(request);
        return chatService.createChatRoom(createChatRoomRequestDto);
    }

    @ApiOperation(value = "채팅방 목록 요청")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "채팅 회원이 존재하지 않습니다"),
            @ApiResponse(code = 409, message = "더 이상 채팅방이 없습니다"),
            @ApiResponse(code = 500, message = "내부 서버 오류")
    })
    @GetMapping("/chatRoom/list")
    public SingleResponse<ChatRoomListDto> getChatRoomList(@ApiParam(value = "페이지", required = true) @RequestParam int page,@ApiParam(value = "페이지당 수", required = true) @RequestParam int perPage, HttpServletRequest request) {
        requestLog(request);
        return chatService.getChatRoomList(page, perPage);
    }

    @ApiOperation(value = "채팅방 확인 요청, id = 0 이라면 해당 유저들 간의 채팅방이 없음")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "내부 서버 오류")
    })
    @GetMapping("/chatRoom")
    public SingleResponse<ChatRoomResponseDto> getChatRoom(@ApiParam(value = "상대 멤버 아이디", required = true) @RequestParam long otherMemberId, HttpServletRequest request) {
        requestLog(request);
        return chatService.getChatRoom(otherMemberId);
    }


    @ApiOperation(value = "채팅 목록 요청")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "채팅 요청 가능 수를 초과하였습니다"),
            @ApiResponse(code = 500, message = "내부 서버 오류")
    })
    @PostMapping("/chatLog")
    public SingleResponse<ChatListDto> getChatLog(@RequestBody ChatLogRequestDto chatLogRequestDto, HttpServletRequest request) {
        requestLog(request);
        return chatService.getChatLogList(chatLogRequestDto);
    }

    @ApiOperation(value = "채팅 발송 요청")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "채팅 최대 글자 수를 초과하였습니다"),
            @ApiResponse(code = 409, message = "채팅방이 존재하지 않습니다\n채팅 회원이 존재하지 않습니다"),
            @ApiResponse(code =  500, message = "내부 서버 오류")
    })
    @PostMapping("/")
    public SingleResponse<ChatSendResponseDto> sendChat(@RequestBody ChatSendRequestDto chatLogRequestDto, HttpServletRequest request) {
        requestLog(request);
        return chatService.sendChat(chatLogRequestDto);
    }

    private void requestLog(HttpServletRequest request) {
        log.debug("{} {}", request.getMethod(), request.getRequestURI());
    }
}
