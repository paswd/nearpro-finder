package ru.paswd.nearprofinder.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ru.paswd.nearprofinder.PropertyItem;
import ru.paswd.nearprofinder.api.ApiManager;
import ru.paswd.nearprofinder.api.ApiRequest;
import ru.paswd.nearprofinder.api.OnJSONRequestBuilder;
import ru.paswd.nearprofinder.api.OnTaskCompleted;
import ru.paswd.nearprofinder.config.NPF;

public class Property {

//    private String propertyListJsonStr;
//    private Context context;
//
//    private PropertyFilter filter;
//
//    private OnPropertyUpdateListener listener;
//    private Session session;
//
//    private static final String PREF_PROPERTY_LIST = "property_list";
//
//    public Property(Context ctx, OnPropertyUpdateListener _listener) {
//        context = ctx;
//        filter = new PropertyFilter(context);
//        listener = _listener;
//        session = new Session(context, null);
//    }
//
//    public void saveLocal() {
//        SharedPreferences sPref = getPreferences();
//
//        SharedPreferences.Editor editor = sPref.edit();
//        editor.putString(PREF_PROPERTY_LIST, propertyListJsonStr);
//        editor.apply();
//    }
//    public void loadLocal() {
//        SharedPreferences sPref = getPreferences();
//        propertyListJsonStr = sPref.getString(PREF_PROPERTY_LIST, "");
//    }
//
//    private SharedPreferences getPreferences() {
//        return PreferenceManager.getDefaultSharedPreferences(context);
//    }
//
//    public void update() {
//        ApiManager apiManager = new ApiManager(context, new OnJSONRequestBuilder() {
//            @Override
//            public ApiRequest onCreate() {
//                try {
//                    String apiHref = NPF.Server.API.GET_REGIONS;
//                    JSONObject sendObject = new JSONObject();
//                    sendObject.put("access_token", NPF.Server.ACCESS_TOKEN);
//                    sendObject.put("session_token", session.getToken());
//                    return new ApiRequest(sendObject, apiHref, false);
//                } catch (JSONException ignored) {}
//
//                return new ApiRequest(null, null, true);
//            }
//        }, new OnTaskCompleted() {
//            @Override
//            public void onCompleted(String res) {
//                propertyListJsonStr = res;
//                listener.onUpdate(getListFromJSON());
//            }
//        });
//        apiManager.execute(null, null);
//    }
//
//    private List<PropertyItem> getListFromJSON() {
//
//    }
}
