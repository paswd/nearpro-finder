package ru.paswd.nearprofinder;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.TreeMap;

public class PointsViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    /*private enum EditDialogMode {
        ADD,
        EDIT
    }*/

    final Context context = this;
    private GoogleMap mMap;
    private HashMap<String, Marker> pointsList;
    //private View alertEditTextLayout;
    //private View alertMarkerMenuLayout;
    private DBHelper dbHelper;

    class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "npf-db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            /*db.execSQL("CREATE TABLE " + NPF.DB_TABLE_POINTS_LIST + " (" +
                    "id integer primary key autoincrement," +
                    "name text," +
                    "lat real," +
                    "lng real," +
                    "added integer," +
                    "synchronized integer);");*/
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    private void importPointsListFromStorage() {

    }

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
        pointsList = new HashMap<>();
        importPointsListFromStorage();

        LayoutInflater inflater = getLayoutInflater();
        /*alertEditTextLayout = inflater.inflate(R.layout.alert_edit_text,
                (ViewGroup) findViewById(R.id.alertEditTextLayout));
        alertMarkerMenuLayout = inflater.inflate(R.layout.alert_marker_menu,
                (ViewGroup) findViewById(R.id.alertMarkerMenuLayout));*/

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

    public void showInfoAlert(String title, String text) {
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

    public void showPointEditableAlert(Marker marker) {
        showPointEditableAlert(null, marker);
    }

    public void showPointEditableAlert(LatLng latLng) {
        showPointEditableAlert(latLng, null);
    }

    public void showPointEditableAlert(final LatLng latLng, final Marker marker) {
        if (latLng == null && marker == null) {
            return;
        }
        if (latLng != null && marker != null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.alert_edit_text,
                (ViewGroup) findViewById(R.id.alertEditTextLayout));

        final EditText dialogInput = layout.findViewById(R.id.alertData);

        String onClickButtonTitle;
        if (marker != null) {
            builder.setTitle("Редактирование точки");
            dialogInput.setText(marker.getTitle());
            onClickButtonTitle = "Изменить";
        } else {
            builder.setTitle("Добавление точки");
            onClickButtonTitle = "Добавить";
        }

        //title.setText("Название точки:");
        dialogInput.setHint("Название точки");
        builder.setView(layout);

        builder.setPositiveButton(onClickButtonTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pointTitle = dialogInput.getText().toString();
                if (pointTitle.isEmpty()) {
                    showInfoAlert("Отказано", "Название не должно быть пустым");
                    return;
                }
                if (marker != null) {
                    if (marker.getTitle().equals(pointTitle)) {
                        return;
                    }
                }
                if (pointsList.containsKey(pointTitle)) {
                    showInfoAlert("Отказано", "Точка с таким названием существует");
                            /*Button btn = dlg.getButton(DialogInterface.BUTTON_POSITIVE);
                            if (btn != null) {

                            }*/
                    return;
                }

                if (marker != null) {
                    pointsList.remove(marker.getTitle());
                    marker.setTitle(pointTitle);
                    pointsList.put(marker.getTitle(), marker);
                    marker.hideInfoWindow();
                    marker.showInfoWindow();
                } else if (latLng != null) {
                    Marker addedMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(pointTitle));
                    pointsList.put(addedMarker.getTitle(), addedMarker);
                }
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

            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                //ddd
                showPointEditableAlert(latLng);
            }
        });

        /*mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });*/
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                //showWarningAlert("Context", "Нажатие на название маркера");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(marker.getTitle());

                //TextView markerTitle = alertMarkerMenuLayout.findViewById(R.id.alertMarkerTitle);
                /*TextView markerEditBtn = alertMarkerMenuLayout.findViewById(R.id.alertMarkerEdit);
                TextView markerDeleteBtn = alertMarkerMenuLayout.findViewById(R.id.alertMarkerDelete);*/

                //markerTitle.setText(marker.getTitle());
                /*markerEditBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showWarningAlert("Редактирование", "Редактирование маркера");
                    }
                });
                markerDeleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showWarningAlert("Удаление", "Удаление маркера");
                    }
                });*/
                builder.setPositiveButton("Редактировать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //showInfoAlert("Редактирование", "Редактирование маркера");
                        showPointEditableAlert(marker);
                    }
                });
                builder.setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showInfoAlert("Удаление", "Удаление маркера");
                    }
                });
                builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Nothing to do
                    }
                });

                //builder.setView(alertMarkerMenuLayout);
                AlertDialog dlg = builder.create();
                dlg.show();
                setButtonPrimaryColor(dlg.getButton(DialogInterface.BUTTON_POSITIVE));
                setButtonPrimaryColor(dlg.getButton(DialogInterface.BUTTON_NEGATIVE));
                setButtonPrimaryColor(dlg.getButton(DialogInterface.BUTTON_NEUTRAL));
            }
        });
    }
}
