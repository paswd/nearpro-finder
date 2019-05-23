package ru.paswd.nearprofinder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;

import ru.paswd.nearprofinder.api.ApiManager;
import ru.paswd.nearprofinder.api.ApiRequest;
import ru.paswd.nearprofinder.api.OnJSONRequestBuilder;
import ru.paswd.nearprofinder.api.OnTaskCompleted;
import ru.paswd.nearprofinder.config.NPF;
import ru.paswd.nearprofinder.model.DownloadImageTask;
import ru.paswd.nearprofinder.model.GeoData;
import ru.paswd.nearprofinder.model.GeoPoints;
import ru.paswd.nearprofinder.model.MarkerLocal;
import ru.paswd.nearprofinder.model.OnSessionInvalidListener;
import ru.paswd.nearprofinder.model.Session;
import ru.paswd.nearprofinder.model.Utils;

public class PropertyItemActivity extends AppCompatActivity {

    private Session session;
    private Timer mTimer;
    private AppTimerTask appTimerTask;

    private int id;
    private String name;
    private int price;
    private String imgSrc;
    private double lat;
    private double lng;
    private String address;
    private String href;
    private String description;

    private GeoPoints geoPoints;
    private List<MarkerLocal> pointsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        id = intent.getIntExtra("id", 0);
        name = intent.getStringExtra("name");
        price = intent.getIntExtra("price", 0);
        imgSrc = intent.getStringExtra("imgSrc");
        lat = intent.getDoubleExtra("lat", 0.);
        lng = intent.getDoubleExtra("lng", 0.);
        address = intent.getStringExtra("address");
        href = intent.getStringExtra("href");
        description = intent.getStringExtra("description");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        session = new Session(this, new OnSessionInvalidListener() {
            @Override
            public void onInvalid() {
                switchToAuthActivity();
            }
        });
        geoPoints = new GeoPoints(this);
        pointsList = geoPoints.importPointsListFromStorage();

        fillView();
    }
    private void fillView() {
        ImageView icon = findViewById(R.id.propertyItemImage);
        DownloadImageTask downloadImageTask = new DownloadImageTask(icon);
        downloadImageTask.execute(NPF.Server.HOST + imgSrc);

        setTitle(name);

        ((TextView) findViewById(R.id.propertyItemPrice))
                .setText(Utils.setPriceConvenientFormat(price) + " \u20BD");
        ((TextView) findViewById(R.id.propertyItemAddress))
                .setText(address);
        ((TextView) findViewById(R.id.propertyItemDescription))
                .setText(description);

        ((TextView) findViewById(R.id.propertyItemPointsDistance)).setText(getDistances());
        Button goBtn = findViewById(R.id.propertyItemGoToHref);
        goBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiManager apiManager = new ApiManager(getApplication(), new OnJSONRequestBuilder() {
                            @Override
                            public ApiRequest onCreate() {
                                try {
                                    String apiHref = NPF.Server.API.REGISTER_FOLLOWING;
                                    JSONObject sendObject = new JSONObject();
                                    sendObject.put("access_token", NPF.Server.ACCESS_TOKEN);
                                    sendObject.put("session_token", session.getToken());
                                    sendObject.put("property_id", id);
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
                        Intent browserIntent = new
                                Intent(Intent.ACTION_VIEW, Uri.parse(href));
                        startActivity(browserIntent);
                    }
                });
    }

    private String getDistances() {
        String distances = "";

        LatLng curr = new LatLng(lat, lng);

        boolean isFirst = true;
        for (MarkerLocal item : pointsList) {
            if (!isFirst) {
                distances += "\n";
            }
            isFirst = false;
            double distance = GeoData.getDistance(item.getLatLng(), curr) / 1000;
            distances += "* " + item.getTitle() + " - " + Utils.round(distance, 2) + " км";
        }

        return distances;
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
