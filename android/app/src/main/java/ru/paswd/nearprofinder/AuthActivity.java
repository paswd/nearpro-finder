package ru.paswd.nearprofinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Button buttonAuth = (Button) findViewById(R.id.buttonAuth);
        buttonAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authSuccess();
            }
        });
    }

    private void authSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
