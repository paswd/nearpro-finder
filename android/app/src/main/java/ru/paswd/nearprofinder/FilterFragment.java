package ru.paswd.nearprofinder;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.paswd.nearprofinder.api.ApiManager;
import ru.paswd.nearprofinder.api.ApiRequest;
import ru.paswd.nearprofinder.api.OnJSONRequestBuilder;
import ru.paswd.nearprofinder.api.OnTaskCompleted;
import ru.paswd.nearprofinder.config.NPF;
import ru.paswd.nearprofinder.model.PropertyFilter;
import ru.paswd.nearprofinder.model.Session;
import ru.paswd.nearprofinder.model.StringWithReference;
import ru.paswd.nearprofinder.model.StringWithTag;

public class FilterFragment extends Fragment {
    private BottomNavigationView nav;
    private View view;

    private ArrayList<StringWithTag> countriesList = new ArrayList<>();
    private ArrayList<StringWithReference> regionsList = new ArrayList<>();
    private ArrayList<StringWithTag> selectedRegionsList = new ArrayList<>();
    private ArrayList<StringWithTag> propertyTypesList = new ArrayList<>();

    Spinner countriesSpinner;
    Spinner regionsSpinner;
    Spinner propertyTypesSpinner;

    private MainActivity activity;

    private Session session;
    private String regionsJsonStr = "";
    private String propertyTypesJsonStr = "";

    private PropertyFilter propertyFilter;

    private static final String PREF_REGIONS = "filter_regions_list";
    private static final String PREF_PROPERTY_TYPES = "filter_types_list";

    public void saveRegionsJsonLocal() {
        SharedPreferences sPref = getPreferences();

        SharedPreferences.Editor editor = sPref.edit();
        //editor.putString(PREF_NAME, sessionToken);
        editor.putString(PREF_REGIONS, regionsJsonStr);
        editor.apply();
    }
    public void savePropertyTypesJsonsLocal() {
        SharedPreferences sPref = getPreferences();

        SharedPreferences.Editor editor = sPref.edit();
        //editor.putString(PREF_NAME, sessionToken);
        editor.putString(PREF_PROPERTY_TYPES, propertyTypesJsonStr);
        editor.apply();
    }
    public void loadJsonsLocal() {
        SharedPreferences sPref = getPreferences();
        //sessionToken = sPref.getString(PREF_NAME, "");
        regionsJsonStr = sPref.getString(PREF_REGIONS, "");
        propertyTypesJsonStr = sPref.getString(PREF_PROPERTY_TYPES, "");
    }

    public void setNavigation(BottomNavigationView navigation) {
        nav = navigation;
    }

    public void setActivity(MainActivity act) { activity = act; }

    private String getPriceStr(int price) {
        return price > 0
                ? Integer.toString(price)
                : "";
    }
    private int setPriceFromStr(String price) {
        return !price.isEmpty()
                ? Integer.valueOf(price)
                : 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_filter, null);
        propertyFilter = new PropertyFilter(getActivity());

                loadJsonsLocal();

        listsFill();
        initSpinners();
        getDataFromHost();
        //setPreviousValues();

        EditText priceMinView = view.findViewById(R.id.filterPriceMin);
        EditText priceMaxView = view.findViewById(R.id.filterPriceMax);
        priceMinView.setText(getPriceStr(propertyFilter.getPriceMin()));
        priceMaxView.setText(getPriceStr(propertyFilter.getPriceMax()));

        Button btnPointsList = view.findViewById(R.id.filterPointsList);
        btnPointsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setViewPointsList();
                //activity.setViewPointsList(true);
                startActivity(new Intent(activity, PointsViewActivity.class));
            }
        });
        Button btnSearch = view.findViewById(R.id.filterSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int countrySelectedPos = countriesSpinner.getSelectedItemPosition();
                int regionSelectedPos = regionsSpinner.getSelectedItemPosition();
                int propertyTypeSelectedPos = propertyTypesSpinner.getSelectedItemPosition();

                int countrySelectedId = countriesList.get(countrySelectedPos).tag;
                int regionSelectedId = selectedRegionsList.get(regionSelectedPos).tag;
                int propertyTypeSelectedId = propertyTypesList.get(propertyTypeSelectedPos).tag;
                String priceMinStr = ((EditText) view
                        .findViewById(R.id.filterPriceMin))
                        .getText()
                        .toString();
                String priceMaxStr = ((EditText) view
                        .findViewById(R.id.filterPriceMax))
                        .getText()
                        .toString();

                int priceMin = setPriceFromStr(priceMinStr);
                int priceMax = setPriceFromStr(priceMaxStr);


                propertyFilter.set(countrySelectedId, regionSelectedId, propertyTypeSelectedId,
                        priceMin, priceMax);

                Toast toast = Toast.makeText(getActivity(),
                        "Фильтры были успешно применены", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        //Button buttonExit = (Button) view.findViewById(R.id.)
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //nav.setSelectedItemId(R.id.navigation_filter);
        nav.getMenu().getItem(NPF.MenuItem.FILTER).setChecked(true);
        getActivity().setTitle(getResources().getString(R.string.title_filter));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    private void testListsFill() {
        countriesList.clear();
        countriesList.add(new StringWithTag(0,"-Не выбрано-"));
        countriesList.add(new StringWithTag(1, "Российская Фередация"));
        regionsList.add(new StringWithReference(0, 1, "-Не выбрано-"));
        regionsList.add(new StringWithReference(1, 1, "Москва"));
        regionsList.add(new StringWithReference(2, 1, "Санкт-Петербург"));
//        citiesList.add("Москва");
//        citiesList.add("Санкт-Петербург");
        propertyTypesList.add(new StringWithTag(0, "-Не выбрано-"));
        propertyTypesList.add(new StringWithTag(1, "Жилое помещение"));
        propertyTypesList.add(new StringWithTag(2, "Офис"));
    }

    private void listsFill() {
        session = new Session(getActivity(), null);
        countriesAndRegionsFill();
        fillRegions(propertyFilter.getCountry());
        typesFill();
    }

    private void countriesAndRegionsFill() {
        try {
            countriesList.clear();
            regionsList.clear();
            countriesList.add(new StringWithTag(0, "-Не выбрано-"));

            if (regionsJsonStr.isEmpty()) {
                return;
            }

            JSONObject resJson = new JSONObject(regionsJsonStr);
            int status = resJson.getInt("status");
            if (status == NPF.Server.API.Respond.OK) {
                JSONArray dataJson = resJson.getJSONArray("data");
                for (int i = 0; i < dataJson.length(); i++) {
                    JSONObject currCountry = dataJson.getJSONObject(i);
                    int countryId = currCountry.getInt("id");
                    String countryName = currCountry.getString("name");
                    countriesList.add(new StringWithTag(countryId, countryName));

                    JSONArray regions = currCountry.getJSONArray("regions");

                    for (int j = 0; j < regions.length(); j++) {
                        JSONObject currRegion = regions.getJSONObject(j);
                        int regionId = currRegion.getInt("id");
                        String regionName = currRegion.getString("name");

                        regionsList.add(new StringWithReference(regionId, countryId, regionName));
                    }
                }
                saveRegionsJsonLocal();
                //fillRegions(1);
                return;
            }

            int errorNum = resJson.getInt("error_num");

            String msg;

            switch (errorNum) {
                case NPF.Server.API.Respond.Errors.NoNetworkConnection.CODE:
                    msg = NPF.Server.API.Respond.Errors.NoNetworkConnection.MESSAGE;
                    break;

                case NPF.Server.API.Respond.Errors.AccessDenied.CODE:
                    msg = NPF.Server.API.Respond.Errors.AccessDenied.MESSAGE;
                    break;

                case NPF.Server.API.Respond.Errors.SqlError.CODE:
                    msg = NPF.Server.API.Respond.Errors.SqlError.MESSAGE;
                    break;

                default:
                    msg = "Неизвестная ошибка";
                    break;

            }
            Toast toast = Toast.makeText(getActivity(),
                    msg, Toast.LENGTH_SHORT);
            toast.show();
        } catch (JSONException ignored) {
        }
    }
    private void typesFill() {
        try {
            propertyTypesList.clear();
            propertyTypesList.add(new StringWithTag(0, "-Не выбрано-"));

            if (propertyTypesJsonStr.isEmpty()) {
                return;
            }

            JSONObject resJson = new JSONObject(propertyTypesJsonStr);
            int status = resJson.getInt("status");
            if (status == NPF.Server.API.Respond.OK) {
                JSONArray dataJson = resJson.getJSONArray("data");
                for (int i = 0; i < dataJson.length(); i++) {
                    JSONObject currType = dataJson.getJSONObject(i);
                    int typeId = currType.getInt("id");
                    String typeName = currType.getString("name");
                    propertyTypesList.add(new StringWithTag(typeId, typeName));

                }
                savePropertyTypesJsonsLocal();
                //fillRegions(1);
                return;
            }

            int errorNum = resJson.getInt("error_num");

            String msg;

            switch (errorNum) {
                case NPF.Server.API.Respond.Errors.NoNetworkConnection.CODE:
                    msg = NPF.Server.API.Respond.Errors.NoNetworkConnection.MESSAGE;
                    break;

                case NPF.Server.API.Respond.Errors.AccessDenied.CODE:
                    msg = NPF.Server.API.Respond.Errors.AccessDenied.MESSAGE;
                    break;

                case NPF.Server.API.Respond.Errors.SqlError.CODE:
                    msg = NPF.Server.API.Respond.Errors.SqlError.MESSAGE;
                    break;

                default:
                    msg = "Неизвестная ошибка";
                    break;

            }
            Toast toast = Toast.makeText(getActivity(),
                    msg, Toast.LENGTH_SHORT);
            toast.show();
        } catch (JSONException ignored) {
        }
    }

    private void getDataFromHost() {
        getCountriesAndRegions();
        getTypes();
        //saveJsonsLocal();
    }

    private void getCountriesAndRegions() {
        ApiManager apiManager = new ApiManager(getActivity(), new OnJSONRequestBuilder() {
            @Override
            public ApiRequest onCreate() {
                try {
                    String apiHref = NPF.Server.API.GET_REGIONS;
                    JSONObject sendObject = new JSONObject();
                    sendObject.put("access_token", NPF.Server.ACCESS_TOKEN);
                    sendObject.put("session_token", session.getToken());
                    return new ApiRequest(sendObject, apiHref, false);
                } catch (JSONException ignored) {}

                return new ApiRequest(null, null, true);
            }
        }, new OnTaskCompleted() {
            @Override
            public void onCompleted(String res) {
                regionsJsonStr = res;
                //countriesAndRegionsFill();
                listsFill();
            }
        });
        //apiManager.setMsgAuth(login, password);
        apiManager.execute(null, null);
    }
    private void getTypes() {
        ApiManager apiManager = new ApiManager(getActivity(), new OnJSONRequestBuilder() {
            @Override
            public ApiRequest onCreate() {
                try {
                    String apiHref = NPF.Server.API.GET_PROPERTY_TYPES;
                    JSONObject sendObject = new JSONObject();
                    sendObject.put("access_token", NPF.Server.ACCESS_TOKEN);
                    sendObject.put("session_token", session.getToken());
                    return new ApiRequest(sendObject, apiHref, false);
                } catch (JSONException ignored) {}

                return new ApiRequest(null, null, true);
            }
        }, new OnTaskCompleted() {
            @Override
            public void onCompleted(String res) {
                propertyTypesJsonStr = res;
                //countriesAndRegionsFill();
                typesFill();
            }
        });
        //apiManager.setMsgAuth(login, password);
        apiManager.execute(null, null);
    }

    private void fillRegions(int countryId) {
        selectedRegionsList.clear();
        selectedRegionsList.add(new StringWithTag(0, "-Не выбрано-"));

        for (StringWithReference item : regionsList) {
            if (item.reference != countryId) {
                continue;
            }
            selectedRegionsList.add(new StringWithTag(item.tag, item.string));
        }
    }

    private void initSpinners() {
        ArrayAdapter<StringWithTag> countriesAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, countriesList);
        ArrayAdapter<StringWithTag> regionsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, selectedRegionsList);
        ArrayAdapter<StringWithTag> propertyTypesAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, propertyTypesList);


        countriesSpinner = (Spinner) view.findViewById(R.id.filterCountry);
        regionsSpinner = (Spinner) view.findViewById(R.id.filterRegion);
        propertyTypesSpinner = (Spinner) view.findViewById(R.id.filterPropertyType);

        countriesSpinner.setAdapter(countriesAdapter);
        regionsSpinner.setAdapter(regionsAdapter);
        propertyTypesSpinner.setAdapter(propertyTypesAdapter);

        countriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int countryId = countriesList.get(position).tag;
                regionsSpinner.setSelection(0);
                fillRegions(countryId);
                setSelection(regionsSpinner, selectedRegionsList, propertyFilter.getRegion());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //fillRegions(0);
            }
        });
        //countriesSpinner.setSelection(0);
        //countriesSpinner.setSelection(propertyFilter.);
        setSelection(countriesSpinner, countriesList, propertyFilter.getCountry());
        setSelection(propertyTypesSpinner, propertyTypesList, propertyFilter.getType());
    }

    private void setSelection(Spinner spinner, ArrayList<StringWithTag> list, int id) {
        int counter = 0;
        boolean selected = false;
        for (StringWithTag item : list) {
            if (item.tag == id) {
                selected = true;
                break;
            }
            counter++;
        }
        spinner.setSelection(selected ? counter : 0);
    }

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getActivity());
    }
}
