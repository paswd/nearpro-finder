package ru.paswd.nearprofinder;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ru.paswd.nearprofinder.config.NPF;

public class PropertyListFragment extends Fragment {
    private Context context;
    private ArrayList<PropertyItem> propertyItems = new ArrayList<>();
    private PropertyListAdapter adapter;
    private View view;
    private BottomNavigationView navigation;
    private MainActivity activity;

    private BottomNavigationView nav;

    public void setContext(Context ctx) {
        context = ctx;
    }

    public void setNavigation(BottomNavigationView navigation) {
        nav = navigation;
    }

    public void setActivity(MainActivity act) { activity = act; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_property_list, null);
        testFillPropertyList();
        adapter = new PropertyListAdapter(context, propertyItems);
        ListView propertyListView = (ListView) view.findViewById(R.id.propertyListView);
        propertyListView.setAdapter(adapter);

        propertyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //activity.setViewPropertyItem(true);
                startActivity(new Intent(context, PropertyItemActivity.class));
            }
        });

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
        //nav.setSelectedItemId(R.id.navigation_property_list);
        nav.getMenu().getItem(NPF.MenuItem.PROPERTY_LIST).setChecked(true);
        getActivity().setTitle(getResources().getString(R.string.title_property_list));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    void testFillPropertyList() {
        propertyItems.clear();
        for (int i = 0; i < 11; i++) {
            propertyItems.add(new PropertyItem("Объект " + i, "Описание", "Адрес", 100000 * i));
        }
    }

    /*void fillPropertyList() {
        propertyItems.clear();

    }*/
}
