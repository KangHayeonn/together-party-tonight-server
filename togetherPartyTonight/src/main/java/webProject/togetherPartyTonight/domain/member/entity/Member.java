package webProject.togetherPartyTonight.domain.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webProject.togetherPartyTonight.domain.chat.entity.ChatRoom;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "chatMemberA") // ChatRoom 의 member 필드와 매핑
    private List<ChatRoom> aChatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "chatMemberB") // ChatRoom 의 member 필드와 매핑
    private List<ChatRoom> bChatRooms = new ArrayList<>();

}
