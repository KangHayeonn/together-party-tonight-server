package webProject.togetherPartyTonight.domain.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lombok.ToString;
import org.springframework.lang.Nullable;
import webProject.togetherPartyTonight.global.common.BaseEntity;
import webProject.togetherPartyTonight.domain.chat.entity.ChatRoom;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "chatMemberA") // ChatRoom 의 member 필드와 매핑
    private List<ChatRoom> aChatRooms = new ArrayList<>();


    @Column(nullable = false,unique = true)
    private String email;

    private String password;

    private String nickname;

    private int reviewCount;

    private String details;

    private Float ratingAvg;

    @OneToMany(mappedBy = "chatMemberB") // ChatRoom 의 member 필드와 매핑
    private List<ChatRoom> bChatRooms = new ArrayList<>();
}
