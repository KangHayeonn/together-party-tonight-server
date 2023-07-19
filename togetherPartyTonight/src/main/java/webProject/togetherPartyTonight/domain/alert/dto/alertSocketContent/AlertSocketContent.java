package webProject.togetherPartyTonight.domain.alert.dto.alertSocketContent;

import lombok.Getter;
import lombok.Setter;


/**
 * 아래와 같은 데이터 구조를 만든다
 *  {
 *      "type" : "alert",
 *      "data" : {
 *          "alertType" : "",
 *          "alertData" : {
 *          }
 *      }
 *  }
 *
 */
@Getter
@Setter
public class AlertSocketContent<T> {
    String alertType;
    T alertData;
}
