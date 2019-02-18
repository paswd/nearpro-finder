package ru.paswd.nearprofinder;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class FilterFragment extends Fragment {
    private BottomNavigationView nav;
    private View view;

    private ArrayList<String> countriesList = new ArrayList<>();
    private ArrayList<String> regionsList = new ArrayList<>();
    private ArrayList<String> citiesList = new ArrayList<>();
    private ArrayList<String> propertyTypesList = new ArrayList();

    public void setNavigation(BottomNavigationView navigation) {
        nav = navigation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_filter, null);
        testListsFill();
        initSpinners();
        //Button buttonExit = (Button) view.findViewById(R.id.)
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //nav.setSelectedItemId(R.id.navigation_filter);
        nav.getMenu().getItem(NPF.MENU_ITEM_FILTER).setChecked(true);
        getActivity().setTitle(getResources().getString(R.string.title_filter));
    }

    private void testListsFill() {
        countriesList.add("-Не выбрано-");
        countriesList.add("Российская Фередация");
        regionsList.add("-Не выбрано-");
        regionsList.add("Москва");
        regionsList.add("Санкт-Петербург");
        citiesList.add("Москва");
        citiesList.add("Санкт-Петербург");
        propertyTypesList.add("-Не выбрано-");
        propertyTypesList.add("Жилое помещение");
        propertyTypesList.add("Офис");
    }

    private void initSpinners() {
        ArrayAdapter<String> countriesAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, countriesList);
        ArrayAdapter<String> regionsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, regionsList);
        ArrayAdapter<String> citiesAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, citiesList);
        ArrayAdapter<String> propertyTypesAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, propertyTypesList);


        Spinner countriesSpinner = (Spinner) view.findViewById(R.id.filterCountry);
        Spinner regionsSpinner = (Spinner) view.findViewById(R.id.filterRegion);
        Spinner citiesSpinner = (Spinner) view.findViewById(R.id.filterCity);
        Spinner propertyTypesSpinner = (Spinner) view.findViewById(R.id.filterPropertyType);

        countriesSpinner.setAdapter(countriesAdapter);
        regionsSpinner.setAdapter(regionsAdapter);
        citiesSpinner.setAdapter(citiesAdapter);
        propertyTypesSpinner.setAdapter(propertyTypesAdapter);
    }
}
