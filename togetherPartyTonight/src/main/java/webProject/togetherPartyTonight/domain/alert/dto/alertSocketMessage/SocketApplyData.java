package webProject.togetherPartyTonight.domain.alert.dto.alertSocketMessage;


import com.google.gson.Gson;
import lombok.Getter;
import webProject.togetherPartyTonight.domain.alert.dto.alertSocketContent.AlertDataSocketContent;
import webProject.togetherPartyTonight.domain.alert.dto.alertSocketContent.AlertSocketContent;
import webProject.togetherPartyTonight.domain.alert.entity.Alert;
import webProject.togetherPartyTonight.global.websocket.socketMessage.CommonSocketResponseMessage;

@Getter
public class SocketApplyData {

    public static final String socketType = "alert";

    public static String getMessage(AlertDataSocketContent alertApplySocketContent, String alertType, Alert alert) {
        CommonSocketResponseMessage<AlertSocketContent<AlertDataSocketContent>> responseMessage = new CommonSocketResponseMessage<>();
        AlertSocketContent<AlertDataSocketContent> alertSocketContent = new AlertSocketContent<>();
        alertSocketContent.setAlertType(alertType);
        alertSocketContent.setAlertId(alert.getId());
        alertSocketContent.setAlertData(alertApplySocketContent);
        alertSocketContent.setCreatedDate(alert.getCreatedDate().toString());
        alertSocketContent.setModifiedDate(alert.getModifiedDate().toString());
        responseMessage.setType(socketType);
        responseMessage.setData(alertSocketContent);
        return new Gson().toJson(responseMessage);
    }
}
