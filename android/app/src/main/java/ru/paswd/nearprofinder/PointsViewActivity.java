package ru.paswd.nearprofinder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.TreeMap;

public class PointsViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    final Context context = this;
    private GoogleMap mMap;
    private TreeMap<String, LatLng> pointsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        pointsList = new TreeMap<>();
        setTitle("Список точек");
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public void setButtonPrimaryColor(Button button) {
        if (button != null) {
            button.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public void showWarningAlert(String title, String text) {
        AlertDialog.Builder wrnBuilder = new AlertDialog.Builder(context);
        wrnBuilder.setTitle(title);
        wrnBuilder.setMessage(text);
        wrnBuilder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dlg= wrnBuilder.create();
        dlg.show();
        setButtonPrimaryColor(dlg.getButton(DialogInterface.BUTTON_POSITIVE));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Добавление точки");
                //final EditText dialogInput = new EditText(context);
                //dialogInput.setLayoutParams(new TableLayout.LayoutParams().setMargins());
                //dialogInput.setInputType(InputType.TYPE_CLASS_TEXT);

                //builder.setView(dialogInput);

                LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.alert_edittext,
                        (ViewGroup) findViewById(R.id.alert_layout));
                //TextView title = layout.findViewById(R.id.alert_title);
                final EditText dialogInput = layout.findViewById(R.id.alert_data);

                //title.setText("Название точки:");
                dialogInput.setHint("Название точки");
                builder.setView(layout);

                builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pointTitle = dialogInput.getText().toString();
                        if (pointTitle.isEmpty()) {
                            showWarningAlert("Отказано", "Название не должно быть пустым");
                            return;
                        }
                        if (pointsList.containsKey(pointTitle)) {
                            showWarningAlert("Отказано", "Точка с таким названием существует");
                            /*Button btn = dlg.getButton(DialogInterface.BUTTON_POSITIVE);
                            if (btn != null) {

                            }*/
                            return;
                        }
                        mMap.addMarker(new MarkerOptions().position(latLng).title(pointTitle));
                        pointsList.put(pointTitle, latLng);

                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Closing dialog view
                    }
                });
                AlertDialog dlg = builder.create();
                dlg.show();

                setButtonPrimaryColor(dlg.getButton(DialogInterface.BUTTON_POSITIVE));
                setButtonPrimaryColor(dlg.getButton(DialogInterface.BUTTON_NEGATIVE));

            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //Show droplist menu
            }
        });
    }
}
