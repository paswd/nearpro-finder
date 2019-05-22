package ru.paswd.nearprofinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Timer;

import ru.paswd.nearprofinder.config.NPF;
import ru.paswd.nearprofinder.model.OnSessionInvalidListener;
import ru.paswd.nearprofinder.model.Session;

public class PropertyItemActivity extends AppCompatActivity {

    private Session session;
    private Timer mTimer;
    private AppTimerTask appTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Объект");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        session = new Session(this, new OnSessionInvalidListener() {
            @Override
            public void onInvalid() {
                switchToAuthActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mTimer != null) {
            mTimer.cancel();
        }

        mTimer = new Timer();
        appTimerTask = new AppTimerTask(session);
        mTimer.schedule(appTimerTask, NPF.Global.TIMER_DELAY, NPF.Global.TIMER_DELAY);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void switchToAuthActivity() {
        Toast toast = Toast.makeText(getApplicationContext(),
                NPF.Session.Messages.SESSION_INVALID, Toast.LENGTH_SHORT);
        toast.show();
        startActivity(new Intent(this, AuthActivity.class));
        finish();
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
}
