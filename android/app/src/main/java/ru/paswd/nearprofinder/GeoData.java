package ru.paswd.nearprofinder;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Collection;
import java.util.HashMap;

public class GeoData {
    private HashMap<String, Marker> pointsList;

    public GeoData() {
        pointsList = new HashMap<>();
    }

    public static double getDistance(Marker mrk1, Marker mrk2) {
        //return 0.;
        double lng1 = convertDegreeToRadian(mrk1.getPosition().longitude);
        double lng2 = convertDegreeToRadian(mrk2.getPosition().longitude);
        double lat1 = convertDegreeToRadian(mrk1.getPosition().latitude);
        double lat2 = convertDegreeToRadian(mrk2.getPosition().latitude);
        double lngDiff = Math.abs(lng1 - lng2);

        double numerator = Math.sqrt(
                Math.pow(
                        Math.cos(lat2) * Math.sin(lngDiff),
                        2.
                ) + Math.pow(
                        Math.cos(lat1) * Math.sin(lat2) -
                                Math.sin(lat1) * Math.cos(lat2) * Math.cos(lngDiff),
                        2.
                )
        );
        double denominator = Math.sin(lat1) * Math.sin(lat2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.cos(lngDiff);

        double angularDiff = Math.atan(numerator / denominator);

        return angularDiff * NPF.Geo.RADIUS_METERS;
    }

    public static double convertDegreeToRadian(double angle) {
        return angle * Math.PI / 180.;
    }

    public static double convertRadianToDegree(double angle) {
        return angle * 180. / Math.PI;
    }

    public void addPoint(Marker marker) {
        pointsList.put(marker.getTitle(), marker);
    }

    public int renamePoint(Marker marker, String newTitle) {
        if (newTitle.isEmpty()) {
            return NPF.MethodResult.EMPTY;
        }
        if (isPointExists(newTitle)) {
            return NPF.MethodResult.EXSISTS;
        }
        String prevTitle = marker.getTitle();
        pointsList.remove(prevTitle);
        marker.setTitle(newTitle);
        pointsList.put(marker.getTitle(), marker);

        return NPF.MethodResult.CORRECT;
    }

    public int removePoint(Marker marker) {
        if (!isPointExists(marker.getTitle())) {
            pointsList.remove(marker.getTitle());
            return NPF.MethodResult.CORRECT;
        }
        return NPF.MethodResult.NOT_EXSISTS;
    }

    public void clearAllPoints() {
        pointsList.clear();
    }

    public boolean isPointExists(String title) {
        return pointsList.containsKey(title);
    }

    public boolean isPointsListEmpty() {
        return pointsList.isEmpty();
    }

    public Collection<Marker> getPointsValues() {
        return pointsList.values();
    }
}
