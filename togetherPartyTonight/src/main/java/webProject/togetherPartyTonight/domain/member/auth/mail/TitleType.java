package webProject.togetherPartyTonight.domain.member.auth.mail;


import lombok.Getter;

@Getter
public enum TitleType {
    SIGN_UP("[투바투] 회원가입을 위한 이메일 인증입니다."),
    FOR_FIND_PASSWORD("[투바투] 비밀번호 변경에 대한 인증입니다.");

    private String type;

    TitleType(String type){
        this.type = type;
    }

}
