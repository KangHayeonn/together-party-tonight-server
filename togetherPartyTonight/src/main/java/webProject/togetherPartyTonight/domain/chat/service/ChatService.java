package webProject.togetherPartyTonight.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import webProject.togetherPartyTonight.domain.chat.dto.*;
import webProject.togetherPartyTonight.domain.chat.entity.Chat;
import webProject.togetherPartyTonight.domain.chat.exception.ChatException;
import webProject.togetherPartyTonight.domain.chat.entity.ChatRoom;
import webProject.togetherPartyTonight.domain.chat.repository.ChatRepository;
import webProject.togetherPartyTonight.domain.chat.repository.ChatRoomRepository;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.global.common.BaseEntity;
import webProject.togetherPartyTonight.global.common.response.ListResponse;
import webProject.togetherPartyTonight.global.common.response.SingleResponse;
import webProject.togetherPartyTonight.global.common.service.ResponseService;
import webProject.togetherPartyTonight.global.websocket.WebSocketService;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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

    @Autowired
    public ChatService(ChatRoomRepository chatRoomRepository, MemberRepository memberRepository, ResponseService responseService, ChatRepository chatRepository, WebSocketService webSocketService) {
        this.chatRoomRepository = chatRoomRepository;
        this.memberRepository = memberRepository;
        this.responseService = responseService;
        this.chatRepository = chatRepository;
        this.webSocketService = webSocketService;
    }

    private static final int maxCount = 100;        //채팅 기록 최대 요청 수
    private static final int defaultSeq = -1;       //채팅 기록 조회 시 첫 조회 요청 seq
    private static final int maxChatMessageBytes = 500;     //db 설정 상 컬럼 최대 바이트

    //방 생성.
    public SingleResponse<CreateChatRoomResponseDto> createChatRoom(CreateChatRoomRequest createChatRoomRequestDto) {
        Optional<ChatRoom> chatRoomByA = chatRoomRepository.findByChatMemberAIdAndChatMemberBId(createChatRoomRequestDto.getMemberId(), createChatRoomRequestDto.getOtherMemberId());
        Optional<ChatRoom> chatRoomByB = chatRoomRepository.findByChatMemberAIdAndChatMemberBId(createChatRoomRequestDto.getOtherMemberId(), createChatRoomRequestDto.getMemberId());

        if (chatRoomByA.isPresent() || chatRoomByB.isPresent()) {
            log.warn("[ChatService] createChatRoom chatRoom is Already Exist memberId: {}, otherMemberId: {}", createChatRoomRequestDto.getMemberId(), createChatRoomRequestDto.getOtherMemberId());
            throw new ChatException(ALREADY_CHATROOM_EXIST);
        }

        Member aMember = memberRepository.findById(createChatRoomRequestDto.getMemberId())
                .orElseThrow(() -> {
                    log.warn("[ChatService] createChatRoom member doesn't exist memberId: {}", createChatRoomRequestDto.getMemberId());
                    throw new ChatException(CHAT_MEMBER_NOT_FOUND);
                });
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
    public SingleResponse<ChatRoomListDto> getChatRoomList(long memberId, int page, int perPage) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.error("[ChatService] getChatRoomList member doesn't exist memberId: {}", memberId);
                    throw new ChatException(CHAT_MEMBER_NOT_FOUND);
                });

        List<ChatRoom> chatRooms = member.getAChatRooms();
        chatRooms.addAll(member.getBChatRooms());

        chatRooms.sort(Comparator.comparing(BaseEntity::getModifiedDate));

        int startIdx = (page - 1) * perPage;
        if (chatRooms.size() <= startIdx) {
            throw new ChatException(OVER_CHAT_ROOM_PAGE);
        }

        chatRooms = chatRooms.subList(startIdx, startIdx + perPage);

        List<ChatRoomListResponseDto> chatRoomList = new ArrayList<>();

        chatRooms.forEach(chatRoom -> addChatRoomListResponseDto(memberId, chatRoomList, chatRoom));

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

        ChatRoom chatRoom = chatRoomRepository.findById(chatLogRequestDto.getChatRoomId())
                .orElseThrow(() -> {
                    log.error("[ChatService] sendChat CHATROOM_NOT_FOUNT requestMemberId: {}, chatRoomId: {}", chatLogRequestDto.getSenderMemberId(), chatLogRequestDto.getChatRoomId());
                    throw new ChatException(CHATROOM_NOT_FOUNT);
                });

        Member sender = memberRepository.findById(chatLogRequestDto.getSenderMemberId())
                .orElseThrow(() -> {
                    log.error("[ChatService] sendChat CHAT_MEMBER_NOT_FOUND requestMemberId: {}, chatRoomId: {}", chatLogRequestDto.getSenderMemberId(), chatLogRequestDto.getChatRoomId());
                    throw new ChatException(CHAT_MEMBER_NOT_FOUND);
                });

        if (isMessageExceedsBytes(chatLogRequestDto.getChatMsg())) {
            log.error("[ChatService] sendChat OVER_MAX_LENGTH requestMemberId: {}, chatRoomId: {}", chatLogRequestDto.getSenderMemberId(), chatLogRequestDto.getChatRoomId());
            throw new ChatException(OVER_MAX_LENGTH);
        }

        if (!chatRoom.isChatMember(sender)) {
            log.error("[ChatService] sendChat sender is not this chatRoom requestMemberId: {}, chatRoomId: {}", chatLogRequestDto.getSenderMemberId(), chatLogRequestDto.getChatRoomId());
            throw new ChatException(CHAT_MEMBER_NOT_FOUND);
        }

        Chat chat = saveChat(chatLogRequestDto.getChatMsg(), chatRoom, sender);
        ChatSendResponseDto chatSendResponseDto = ChatSendResponseDto.builder()
                .chatId(chat.getId())
                .build();

        chatRoom.setModifiedDate(chat.getCreatedDate());
        chatRoomRepository.save(chatRoom);

        //소켓 메시지 발송.
        String message = ChatSocketMessage.getMessage(chatLogRequestDto.getChatMsg(), chat.getId());
        webSocketService.sendUser(chatRoom.getChatMemberA().getId(), message);
        webSocketService.sendUser(chatRoom.getChatMemberB().getId(), message);

        return responseService.getSingleResponse(chatSendResponseDto);
    }


    public SingleResponse<ChatRoomResponseDto> getChatRoom(long memberId, long otherMemberId) {
        ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto();
        Optional<ChatRoom> chatRoomByMemberId = chatRoomRepository.findByChatMemberAIdAndChatMemberBId(memberId, otherMemberId);
        if (chatRoomByMemberId.isPresent()) {
            chatRoomResponseDto = ChatRoomResponseDto.builder()
                    .chatRoomId(chatRoomByMemberId.get().getId())
                    .chatRoomName(chatRoomByMemberId.get().getChatRoomAName())
                    .build();
            return responseService.getSingleResponse(chatRoomResponseDto);
        }

        Optional<ChatRoom> chatRoomByOtherMemberId = chatRoomRepository.findByChatMemberAIdAndChatMemberBId(otherMemberId,memberId );
        if (chatRoomByOtherMemberId.isPresent()) {
            chatRoomResponseDto = ChatRoomResponseDto.builder()
                    .chatRoomId(chatRoomByOtherMemberId.get().getId())
                    .chatRoomName(chatRoomByOtherMemberId.get().getChatRoomAName())
                    .build();
        }

        return responseService.getSingleResponse(chatRoomResponseDto);
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
            chats = chatRepository.findLatestChatsByRoomId(chatRoomId, pageable);
        } else {
            chats = chatRepository.findLatestChatsByRoomIdWithMaxChatId(chatRoomId, latestSeq, pageable);
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

        if (chats.size() == 1) {
            chatMessage = chats.get(0).getChatMsg();
        }

        ChatRoomListResponseDto chatRoomListResponseDto = ChatRoomListResponseDto.builder()
                .chatRoomName(chatRoomName)
                .chatRoomId(chatRoom.getId())
                .latestMessage(chatMessage)
                .build();
        chatRoomListResponseListDto.add(chatRoomListResponseDto);
    }

    //------------- 조건 함수
    private boolean isFirstPage(long latestSeq) {
        return latestSeq == defaultSeq;
    }

    private boolean isMessageExceedsBytes(String chatMessage) {
        return chatMessage.getBytes(StandardCharsets.UTF_8).length >= maxChatMessageBytes;
    }
}
