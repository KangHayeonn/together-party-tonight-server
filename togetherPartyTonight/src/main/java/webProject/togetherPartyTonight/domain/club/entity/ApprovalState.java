package webProject.togetherPartyTonight.domain.club.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ApprovalState {
    ALL("all"),
    APPROVE("approve"),
    PENDING ("pending"),
    REFUSE ("refuse"),
    KICKOUT("kickout");

    private String approvalState;


}
