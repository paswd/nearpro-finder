package ru.paswd.nearprofinder;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ru.paswd.nearprofinder.api.ApiManager;
import ru.paswd.nearprofinder.api.ApiRequest;
import ru.paswd.nearprofinder.api.OnJSONRequestBuilder;
import ru.paswd.nearprofinder.api.OnTaskCompleted;
import ru.paswd.nearprofinder.config.NPF;
import ru.paswd.nearprofinder.model.Session;

public class SettingsFragment extends Fragment {
    private BottomNavigationView nav;
    private View view;
    private Session session;

    public void setNavigation(BottomNavigationView navigation) {
        nav = navigation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        session = new Session(getActivity(), null);
        view = inflater.inflate(R.layout.fragment_settings, null);
        Button buttonExit = view.findViewById(R.id.buttonExit);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiManager apiManager = new ApiManager(getActivity(), new OnJSONRequestBuilder() {
                    @Override
                    public ApiRequest onCreate() {
                        try {
                            String apiHref = NPF.Server.API.LOGOUT;
                            JSONObject sendObject = new JSONObject();
                            sendObject.put("access_token", NPF.Server.ACCESS_TOKEN);
                            sendObject.put("session_token", session.getToken());

                            return new ApiRequest(sendObject, apiHref, false);
                        } catch (JSONException ignored) {}

                        return new ApiRequest(null, null, true);
                    }
                }, new OnTaskCompleted() {
                    @Override
                    public void onCompleted(String res) {

                    }
                });
                apiManager.execute(null, null);
                session.destroy();

                startActivity(new Intent(getActivity(), AuthActivity.class));
                getActivity().finish();
            }
        });

        final TextView viewLogin = (TextView) view.findViewById(R.id.settingsShowLogin);
        final TextView viewEmail = (TextView) view.findViewById(R.id.settingsShowEmail);

        ApiManager apiManager = new ApiManager(getActivity(), new OnJSONRequestBuilder() {
            @Override
            public ApiRequest onCreate() {
                try {
                    String apiHref = NPF.Server.API.GET_USER;
                    JSONObject sendObject = new JSONObject();
                    sendObject.put("access_token", NPF.Server.ACCESS_TOKEN);
                    sendObject.put("session_token", session.getToken());

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
                        JSONObject data = resJson.getJSONObject("data");
                        String login = data.getString("login");
                        String email = data.getString("email");

                        viewLogin.setText(login);
                        viewEmail.setText(email);
                        return;
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
                    Toast toast = Toast.makeText(getActivity(),
                            msg, Toast.LENGTH_SHORT);
                    toast.show();
                } catch (JSONException ignored) {
                }
            }
        });
        apiManager.execute(null, null);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //nav.setSelectedItemId(R.id.navigation_settings);
        nav.getMenu().getItem(NPF.MenuItem.SETTINGS).setChecked(true);
        getActivity().setTitle(getResources().getString(R.string.title_settings));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
    }
}
