package webProject.togetherPartyTonight.global.common.service;

import org.springframework.stereotype.Service;
import webProject.togetherPartyTonight.global.common.response.CommonResponse;
import webProject.togetherPartyTonight.global.common.response.FailureResponse;
import webProject.togetherPartyTonight.global.common.response.ListResponse;
import webProject.togetherPartyTonight.global.common.response.SingleResponse;

import java.util.List;

@Service
public class ResponseService {

    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    public <T> SingleResponse<T> getSingleResponse(T data) {
        SingleResponse<T> singleResponse = new SingleResponse<>();
        singleResponse.setData(data);
        setSuccessResponse(singleResponse);
        return singleResponse;
    }

    public <T> ListResponse<T> getListResponse(List<T> dataList) {
        ListResponse<T> listResponse = new ListResponse<>();
        listResponse.setDataList(dataList);
        setSuccessResponse(listResponse);
        return listResponse;
    }

    public FailureResponse getFailureResponse(int code, String errorMessage) {
        return new FailureResponse(FAIL, code, errorMessage);
    }

    //----------

    public void setSuccessResponse(CommonResponse commonResponse) {
        commonResponse.setSuccess(SUCCESS);
        commonResponse.setCode(200);
    }
}
