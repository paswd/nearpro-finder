package ru.paswd.nearprofinder.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.paswd.nearprofinder.config.NPF;

public class GeoPoints {
    Context context;
    private DBHelper dbHelper;

    private GeoData geoData;
    private LatLng optimalPoint;

    public GeoPoints(Context ctx) {
        context = ctx;
        dbHelper = new DBHelper(context);
        geoData = new GeoData();
    }

//    public GeoData getGeoData() {
//        return geoData;
//    }

    public void addLocal(Marker marker) {
        geoData.addPoint(marker);
    }
    public void addLocal(String title, LatLng pos) {
        geoData.addPoint(title, pos);
    }

    public void add(Marker addedMarker, LatLng latLng, String title) {
                addLocal(addedMarker);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(NPF.DB.TABLE_POINTS_LIST, "name = ?",
                new String[] { addedMarker.getTitle() });

        ContentValues cv = new ContentValues();
        cv.put("name", addedMarker.getTitle());
        cv.put("lat", latLng.latitude);
        cv.put("lng", latLng.longitude);
        cv.put("updated", Utils.getCurrentUnixTimestamp());
        cv.put("synchronized", 0);
        cv.put("deleted", 0);
        db.insert(NPF.DB.TABLE_POINTS_LIST, null, cv);

        //For testing
        /*secondMarker = firstMarker;
        firstMarker = addedMarker;
        addedCnt++;
        printCurrentDistance();*/

        refreshOptimalPoint();

        //updatePointStorage();
    }

    public void rename(Marker marker, String newTitle) {
        String prevTitle = marker.getTitle();
        /*pointsList.remove(prevTitle);
        marker.setTitle(newTitle);
        pointsList.put(marker.getTitle(), marker);*/
        geoData.renamePoint(marker, newTitle);
        marker.showInfoWindow();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(NPF.DB.TABLE_POINTS_LIST, "name = ?",
                new String[] { marker.getTitle() });

        ContentValues cv = new ContentValues();
        cv.put("name", marker.getTitle());
        cv.put("updated", Utils.getCurrentUnixTimestamp());

        db.update(NPF.DB.TABLE_POINTS_LIST, cv, "name = ?", new String[] { prevTitle });

        //updatePointStorage();
    }

    public void remove(Marker marker) {
        geoData.removePoint(marker);
        ContentValues cv = new ContentValues();
        cv.put("deleted", 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(NPF.DB.TABLE_POINTS_LIST, cv, "name = ?",
                new String[] { marker.getTitle() });
        marker.remove();
        refreshOptimalPoint();
        //updatePointStorage();
    }

    public void clear() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(NPF.DB.TABLE_POINTS_LIST, null, null);

//        for (Marker i : geoData.getPointsValues()) {
//            i.remove();
//        }
        //pointsList.clear();
        geoData.clearAllPoints();
        refreshOptimalPoint();
    }

    public List<MarkerLocal> importPointsListFromStorage() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cr = db.query(NPF.DB.TABLE_POINTS_LIST, new String[] {"name", "lat", "lng"},
                "deleted = 0", null, null, null,null);
        List<MarkerLocal> res = new ArrayList<>();
        if (cr.moveToFirst()) {
            int colLat = cr.getColumnIndex("lat");
            int colLng = cr.getColumnIndex("lng");
            int colName = cr.getColumnIndex("name");

            do {
                double lat = cr.getDouble(colLat);
                double lng = cr.getDouble(colLng);
                String title = cr.getString(colName);
                res.add(new MarkerLocal(new LatLng(lat, lng), title));
                addLocal(title, new LatLng(lat, lng));
            } while (cr.moveToNext());
        }
        cr.close();
        return res;
    }

    private String convertAddressListToUnit(ArrayList<String> addresses) {
        StringBuilder res = new StringBuilder();

        boolean first = true;
        for (String i : addresses) {
            if (i.isEmpty()) {
                continue;
            }
            if (!first) {
                res.append(", ");
            }
            first = false;
            res.append(i);
        }

        return res.toString();
    }


    private String makeNotNull(String str) {
        return (str != null ? str : "");
    }

    public String getAddress(LatLng latLng) {
        try {
            Geocoder geo = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
            String res = "";

            if (!addresses.isEmpty()) {
                ArrayList<String> addressesList = new ArrayList<>();
                //addressesList.add(makeNotNull(addresses.get(0).getAddressLine(0))); // Адрес

                addressesList.add(makeNotNull(addresses.get(0).getThoroughfare())); // Улица
                addressesList.add(makeNotNull(addresses.get(0).getFeatureName())); // Дом, корпус
                addressesList.add(makeNotNull(addresses.get(0).getLocality())); // Город
                addressesList.add(makeNotNull(addresses.get(0).getCountryName())); // Страна
                addressesList.add(makeNotNull(addresses.get(0).getPostalCode())); // Индекс
                /*res = addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " +
                        addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();*/
                res = convertAddressListToUnit(addressesList);
            }
            return res;
        }
        catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
            return "ERROR: " + e.toString();
        }
    }

    public boolean isExists(String title) {
        return geoData.isPointExists(title);
    }

    public boolean isEmpty() {
        return geoData.isPointsListEmpty();
    }

    public LatLng refreshOptimalPoint() {
        if (geoData.getPointCount() >= 2) {
            optimalPoint = geoData.getOptimalPoint();
            return optimalPoint;

            //showInfoAlert("Optimal pos");
        }
        return null;
    }
    public LatLng getOptimalPoint() {
        if (geoData == null) {
            return null;
        }
        optimalPoint = geoData.getOptimalPoint();
        return optimalPoint;
    }





    class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, NPF.DB.NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + NPF.DB.TABLE_POINTS_LIST + " (" +
                    "name text primary key," +
                    "lat real," +
                    "lng real," +
                    "updated integer," +
                    "synchronized integer," +
                    "deleted integer);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
