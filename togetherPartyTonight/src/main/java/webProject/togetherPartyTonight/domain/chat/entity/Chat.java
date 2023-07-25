package webProject.togetherPartyTonight.domain.chat.entity;

import lombok.*;
import webProject.togetherPartyTonight.domain.chat.dto.ChatDto;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.global.common.BaseEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chat_room_id", referencedColumnName = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "send_member_id", referencedColumnName = "member_id")
    private Member sender;

    @Column(length = 500, nullable = false)
    private String chatMsg;

    public ChatDto toChatDto() {
        return ChatDto.builder()
                .chatId(getId())
                .message(getChatMsg())
                .senderMemberId(sender.getId())
                .senderNickname(sender.getNickname())
                .dateTime(getCreatedDate().toString())
                .build();
    }
}
