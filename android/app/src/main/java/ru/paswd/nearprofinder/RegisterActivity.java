package ru.paswd.nearprofinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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

    private void register(String login, String password, String passwordConfirm, String email) {
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

        API api = new API(this, new OnTaskCompleted() {
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
                        case NPF.Server.API.Respond.Errors.NO_NETWORK_CONNECTION:
                            msg = "Интернет-соединение отсутствует";
                            break;

                        case NPF.Server.API.Respond.Errors.ACCESS_DENIED:
                            msg = "Доступ запрещён";
                            break;

                        case NPF.Server.API.Respond.Errors.MYSQL_ERROR:
                            msg = "Ошибка базы данных";
                            break;

                        case NPF.Server.API.Respond.Errors.Reg.FIELDS_EMPTY:
                            msg = "Некоторые поля не заполнены";
                            break;

                        case NPF.Server.API.Respond.Errors.Reg.USER_EXSISTS:
                            msg = "Пользователь с указанным логином уже зарегистрирован";
                            break;

                        default:
                            msg = "Неизвестная ошибка";
                            break;

                    }
                    Toast toast = Toast.makeText(getApplicationContext(),
                            msg, Toast.LENGTH_SHORT);
                    toast.show();
                } catch (JSONException ignored) {}
            }
        });
        api.setMsgRegister(login, password, email);
        api.execute(null, null);


    }
}
