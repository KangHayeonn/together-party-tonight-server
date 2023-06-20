package webProject.togetherPartyTonight.domain.club.info.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
public enum ApprovalState {
    APPROVE("approve"),
    PENDING ("pending"),
    REFUSE ("refuse"),
    KICKOUT("kickout");

    private String approvalState;


}
