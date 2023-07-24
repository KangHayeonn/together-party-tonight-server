package webProject.togetherPartyTonight.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import webProject.togetherPartyTonight.domain.alert.service.AlertService;
import webProject.togetherPartyTonight.domain.billing.exception.BillingException;
import webProject.togetherPartyTonight.domain.chat.dto.*;
import webProject.togetherPartyTonight.domain.chat.entity.Chat;
import webProject.togetherPartyTonight.domain.chat.entity.ChatRoom;
import webProject.togetherPartyTonight.domain.chat.exception.ChatException;
import webProject.togetherPartyTonight.domain.chat.repository.ChatRepository;
import webProject.togetherPartyTonight.domain.chat.repository.ChatRoomRepository;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.global.common.BaseEntity;
import webProject.togetherPartyTonight.global.common.response.SingleResponse;
import webProject.togetherPartyTonight.global.common.service.ResponseService;
import webProject.togetherPartyTonight.global.websocket.WebSocketService;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static webProject.togetherPartyTonight.domain.billing.exception.BillingErrorCode.MEMBER_NOT_FOUND;
import static webProject.togetherPartyTonight.domain.chat.exception.ChatErrorCode.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private ChatRoomRepository chatRoomRepository;
    private MemberRepository memberRepository;
    private ResponseService responseService;
    private ChatRepository chatRepository;
    private WebSocketService webSocketService;
    private AlertService alertService;

    @Autowired
    public ChatService(ChatRoomRepository chatRoomRepository, MemberRepository memberRepository, ResponseService responseService, ChatRepository chatRepository, WebSocketService webSocketService, AlertService alertService) {
        this.chatRoomRepository = chatRoomRepository;
        this.memberRepository = memberRepository;
        this.responseService = responseService;
        this.chatRepository = chatRepository;
        this.webSocketService = webSocketService;
        this.alertService = alertService;
    }

    private static final int maxCount = 100;        //채팅 기록 최대 요청 수
    private static final int defaultSeq = -1;       //채팅 기록 조회 시 첫 조회 요청 seq
    private static final int maxChatMessageBytes = 500;     //db 설정 상 컬럼 최대 바이트

    //방 생성.
    public SingleResponse<CreateChatRoomResponseDto> createChatRoom(CreateChatRoomRequest createChatRoomRequestDto) {
        Member aMember = getMemberBySecurityContextHolder();
        Optional<ChatRoom> chatRoomByA = chatRoomRepository.findByChatMemberAIdAndChatMemberBId(aMember.getId(), createChatRoomRequestDto.getOtherMemberId());
        Optional<ChatRoom> chatRoomByB = chatRoomRepository.findByChatMemberAIdAndChatMemberBId(createChatRoomRequestDto.getOtherMemberId(), aMember.getId());

        if (chatRoomByA.isPresent() || chatRoomByB.isPresent()) {
            log.warn("[ChatService] createChatRoom chatRoom is Already Exist memberId: {}, otherMemberId: {}", aMember.getId(), createChatRoomRequestDto.getOtherMemberId());
            throw new ChatException(ALREADY_CHATROOM_EXIST);
        }

        Member bMember = memberRepository.findById(createChatRoomRequestDto.getOtherMemberId())
                .orElseThrow(() -> {
                    log.warn("[ChatService] createChatRoom otherMember doesn't exist otherMemberId: {}", createChatRoomRequestDto.getOtherMemberId());
                    throw new ChatException(OTHER_CHAT_MEMBER_NOT_FOUND);
                });

        ChatRoom chatRoom = ChatRoom.builder()
                .chatMemberA(aMember)
                .chatMemberB(bMember)
                .chatRoomAName(bMember.getNickname())
                .chatRoomBName(aMember.getNickname())
                .build();

        ChatRoom saveRoom = chatRoomRepository.save(chatRoom);

        CreateChatRoomResponseDto createChatRoomResponseDto = CreateChatRoomResponseDto.builder()
                .chatRoomId(saveRoom.getId())
                .build();

        return responseService.getSingleResponse(createChatRoomResponseDto);
    }

    //채팅방 목록을 불러온다  혹시 한 사람이 채팅 목록이 100개가 넘어거는 경우가 있다면 페이징 처리 필요.
    public SingleResponse<ChatRoomListDto> getChatRoomList() {

        Member member = getMemberBySecurityContextHolder();

        List<ChatRoom> chatRooms = member.getAChatRooms();
        chatRooms.addAll(member.getBChatRooms());

        chatRooms.sort(Comparator.comparing(BaseEntity::getModifiedDate));

        List<ChatRoomListResponseDto> chatRoomList = new ArrayList<>();

        chatRooms.forEach(chatRoom -> addChatRoomListResponseDto(member.getId(), chatRoomList, chatRoom));

        ChatRoomListDto chatRoomListDto = ChatRoomListDto.builder().chatRoomList(chatRoomList).build();

        return responseService.getSingleResponse(chatRoomListDto);
    }


    //채팅 목록 반환
    public SingleResponse<ChatListDto> getChatLogList(ChatLogRequestDto chatLogRequestDto) {

        if (chatLogRequestDto.getListCount() > maxCount) {
            log.info("[ChatService] getChatLogList listCount over MaxCount, chatRoomId: {}", chatLogRequestDto.getChatRoomId());
            throw new ChatException(OVER_MAX_LIST_COUNT);
        }

        //첫 요청일 경우에 가장 최신 목록을 가져와서 줌.  TodoSM 추후 캐싱처리 구현
        List<Chat> chats = getChats(chatLogRequestDto.getChatRoomId(), chatLogRequestDto.getLastChatSeq(), chatLogRequestDto.getListCount());

        List<ChatDto> chatDtoList = new ArrayList<>();

        chats.forEach(chat -> chatDtoList.add(chat.toChatDto()));

        ChatListDto chatListDto = ChatListDto.builder().chatList(chatDtoList).build();

        return responseService.getSingleResponse(chatListDto);
    }

    public SingleResponse<ChatSendResponseDto> sendChat(ChatSendRequestDto chatLogRequestDto) {

        Member sender = getMemberBySecurityContextHolder();

        ChatRoom chatRoom = chatRoomRepository.findById(chatLogRequestDto.getChatRoomId())
                .orElseThrow(() -> {
                    log.error("[ChatService] sendChat CHATROOM_NOT_FOUNT requestMemberId: {}, chatRoomId: {}", sender.getId(), chatLogRequestDto.getChatRoomId());
                    throw new ChatException(CHATROOM_NOT_FOUNT);
                });

        if (isMessageExceedsBytes(chatLogRequestDto.getChatMsg())) {
            log.error("[ChatService] sendChat OVER_MAX_LENGTH requestMemberId: {}, chatRoomId: {}", sender.getId(), chatLogRequestDto.getChatRoomId());
            throw new ChatException(OVER_MAX_LENGTH);
        }

        if (!chatRoom.isChatMember(sender)) {
            log.error("[ChatService] sendChat sender is not this chatRoom requestMemberId: {}, chatRoomId: {}", sender.getId(), chatLogRequestDto.getChatRoomId());
            throw new ChatException(CHAT_MEMBER_NOT_FOUND);
        }

        Chat chat = saveChat(chatLogRequestDto.getChatMsg(), chatRoom, sender);
        ChatSendResponseDto chatSendResponseDto = ChatSendResponseDto.builder()
                .chatId(chat.getId())
                .build();

        chatRoom.setModifiedDate(chat.getCreatedDate());
        ChatRoom saveChatRoom = chatRoomRepository.save(chatRoom);

        //소켓 메시지 발송.
        String message = ChatSocketMessage.getMessage(chatLogRequestDto.getChatMsg(), chat.getId(), sender, chatRoom);
        webSocketService.sendUser(chatRoom.getChatMemberA().getId(), message);
        webSocketService.sendUser(chatRoom.getChatMemberB().getId(), message);

        alertService.saveChattingAlertData(chat, saveChatRoom, sender);

        return responseService.getSingleResponse(chatSendResponseDto);
    }


    public SingleResponse<ChatRoomResponseDto> getChatRoom(long otherMemberId) {

        Member member = getMemberBySecurityContextHolder();
        ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto();
        Optional<ChatRoom> chatRoomByMemberId = chatRoomRepository.findByChatMemberAIdAndChatMemberBId(member.getId(), otherMemberId);
        if (chatRoomByMemberId.isPresent()) {
            chatRoomResponseDto = ChatRoomResponseDto.builder()
                    .chatRoomId(chatRoomByMemberId.get().getId())
                    .chatRoomName(chatRoomByMemberId.get().getChatRoomAName())
                    .build();
            return responseService.getSingleResponse(chatRoomResponseDto);
        }

        Optional<ChatRoom> chatRoomByOtherMemberId = chatRoomRepository.findByChatMemberAIdAndChatMemberBId(otherMemberId, member.getId());
        if (chatRoomByOtherMemberId.isPresent()) {
            chatRoomResponseDto = ChatRoomResponseDto.builder()
                    .chatRoomId(chatRoomByOtherMemberId.get().getId())
                    .chatRoomName(chatRoomByOtherMemberId.get().getChatRoomAName())
                    .build();
        }

        return responseService.getSingleResponse(chatRoomResponseDto);
    }

    public SingleResponse<ChatRoomResponseDto> renameChatRoom(ChatRoomRenameRequestDto chatRoomRenameRequestDto) {
        Member member = getMemberBySecurityContextHolder();

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomRenameRequestDto.getChatRoomId())
                .orElseThrow(() -> {
                    log.error("[ChatService] renameChatRoom CHATROOM_NOT_FOUNT requestMemberId: {}, chatRoomId: {}", member.getId(), chatRoomRenameRequestDto.getChatRoomId());
                    throw new ChatException(CHATROOM_NOT_FOUNT);
                });

        //채팅방 안에 존재하는 유저가 아니면 예외처리
        if (!chatRoom.isChatMember(member)) {
            log.error("[ChatService] renameChatRoom is not this chatRoom requestMemberId: {}, chatRoomId: {}", member.getId(), chatRoomRenameRequestDto.getChatRoomId());
            throw new ChatException(CHAT_MEMBER_NOT_FOUND);
        }

        chatRoom.rename(member, chatRoomRenameRequestDto.getChatRoomName());

        ChatRoom saveChatRoom = chatRoomRepository.save(chatRoom);

        ChatRoomResponseDto chatRoomResponseDto = ChatRoomResponseDto.builder()
                .chatRoomId(saveChatRoom.getId())
                .chatRoomName(saveChatRoom.getChatRoomName(member))
                .build();

        return responseService.getSingleResponse(chatRoomResponseDto);
    }

    public SingleResponse<String> leaveChatRoom(ChatRoomLeaveRequest chatRoomLeaveRequest) {

        Member member = getMemberBySecurityContextHolder();

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomLeaveRequest.getChatRoomId())
                .orElseThrow(() -> {
                    log.error("[ChatService] leaveChatRoom CHATROOM_NOT_FOUNT requestMemberId: {}, chatRoomId: {}", member.getId(), chatRoomLeaveRequest.getChatRoomId());
                    throw new ChatException(CHATROOM_NOT_FOUNT);
                });

        //채팅방 안에 존재하는 유저가 아니면 예외처리
        if (!chatRoom.isChatMember(member)) {
            log.error("[ChatService] leaveChatRoom is not this chatRoom requestMemberId: {}, chatRoomId: {}", member.getId(), chatRoomLeaveRequest.getChatRoomId());
            throw new ChatException(CHAT_MEMBER_NOT_FOUND);
        }

        alertService.saveLeaveChatRoomAlertDat(chatRoom, member);

        chatRepository.deleteByChatRoomId(chatRoom.getId());
        chatRoomRepository.delete(chatRoom);

        return responseService.getSingleResponse("채팅방 나가기에 성공하였습니다");
    }

    //--------------- 부 기능 함수
    private Chat saveChat(String chatMsg, ChatRoom chatRoom, Member sender) {
        Chat chat = Chat.builder()
                .sender(sender)
                .chatRoom(chatRoom)
                .chatMsg(chatMsg)
                .build();

        return chatRepository.save(chat);
    }

    private List<Chat> getChats(long chatRoomId, long latestSeq, int listCount) {
        Optional<List<Chat>> chats;
        Pageable pageable = PageRequest.of(0, listCount);
        if (isFirstPage(latestSeq)) {
            chats = chatRepository.findByChatRoomIdOrderByIdDesc(chatRoomId, pageable);
        } else {
            chats = chatRepository.findByChatRoomIdAndIdLessThanEqualOrderByIdDesc(chatRoomId, latestSeq, pageable);
        }
        return chats.orElse(new ArrayList<>());
    }

    private void addChatRoomListResponseDto(long memberId, List<ChatRoomListResponseDto> chatRoomListResponseListDto, ChatRoom chatRoom) {
        String chatRoomName = "";
        if (Objects.equals(chatRoom.getChatMemberA().getId(), memberId)) {
            chatRoomName = chatRoom.getChatRoomAName();
        } else if (Objects.equals(chatRoom.getChatMemberB().getId(), memberId)) {
            chatRoomName = chatRoom.getChatRoomBName();
        } else {
            //여기에 올 일 없음.
            log.error("[ChatService] getChatRoomList room is not available roomId: {}, memberId: {}", chatRoom.getId(), memberId);
            return;
        }

        //가장 최근 채팅 한개만 들어온다.
        List<Chat> chats = getChats(chatRoom.getId(), defaultSeq, 1);
        String chatMessage = "";
        String dateTimeString = "";

        if (chats.size() == 1) {
            chatMessage = chats.get(0).getChatMsg();
            dateTimeString = chats.get(0).getCreatedDate().toString();
        }

        ChatRoomListResponseDto chatRoomListResponseDto = ChatRoomListResponseDto.builder()
                .chatRoomName(chatRoomName)
                .chatRoomId(chatRoom.getId())
                .latestMessage(chatMessage)
                .dateTime(dateTimeString)
                .build();
        chatRoomListResponseListDto.add(chatRoomListResponseDto);
    }

    // Jwt 토큰으로 인증 완료된 Member 를 받아온다.
    private Member getMemberBySecurityContextHolder() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        return memberRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> {
                    log.error("[BillingService] getMemberBySecurityContextHolder member not found, memberId: {}", username);
                    throw new BillingException(MEMBER_NOT_FOUND);
                });
    }

    //------------- 조건 함수
    private boolean isFirstPage(long latestSeq) {
        return latestSeq == defaultSeq;
    }

    private boolean isMessageExceedsBytes(String chatMessage) {
        return chatMessage.getBytes(StandardCharsets.UTF_8).length >= maxChatMessageBytes;
    }
}
