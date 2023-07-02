package webProject.togetherPartyTonight.domain.search.exception;

import lombok.Getter;
import webProject.togetherPartyTonight.global.error.CommonException;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
public class SearchException extends CommonException {
    public SearchException(ErrorInterface errorCode) {
        super(errorCode);
    }
}
