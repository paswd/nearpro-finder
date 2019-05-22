package ru.paswd.nearprofinder.api;

import org.json.JSONObject;

public class ApiRequest {
    private JSONObject sendingData;
    private String apiHref;
    private boolean error;

    public ApiRequest(JSONObject _sengingData, String _apiHref, boolean _isError) {
        sendingData = _sengingData;
        apiHref = _apiHref;
        error = _isError;
    }

    public JSONObject getSendingData() {
        return sendingData;
    }

    public String getApiHref() {
        return apiHref;
    }

    public boolean isError() {
        return error;
    }
}
