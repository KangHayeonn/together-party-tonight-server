package webProject.togetherPartyTonight.domain.alert.dto.alertSocketContent;

import lombok.Getter;
import lombok.Setter;


/**
 * 아래와 같은 데이터 구조를 만든다
 *  {
 *      "type" : "alert",
 *      "data" : {
 *          "alertType" : "",
 *          "alertId" : 1,
 *          "alertData" : {
 *          }
 *      },
 *      "alertIsRead" : false,
 *      "createdDate" : "2023-07-16T17:31:43.490118",
 *      "modifiedDate" : "2023-07-16T17:31:43.490118"
 *  }
 *
 */
@Getter
@Setter
public class AlertSocketContent<T> {
    String alertType;
    Long alertId;       //알림 아이디
    T alertData;
    Boolean alertIsRead;    //알림 읽은 여부
    String createdDate;
    String modifiedDate;
}
