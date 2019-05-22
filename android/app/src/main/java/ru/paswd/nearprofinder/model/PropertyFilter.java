package ru.paswd.nearprofinder.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PropertyFilter {
    private static final String PREFERENCES_PREFIX = "filter_";
    private static final String PREF_COUNTRY = PREFERENCES_PREFIX + "country";
    private static final String PREF_REGION = PREFERENCES_PREFIX + "region";
    private static final String PREF_TYPE = PREFERENCES_PREFIX + "type";
    private static final String PREF_PRICE_MIN = PREFERENCES_PREFIX + "price_min";
    private static final String PREF_PRICE_MAX = PREFERENCES_PREFIX + "price_max";

    private Context context;

    private int country;
    private int region;
    private int type;
    private int priceMin;
    private int priceMax;

    public PropertyFilter(Context ctx) {
        context = ctx;
        loadLocal();
    }

    public void setEmpty() {
        country = 0;
        region = 0;
        type = 0;
        priceMin = 0;
        priceMax = 0;
    }

    public void set(int _country, int _region, int _type, int _priceMin, int _priceMax) {
        country = _country;
        region = _region;
        type = _type;
        priceMin = _priceMin;
        priceMax = _priceMax;

        saveLocal();
    }

    private void loadLocal() {
        SharedPreferences sPref = getPreferences();
        //sessionToken = sPref.getString(PREF_NAME, "");
        country = sPref.getInt(PREF_COUNTRY, 0);
        region = sPref.getInt(PREF_REGION, 0);
        type = sPref.getInt(PREF_TYPE, 0);
        priceMin = sPref.getInt(PREF_PRICE_MIN, 0);
        priceMax = sPref.getInt(PREF_PRICE_MAX, 0);

    }

    private void saveLocal() {
        SharedPreferences sPref = getPreferences();

        SharedPreferences.Editor editor = sPref.edit();
        //editor.putString(PREF_NAME, sessionToken);
        editor.putInt(PREF_COUNTRY, country);
        editor.putInt(PREF_REGION, region);
        editor.putInt(PREF_TYPE, type);
        editor.putInt(PREF_PRICE_MIN, priceMin);
        editor.putInt(PREF_PRICE_MAX, priceMax);
        editor.apply();
    }

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getCountry() {
        return country;
    }
    public int getRegion() {
        return region;
    }
    public int getType() {
        return type;
    }
    public int getPriceMin() {
        return priceMin;
    }
    public int getPriceMax() {
        return priceMax;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPriceMin(int priceMin) {
        this.priceMin = priceMin;
    }

    public void setPriceMax(int priceMax) {
        this.priceMax = priceMax;
    }
}
