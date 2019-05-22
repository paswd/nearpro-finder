package ru.paswd.nearprofinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ru.paswd.nearprofinder.api.ApiManager;
import ru.paswd.nearprofinder.api.ApiRequest;
import ru.paswd.nearprofinder.api.OnJSONRequestBuilder;
import ru.paswd.nearprofinder.api.OnTaskCompleted;
import ru.paswd.nearprofinder.config.NPF;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Авторизация");
        setContentView(R.layout.activity_auth);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button buttonAuth = (Button) findViewById(R.id.buttonAuth);
        buttonAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //authSuccess();
                EditText login = (EditText) findViewById(R.id.authLogin);
                EditText password = (EditText) findViewById(R.id.authPassword);
                auth(login.getText().toString(), password.getText().toString());
            }
        });

        TextView regHref = (TextView) findViewById(R.id.authGoReg);
        regHref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void auth(final String login, final String password) {
        if (login.isEmpty() || password.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    NPF.Server.API.Respond.Errors.Auth.InvalidInput.MESSAGE, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        ApiManager apiManager = new ApiManager(this, new OnJSONRequestBuilder() {
            @Override
            public ApiRequest onCreate() {
                try {
                    String apiHref = NPF.Server.API.AUTH;
                    JSONObject sendObject = new JSONObject();
                    sendObject.put("access_token", NPF.Server.ACCESS_TOKEN);
                    sendObject.put("login", login);
                    sendObject.put("password", password);

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
                        //JSONObject data = resJson.getJSONObject("data");
                        //String sessionToken = data.getString("session_token");
                        authSuccess();
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

                        case NPF.Server.API.Respond.Errors.Auth.InvalidInput.CODE:
                            msg = NPF.Server.API.Respond.Errors.Auth.InvalidInput.MESSAGE;
                            break;

                        default:
                            msg = "Неизвестная ошибка";
                            break;

                    }
                    Toast toast = Toast.makeText(getApplicationContext(),
                            msg, Toast.LENGTH_SHORT);
                    toast.show();
                } catch (JSONException ignored) {
                }
            }
        });
        //apiManager.setMsgAuth(login, password);
        apiManager.execute(null, null);
    }

    private void authSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void register() {
        startActivity(new Intent(this, RegisterActivity.class));
        //finish();
    }
}
