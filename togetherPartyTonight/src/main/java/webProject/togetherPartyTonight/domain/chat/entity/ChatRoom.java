package webProject.togetherPartyTonight.domain.chat.entity;

import lombok.*;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.global.common.BaseEntity;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chat_member_a_id", referencedColumnName = "member_id")
    private Member chatMemberA;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chat_member_b_id", referencedColumnName = "member_id")
    private Member chatMemberB;

    //chatMemberA 가 보는 채팅창 이름 . (초깃값 : chatMemberB 닉네임)
    @Column(name = "chat_room_a_name", length = 20, nullable = false)
    private String chatRoomAName;

    //chatMemberB 가 보는 채팅창 이름 . (초깃값 : chatMemberA 닉네임)
    @Column(name = "chat_room_b_name",length = 20, nullable = false)
    private String chatRoomBName;

    public Long getOtherMemberId(Member sender) {
        if (Objects.equals(chatMemberA.getId(), sender.getId())) {
            return chatMemberB.getId();
        }
        return chatMemberA.getId();
    }

    public Member getOtherMember(Member sender) {
        if (Objects.equals(chatMemberA.getId(), sender.getId())) {
            return chatMemberB;
        }
        return chatMemberA;
    }

    public Boolean isChatMember(Member sender) {
        if (Objects.equals(chatMemberA.getId(), sender.getId())) {
            return true;
        }
        return Objects.equals(chatMemberB.getId(), sender.getId());
    }
}
