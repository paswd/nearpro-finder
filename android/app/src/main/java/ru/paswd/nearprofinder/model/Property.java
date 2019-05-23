package ru.paswd.nearprofinder.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.paswd.nearprofinder.PropertyItem;
import ru.paswd.nearprofinder.api.ApiManager;
import ru.paswd.nearprofinder.api.ApiRequest;
import ru.paswd.nearprofinder.api.OnJSONRequestBuilder;
import ru.paswd.nearprofinder.api.OnTaskCompleted;
import ru.paswd.nearprofinder.config.NPF;

public class Property {

    private String propertyListJsonStr;
    private Context context;

    private PropertyFilter filter;
    private GeoPoints geoPoints;

    private OnPropertyUpdateListener listener;
    private Session session;

    private static final String PREF_PROPERTY_LIST = "property_list";

    public Property(Context ctx, OnPropertyUpdateListener _listener) {
        context = ctx;
        filter = new PropertyFilter(context);
        listener = _listener;
        session = new Session(context, null);
        geoPoints = new GeoPoints(context);
        loadLocal();
    }

    public void saveLocal() {
        SharedPreferences sPref = getPreferences();

        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(PREF_PROPERTY_LIST, propertyListJsonStr);
        editor.apply();
    }
    public void loadLocal() {
        SharedPreferences sPref = getPreferences();
        propertyListJsonStr = sPref.getString(PREF_PROPERTY_LIST, "");
    }

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void update() {
        ApiManager apiManager = new ApiManager(context, new OnJSONRequestBuilder() {
            @Override
            public ApiRequest onCreate() {
                try {
                    String apiHref = NPF.Server.API.GET_PROPERTY;
                    JSONObject sendObject = new JSONObject();
                    sendObject.put("access_token", NPF.Server.ACCESS_TOKEN);
                    sendObject.put("session_token", session.getToken());

                    LatLng optimalPoint = geoPoints.getOptimalPoint();

                    if (!geoPoints.isEmpty()) {
                        sendObject.put("is_locality", 1);
                        sendObject.put("lat", optimalPoint.latitude);
                        sendObject.put("lng", optimalPoint.longitude);
                        //todo radius
                        sendObject.put("radius", 5000.);
                    }
                    if (filter.getCountry() > 0) {
                        sendObject.put("country", filter.getCountry());
                    }
                    if (filter.getRegion() > 0) {
                        sendObject.put("region", filter.getRegion());
                    }
                    if (filter.getType() > 0) {
                        sendObject.put("type", filter.getType());
                    }
                    if (filter.getPriceMin() > 0) {
                        sendObject.put("price_min", filter.getPriceMin());
                    }
                    if (filter.getPriceMax() > 0) {
                        sendObject.put("price_max", filter.getPriceMax());
                    }

                    return new ApiRequest(sendObject, apiHref, false);
                } catch (JSONException ignored) {}

                return new ApiRequest(null, null, true);
            }
        }, new OnTaskCompleted() {
            @Override
            public void onCompleted(String res) {
                propertyListJsonStr = res;
                saveLocal();
                listener.onUpdate(getListFromJSON());
            }
        });
        apiManager.execute(null, null);
    }

    private List<PropertyItem> getListFromJSON() {
        List<PropertyItem> res = new ArrayList<>();
        try {
            JSONObject resJson = new JSONObject(propertyListJsonStr);
            int status = resJson.getInt("status");
            if (status == NPF.Server.API.Respond.OK) {
                JSONArray data = resJson.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject item = data.getJSONObject(i);
                    int id = item.getInt("id");
                    String name = item.getString("name");
                    int price = item.getInt("price");
                    String address = item.getString("address");
                    String desc = item.getString("description");
                    String imageSrc = item.getString("img_src");
                    res.add(new PropertyItem(id, name, desc, address, price, imageSrc));
                }
                //saveLocal();
                return res;
            }

            int errorNum = resJson.getInt("error_num");

            String msg;

            switch (errorNum) {
                case NPF.Server.API.Respond.Errors.NoNetworkConnection.CODE:
                    msg = NPF.Server.API.Respond.Errors.NoNetworkConnection.MESSAGE;
                    break;

                case NPF.Server.API.Respond.Errors.AccessDenied.CODE:
                    msg = NPF.Server.API.Respond.Errors.AccessDenied.MESSAGE;
                    break;

                case NPF.Server.API.Respond.Errors.SqlError.CODE:
                    msg = NPF.Server.API.Respond.Errors.SqlError.MESSAGE;
                    break;

                default:
                    msg = "Неизвестная ошибка";
                    break;

            }
            Toast toast = Toast.makeText(context,
                    msg, Toast.LENGTH_SHORT);
            toast.show();
        } catch (JSONException ignored) {}

        return res;
    }
}
