package ru.paswd.nearprofinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
                authSuccess();
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

    private void authSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void register() {
        startActivity(new Intent(this, RegisterActivity.class));
        //finish();
    }
}
