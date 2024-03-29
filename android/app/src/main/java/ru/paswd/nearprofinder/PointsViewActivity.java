package ru.paswd.nearprofinder;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import ru.paswd.nearprofinder.config.NPF;
import ru.paswd.nearprofinder.model.GeoData;
import ru.paswd.nearprofinder.model.GeoPoints;
import ru.paswd.nearprofinder.model.MarkerLocal;
import ru.paswd.nearprofinder.model.OnSessionInvalidListener;
import ru.paswd.nearprofinder.model.Session;

public class PointsViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    final Context context = this;
    private GoogleMap mMap;
    //private HashMap<String, Marker> pointsList;
//    private DBHelper dbHelper;
//    private GeoData geoData;
    private GeoPoints geoPoints;
    private Marker optimalPoint = null;

    //For testing
    /*private Marker firstMarker;
    private Marker secondMarker;
    private int addedCnt = 0;*/

    private Session session;
    private Timer mTimer;
    private AppTimerTask appTimerTask;

//    class DBHelper extends SQLiteOpenHelper {
//        public DBHelper(Context context) {
//            // конструктор суперкласса
//            super(context, NPF.DB.NAME, null, 1);
//        }
//
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//            db.execSQL("CREATE TABLE " + NPF.DB.TABLE_POINTS_LIST + " (" +
//                    "name text primary key," +
//                    "lat real," +
//                    "lng real," +
//                    "updated integer," +
//                    "synchronized integer," +
//                    "deleted integer);");
//        }
//
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//        }
//    }

    private Marker addPointLocal(LatLng latLng, String title) {
        Marker addedMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(title));
        //pointsList.put(addedMarker.getTitle(), addedMarker);
        //geoPoints.getGeoData().addPoint(addedMarker);
        geoPoints.addLocal(addedMarker);
        return addedMarker;
    }

    //For testing
    /*private void printCurrentDistance() {
        if (addedCnt >= 2) {
            showInfoAlert("Distance", Double.toString(GeoData.getDistance(firstMarker, secondMarker)) + " meters");
        }
    }*/

    private void addPoint(LatLng latLng, String title) {
        Marker addedMarker = addPointLocal(latLng, title);
        geoPoints.add(addedMarker, latLng, title);

//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        db.delete(NPF.DB.TABLE_POINTS_LIST, "name = ?",
//                new String[] { addedMarker.getTitle() });
//
//        ContentValues cv = new ContentValues();
//        cv.put("name", addedMarker.getTitle());
//        cv.put("lat", latLng.latitude);
//        cv.put("lng", latLng.longitude);
//        cv.put("updated", getCurrentUnixTimestamp());
//        cv.put("synchronized", 0);
//        cv.put("deleted", 0);
//        db.insert(NPF.DB.TABLE_POINTS_LIST, null, cv);
//
//        //For testing
//        /*secondMarker = firstMarker;
//        firstMarker = addedMarker;
//        addedCnt++;
//        printCurrentDistance();*/
//
        refreshOptimalPoint();

        updatePointStorage();
    }

    private void renamePoint(Marker marker, String newTitle) {
        geoPoints.rename(marker, newTitle);
//        String prevTitle = marker.getTitle();
//        /*pointsList.remove(prevTitle);
//        marker.setTitle(newTitle);
//        pointsList.put(marker.getTitle(), marker);*/
//        geoData.renamePoint(marker, newTitle);
//        marker.showInfoWindow();
//
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        db.delete(NPF.DB.TABLE_POINTS_LIST, "name = ?",
//                new String[] { marker.getTitle() });
//
//        ContentValues cv = new ContentValues();
//        cv.put("name", marker.getTitle());
//        cv.put("updated", getCurrentUnixTimestamp());
//
//        db.update(NPF.DB.TABLE_POINTS_LIST, cv, "name = ?", new String[] { prevTitle });
//
        updatePointStorage();
    }

    private void removePoint(Marker marker) {
        geoPoints.remove(marker);
        //pointsList.remove(marker.getTitle());
//        geoData.removePoint(marker);
//        ContentValues cv = new ContentValues();
//        cv.put("deleted", 1);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        db.update(NPF.DB.TABLE_POINTS_LIST, cv, "name = ?",
//                new String[] { marker.getTitle() });
//        marker.remove();
        refreshOptimalPoint();
        updatePointStorage();
    }

    private void clearAllPoints() {
        geoPoints.clear();
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        db.delete(NPF.DB.TABLE_POINTS_LIST, null, null);
//
//        for (Marker i : geoData.getPointsValues()) {
//            i.remove();
//        }
//        //pointsList.clear();
//        geoData.clearAllPoints();
        refreshOptimalPoint();
    }

    private long getCurrentUnixTimestamp() {
        return System.currentTimeMillis() / 1000L;
    }

    private void importPointsListFromStorage() {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor cr = db.query(NPF.DB.TABLE_POINTS_LIST, new String[] {"name", "lat", "lng"},
//                "deleted = 0", null, null, null,null);
//        if (cr.moveToFirst()) {
//            int colLat = cr.getColumnIndex("lat");
//            int colLng = cr.getColumnIndex("lng");
//            int colName = cr.getColumnIndex("name");
//
//            do {
//                double lat = cr.getDouble(colLat);
//                double lng = cr.getDouble(colLng);
//                String title = cr.getString(colName);
//
//                addPointLocal(new LatLng(lat, lng), title);
//            } while (cr.moveToNext());
//        }
//        cr.close();
        List<MarkerLocal> markerLocalList = geoPoints.importPointsListFromStorage();

        for (MarkerLocal item : markerLocalList) {
            addPointLocal(item.getLatLng(), item.getTitle());
        }
    }

//    private String makeNotNull(String str) {
//        return (str != null ? str : "");
//    }

//    private String convertAddressListToUnit(ArrayList<String> addresses) {
//        StringBuilder res = new StringBuilder();
//
//        boolean first = true;
//        for (String i : addresses) {
//            if (i.isEmpty()) {
//                continue;
//            }
//            if (!first) {
//                res.append(", ");
//            }
//            first = false;
//            res.append(i);
//        }
//
//        return res.toString();
//    }
//
//
//    private String getAddress(LatLng latLng) {
//        try {
//            Geocoder geo = new Geocoder(this, Locale.getDefault());
//            List<Address> addresses = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
//            String res = "";
//
//            if (!addresses.isEmpty()) {
//                ArrayList<String> addressesList = new ArrayList<>();
//                //addressesList.add(makeNotNull(addresses.get(0).getAddressLine(0))); // Адрес
//
//                addressesList.add(makeNotNull(addresses.get(0).getThoroughfare())); // Улица
//                addressesList.add(makeNotNull(addresses.get(0).getFeatureName())); // Дом, корпус
//                addressesList.add(makeNotNull(addresses.get(0).getLocality())); // Город
//                addressesList.add(makeNotNull(addresses.get(0).getCountryName())); // Страна
//                addressesList.add(makeNotNull(addresses.get(0).getPostalCode())); // Индекс
//                /*res = addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " +
//                        addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();*/
//                res = convertAddressListToUnit(addressesList);
//            }
//            return res;
//        }
//        catch (Exception e) {
//            e.printStackTrace(); // getFromLocation() may sometimes fail
//            return "ERROR: " + e.toString();
//        }
//    }

    private void updatePointStorage() {
        //showInfoAlert("Update storage", "Storage Updated");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //pointsList = new HashMap<>();
//        dbHelper = new DBHelper(this);
//        geoData = new GeoData();
        geoPoints = new GeoPoints(context);
        //importPointsListFromStorage();

        session = new Session(this, new OnSessionInvalidListener() {
            @Override
            public void onInvalid() {
                switchToAuthActivity();
            }
        });

        setTitle("Список точек");
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

    private void showPointDeleteAlert(final Marker marker) {
        String strText = "Вы действительно хотите удалить точку \"" + marker.getTitle() + "\"? \n\n";
        strText += "Это действие необратимо";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Удаление точки");
        builder.setMessage(strText);
        builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //showInfoAlert("Удаление", "Точка удалена");
                removePoint(marker);
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Nothing to do
            }
        });

        AlertDialog dlg = builder.create();
        dlg.show();
        setButtonPrimaryColor(dlg.getButton(DialogInterface.BUTTON_POSITIVE));
        setButtonPrimaryColor(dlg.getButton(DialogInterface.BUTTON_NEGATIVE));
    }
    private void showMapClearAlert() {
        String strText = "Вы действительно хотите очистить карту? Это действие необратимо";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Очистить карту");
        builder.setMessage(strText);
        builder.setPositiveButton("Очистить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //showInfoAlert("Удаление", "Точка удалена");
                clearAllPoints();
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Nothing to do
            }
        });

        AlertDialog dlg = builder.create();
        dlg.show();
        setButtonPrimaryColor(dlg.getButton(DialogInterface.BUTTON_POSITIVE));
        setButtonPrimaryColor(dlg.getButton(DialogInterface.BUTTON_NEGATIVE));
    }

    private void showPointEditableAlert(Marker marker) {
        showPointEditableAlert(null, marker);
    }

    private void showPointEditableAlert(LatLng latLng) {
        showPointEditableAlert(latLng, null);
    }

    private void showPointEditableAlert(final LatLng latLng, final Marker marker) {
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
            builder.setTitle("Переименование точки");
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
                //if (pointsList.containsKey(pointTitle)) {
//                if (geoData.isPointExists(pointTitle)) {
                if (geoPoints.isExists(pointTitle)) {
                    showInfoAlert("Отказано", "Точка с таким названием существует");
                            /*Button btn = dlg.getButton(DialogInterface.BUTTON_POSITIVE);
                            if (btn != null) {

                            }*/
                    return;
                }

                if (marker != null) {
                    renamePoint(marker, pointTitle);
                } else if (latLng != null) {
                    addPoint(latLng, pointTitle);
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
        LatLng moscow = new LatLng(55.75222, 37.61556);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moscow, (float) 9.5));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {

            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
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
                builder.setTitle("Точка \"" + marker.getTitle() + "\"");
                String msg = "";
                msg += geoPoints.getAddress(marker.getPosition()) + "\n\n";
                msg += "Lat:\t\t" + Double.toString(marker.getPosition().latitude) + "\n";
                msg += "Lng:\t" + Double.toString(marker.getPosition().longitude) + "\n";
                builder.setMessage(msg);

                builder.setPositiveButton("Переименовать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //showInfoAlert("Редактирование", "Редактирование маркера");
                        showPointEditableAlert(marker);
                    }
                });
                builder.setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //showInfoAlert("Удаление", "Удаление маркера");
                        showPointDeleteAlert(marker);
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
        importPointsListFromStorage();
        refreshOptimalPoint();
        /*LatLng ln = new LatLng(10., 350.);

        addPoint(GeoData.coordinateNormalize(ln), "Overload");*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.points_view_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_points_clear:
                if (geoPoints.isEmpty()) {
                    showInfoAlert("Очистка карты", "Карта уже была очищена, или на неё не было установлено ни одной точки");
                } else {
                    showMapClearAlert();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        finish();
    }

    private void removeOptimalPoint() {
        if (optimalPoint != null) {
            optimalPoint.remove();
        }
    }

    private void refreshOptimalPoint() {
        removeOptimalPoint();
        LatLng optimal = geoPoints.refreshOptimalPoint();
        if (optimal != null) {
            optimalPoint = mMap.addMarker(new MarkerOptions()
                    .position(optimal).title("Optimal")
                    .icon(BitmapDescriptorFactory.defaultMarker(228)));
            //showInfoAlert("Optimal pos");
        }
    }
}
