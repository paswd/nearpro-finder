package ru.paswd.nearprofinder;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

public class PropertyListFragment extends Fragment {
    Context context;
    ArrayList<PropertyItem> propertyItems = new ArrayList<>();
    PropertyListAdapter adapter;
    View view;

    private BottomNavigationView nav;

    void setContext(Context ctx) {
        context = ctx;
    }

    public void setNavigation(BottomNavigationView navigation) {
        nav = navigation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_property_list, null);
        fillPropertyList();
        adapter = new PropertyListAdapter(context, propertyItems);
        ListView propertyListView = (ListView) view.findViewById(R.id.propertyListView);
        propertyListView.setAdapter(adapter);

        return view;
    }
    /*public void init(Context ctx, View v) {
        context = ctx;
        //view = v;
        fillPropertyList();
        adapter = new PropertyListAdapter(context, propertyItems);
        //ListView propertyListView = (ListView) view.findViewById(R.id.propertyListView);
        //propertyListView.setAdapter(adapter);
    }*/

    @Override
    public void onResume() {
        super.onResume();
        nav.setSelectedItemId(R.id.navigation_property_list);
    }

    void fillPropertyList() {
        for (int i = 0; i < 5; i++) {
            propertyItems.add(new PropertyItem("Объект " + i, "Описание", "Адрес", 100000));
        }
    }
}
