package webProject.togetherPartyTonight.domain.chat.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.global.common.BaseEntity;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Setter
@Getter
@NoArgsConstructor
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

    @OneToMany(mappedBy = "chatRoom")
    private List<Chat> chats = new ArrayList<>();

    //chatMemberA 가 보는 채팅창 이름 . (초깃값 : chatMemberB 닉네임)
    @Column(length = 20, nullable = false)
    private String chatRoomAName;

    //chatMemberB 가 보는 채팅창 이름 . (초깃값 : chatMemberA 닉네임)
    @Column(length = 20, nullable = false)
    private String chatRoomBName;
}
