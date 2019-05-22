package ru.paswd.nearprofinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ru.paswd.nearprofinder.api.ApiManager;
import ru.paswd.nearprofinder.api.ApiRequest;
import ru.paswd.nearprofinder.api.OnJSONRequestBuilder;
import ru.paswd.nearprofinder.api.OnTaskCompleted;
import ru.paswd.nearprofinder.config.NPF;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Объект");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button regBtn = (Button) findViewById(R.id.buttonReg);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText login = (EditText) findViewById(R.id.regLogin);
                EditText password = (EditText) findViewById(R.id.regPassword);
                EditText passwordConfirm = (EditText) findViewById(R.id.regPasswordConfirm);
                EditText email = (EditText) findViewById(R.id.regEmail);

                register(login.getText().toString(), password.getText().toString(),
                        passwordConfirm.getText().toString(), email.getText().toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onBackPressed() {
        finish();
    }

    private void register(final String login, final String password,
                          final String passwordConfirm, final String email) {
        if (!password.equals(passwordConfirm)) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Пароли не совпадают", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (login.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Некоторые поля не заполнены", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        ApiManager apiManager = new ApiManager(this, new OnJSONRequestBuilder() {
            @Override
            public ApiRequest onCreate() {
                try {
                    String apiHref = NPF.Server.API.REGISTER;
                    JSONObject sendObject = new JSONObject();
                    sendObject.put("access_token", NPF.Server.ACCESS_TOKEN);
                    sendObject.put("login", login);
                    sendObject.put("password", password);
                    sendObject.put("email", email);

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
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Вы были успешно зарегистрированы", Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
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

                        case NPF.Server.API.Respond.Errors.FieldsEmpty.CODE:
                            msg = NPF.Server.API.Respond.Errors.FieldsEmpty.MESSAGE;
                            break;

                        case NPF.Server.API.Respond.Errors.Reg.UserExists.CODE:
                            msg = NPF.Server.API.Respond.Errors.Reg.UserExists.MESSAGE;
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

        //apiManager.setMsgRegister(login, password, email);
        apiManager.execute(null, null);


    }
}
