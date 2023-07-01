package webProject.togetherPartyTonight.domain.member.entity;

import lombok.*;

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.lang.Nullable;
import webProject.togetherPartyTonight.global.common.BaseEntity;
import webProject.togetherPartyTonight.domain.chat.entity.ChatRoom;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false,unique = true,name = "member_email")
    private String email;

    @Column(name = "member_password")
    private String password;

    @Column(nullable = false, name = "member_nickname")
    private String nickname;

    @Column(name = "member_review_count",columnDefinition = "int(11) default 0")
    private Integer reviewCount;

    @Column(name = "member_rating_avg", columnDefinition = "decimal(2,1) default 0.0")
    private Float ratingAvg;

    @Column(name = "member_details")
    private String details;

    @Column(name = "member_profile_image")
    private String profileImage;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "member_provider")
    private OAuthProvider oAuthProvider;

    @OneToMany(mappedBy = "chatMemberA", fetch = LAZY) // ChatRoom 의 member 필드와 매핑
    private List<ChatRoom> aChatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "chatMemberB", fetch = LAZY) // ChatRoom 의 member 필드와 매핑
    private List<ChatRoom> bChatRooms = new ArrayList<>();

    @Builder
    public Member(String email,String password,String nickname,Integer reviewCount,Float ratingAvg,String profileImage,OAuthProvider oAuthProvider){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.reviewCount = reviewCount;
        this.ratingAvg = ratingAvg;
        this.profileImage = profileImage;
        this.oAuthProvider = oAuthProvider;
    }

}
