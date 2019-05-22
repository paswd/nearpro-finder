package ru.paswd.nearprofinder;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import ru.paswd.nearprofinder.model.Session;
import ru.paswd.nearprofinder.model.StringWithReference;
import ru.paswd.nearprofinder.model.StringWithTag;

public class FilterFragment extends Fragment {
    private BottomNavigationView nav;
    private View view;

    private ArrayList<StringWithTag> countriesList = new ArrayList<>();
    private ArrayList<StringWithReference> regionsList = new ArrayList<>();
    private ArrayList<StringWithTag> selectedRegionsList = new ArrayList<>();
//    private ArrayList<String> citiesList = new ArrayList<>();
    private ArrayList<StringWithTag> propertyTypesList = new ArrayList<>();
    private MainActivity activity;

    private Session session;
    String regionsJsonStr = "";
    String propertyTypesJsonStr = "";

    public void setNavigation(BottomNavigationView navigation) {
        nav = navigation;
    }

    public void setActivity(MainActivity act) { activity = act; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_filter, null);
        listsFill();
        //testListsFill();
        initSpinners();
        getDataFromHost();
        Button btnPointsList = view.findViewById(R.id.filterPointsList);
        btnPointsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setViewPointsList();
                //activity.setViewPointsList(true);
                startActivity(new Intent(activity, PointsViewActivity.class));
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
    private void getListsFromHost() {

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
    private void listsFill() {
        session = new Session(getActivity(), null);
        countriesAndRegionsFill();
        fillRegions(0);
        typesFill();
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
    private void getDataFromHost() {
        getCountriesAndRegions();
        //fillRegions(0);
        getTypes();
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
//        ArrayAdapter<String> citiesAdapter = new ArrayAdapter<>(getActivity(),
//                android.R.layout.simple_spinner_dropdown_item, citiesList);
        ArrayAdapter<StringWithTag> propertyTypesAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, propertyTypesList);


        Spinner countriesSpinner = (Spinner) view.findViewById(R.id.filterCountry);
        final Spinner regionsSpinner = (Spinner) view.findViewById(R.id.filterRegion);
//        Spinner citiesSpinner = (Spinner) view.findViewById(R.id.filterCity);
        Spinner propertyTypesSpinner = (Spinner) view.findViewById(R.id.filterPropertyType);

        countriesSpinner.setAdapter(countriesAdapter);
        regionsSpinner.setAdapter(regionsAdapter);
//        citiesSpinner.citiesSpinnersetAdapter(citiesAdapter);
        propertyTypesSpinner.setAdapter(propertyTypesAdapter);

        countriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int countryId = countriesList.get(position).tag;
                regionsSpinner.setSelection(0);
                fillRegions(countryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //fillRegions(0);
            }
        });
        countriesSpinner.setSelection(0);
    }
}
