package ru.paswd.nearprofinder;

import com.google.android.gms.maps.model.Marker;

public class GeoData {
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

        return angularDiff * NPF.EARTH_RADIUS_METERS;
    }

    public static double convertDegreeToRadian(double angle) {
        return angle * Math.PI / 180.;
    }

    public static double convertRadianToDegree(double angle) {
        return angle * 180. / Math.PI;
    }
}
