package ru.paswd.nearprofinder.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ru.paswd.nearprofinder.config.NPF;

public class GeoData {
    private HashMap<String, Marker> markerList;
    private HashMap<String, LatLng> pointsList;
    private LatLng optimalPoint;
    private boolean isDataChanged;
    private boolean isMarker;

    public GeoData() {
        pointsList = new HashMap<>();
        markerList = new HashMap<>();
        optimalPoint = new LatLng(0., 0.);
        isDataChanged = false;
        isMarker = true;
    }

    public static double getDistance(Marker mrk1, Marker mrk2) {
        return getDistance(mrk1.getPosition(), mrk2.getPosition());
    }

    public static double getDistance(LatLng pos1, LatLng pos2) {
        //return 0.;
        double lng1 = convertDegreeToRadian(pos1.longitude);
        double lng2 = convertDegreeToRadian(pos2.longitude);
        double lat1 = convertDegreeToRadian(pos1.latitude);
        double lat2 = convertDegreeToRadian(pos2.latitude);
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

        double angularDiff = Math.abs(Math.atan(numerator / denominator));

        return angularDiff * NPF.Geo.EARTH_RADIUS_METERS;
    }


    public static double convertDegreeToRadian(double angle) {
        return angle * Math.PI / 180.;
    }

    public static double convertRadianToDegree(double angle) {
        return angle * 180. / Math.PI;
    }

    private double getMinimizedFunctionValue(LatLng curr) {
        Iterator it = pointsList.entrySet().iterator();
        double summ = 0.;

        while (it.hasNext()) {
            Map.Entry<String, LatLng> pair = (Map.Entry) it.next();
            double currDist = getDistance(curr, pair.getValue());
            summ += Math.pow(currDist, 2.);
        }

        return summ;
    }


    private int calculateOptimalPoint() {
        LatLng currPoint = pointsList.entrySet().iterator().next().getValue();

        if (pointsList.size() == 0) {
            optimalPoint = null;
            isDataChanged = false;
            return NPF.MethodResult.CORRECT;
        }
        if (pointsList.size() == 1) {
            optimalPoint = currPoint;
            isDataChanged = false;
            return NPF.MethodResult.CORRECT;
        }

        while (true) {
            double currStepLat = NPF.Geo.Optimization.DEGREE_STEP_BASIC;
            double currStepLng = NPF.Geo.Optimization.DEGREE_STEP_BASIC;

            boolean changedLat = false;
            boolean changedLng = false;

            double currMinFunc = getMinimizedFunctionValue(currPoint);

            //Lat
            while (currStepLat > NPF.Geo.Optimization.DEGREE_STEP_EPS) {
                if (Math.abs(currPoint.latitude) + currStepLat >= NPF.Geo.ABS_LAT_MAX) {
                    currStepLat /= 2.;
                    continue;
                }

                LatLng newPointUpper = new LatLng(currPoint.latitude + currStepLat,
                        currPoint.longitude);

                double minFuncUpper = getMinimizedFunctionValue(newPointUpper);
                if (minFuncUpper < currMinFunc) {
                    currPoint = newPointUpper;
                    currMinFunc = minFuncUpper;
                    changedLat = true;
                    continue;
                }

                LatLng newPointLower = new LatLng(currPoint.latitude - currStepLat,
                        currPoint.longitude);

                double minFuncLower = getMinimizedFunctionValue(newPointLower);
                if (minFuncLower < currMinFunc) {
                    currPoint = newPointLower;
                    currMinFunc = minFuncLower;
                    changedLat = true;
                    continue;
                }

                currStepLat /= 2.;
            }

            //Lng
            while (currStepLng > NPF.Geo.Optimization.DEGREE_STEP_EPS) {
                LatLng newPointUpper = new LatLng(currPoint.latitude,
                        currPoint.longitude + currStepLng);

                double minFuncUpper = getMinimizedFunctionValue(newPointUpper);
                if (minFuncUpper < currMinFunc) {
                    currPoint = newPointUpper;
                    currMinFunc = minFuncUpper;
                    changedLng = true;
                    continue;
                }

                LatLng newPointLower = new LatLng(currPoint.latitude,
                        currPoint.longitude - currStepLng);

                double minFuncLower = getMinimizedFunctionValue(newPointLower);
                if (minFuncLower < currMinFunc) {
                    currPoint = newPointLower;
                    currMinFunc = minFuncLower;
                    changedLng = true;
                    continue;
                }

                currStepLng /= 2.;
            }

            if (!changedLat && !changedLng) {
                break;
            }
        }

        optimalPoint = currPoint;
        isDataChanged = false;

        return NPF.MethodResult.CORRECT;
    }
    public int addPoint(String title, LatLng pos) {
//        if (isPointExists(title)) {
//            return NPF.MethodResult.EXSISTS;
//        }

        pointsList.put(title, pos);
        isDataChanged = true;

        return NPF.MethodResult.CORRECT;
    }

    public int addPoint(Marker marker) {
//        if (isPointExists(marker.getTitle())) {
//            return NPF.MethodResult.EXSISTS;
//        }

        pointsList.put(marker.getTitle(), marker.getPosition());
        markerList.put(marker.getTitle(), marker);
        isDataChanged = true;

        return NPF.MethodResult.CORRECT;
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
        markerList.remove(prevTitle);
        marker.setTitle(newTitle);
        pointsList.put(marker.getTitle(), marker.getPosition());
        markerList.put(marker.getTitle(), marker);
        isDataChanged = true;

        return NPF.MethodResult.CORRECT;
    }

    public int removePoint(Marker marker) {
        if (!isPointExists(marker.getTitle())) {
            return NPF.MethodResult.NOT_EXSISTS;
        }

        pointsList.remove(marker.getTitle());
        markerList.remove(marker.getTitle());
        isDataChanged = true;

        return NPF.MethodResult.CORRECT;
    }

    public void clearAllPoints() {
        isDataChanged = true;
        for (Marker mrk : getPointsValues()) {
            mrk.remove();
        }
        pointsList.clear();
        markerList.clear();
    }

    public boolean isPointExists(String title) {
        return pointsList.containsKey(title);
    }

    public boolean isPointsListEmpty() {
        return pointsList.isEmpty();
    }

    public Collection<Marker> getPointsValues() {
        return markerList.values();
    }

    public LatLng getOptimalPoint() {
        if (isDataChanged) {
            calculateOptimalPoint();
        }
        return optimalPoint;
    }

    public int getPointCount() {
        return pointsList.size();
    }
}
