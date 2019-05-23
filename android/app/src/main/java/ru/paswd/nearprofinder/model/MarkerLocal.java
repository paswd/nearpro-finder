package ru.paswd.nearprofinder.model;

import com.google.android.gms.maps.model.LatLng;

public class MarkerLocal {
    private LatLng latLng;
    private String title;

    public MarkerLocal(LatLng _latLng, String _title) {
        latLng = _latLng;
        title = _title;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
