package ru.paswd.nearprofinder.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import ru.paswd.nearprofinder.api.ApiManager;
import ru.paswd.nearprofinder.api.ApiRequest;
import ru.paswd.nearprofinder.api.OnJSONRequestBuilder;
import ru.paswd.nearprofinder.api.OnTaskCompleted;
import ru.paswd.nearprofinder.config.NPF;

public class Session {
    private Context context;
    private String sessionToken;
    private boolean validSession = false;
    private OnSessionInvalidListener invalidListener;
    //private SharedPreferences sPref;

    private static final String PREF_NAME = "session_token";

    public Session(Context ctx, OnSessionInvalidListener _invalidListener) {
        context = ctx;
        invalidListener = _invalidListener;
        loadSessionToken();

        if (isValidSession()) {
            check();
        }
    }

    public String getToken() {
        return sessionToken;
    }

    public boolean isValidSession() {
        return validSession;
    }

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private void loadSessionToken() {
        SharedPreferences sPref = getPreferences();
        sessionToken = sPref.getString(PREF_NAME, "");
        if (!sessionToken.isEmpty()) {
            validSession = true;
        }
    }

    private void saveSessionToken() {
        SharedPreferences sPref = getPreferences();

        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(PREF_NAME, sessionToken);
        editor.apply();
    }

    public void create(String _sessionToken) {
        if (_sessionToken.isEmpty()) {
            return;
        }
        sessionToken = _sessionToken;
        validSession = true;
        saveSessionToken();
    }

    public void destroy() {
        sessionToken = "";
        validSession = false;
        saveSessionToken();
    }

    public void check() {
        ApiManager apiManager = new ApiManager(context, new OnJSONRequestBuilder() {
            @Override
            public ApiRequest onCreate() {
                try {
                    String apiHref = NPF.Server.API.CHECK_SESSION;
                    JSONObject sendObject = new JSONObject();
                    sendObject.put("access_token", NPF.Server.ACCESS_TOKEN);
                    sendObject.put("session_token", sessionToken);

                    return new ApiRequest(sendObject, apiHref, false);
                } catch (JSONException ignored) {}

                return new ApiRequest(null, null, true);
            }
        }, new OnTaskCompleted() {
            @Override
            public void onCompleted(String res) {
                try {
                    JSONObject resJson = new JSONObject(res);
                    int status = resJson.getInt("status");
                    if (status == NPF.Server.API.Respond.OK) {
                        return;
                    }
                    destroy();
                    if (invalidListener != null) {
                        invalidListener.onInvalid();
                    }

//                    Toast toast = Toast.makeText(context,
//                            "Время сессии истекло", Toast.LENGTH_SHORT);
//                    toast.show();
                } catch (JSONException ignored) {
                }
            }
        });
        apiManager.execute(null, null);
    }
}
