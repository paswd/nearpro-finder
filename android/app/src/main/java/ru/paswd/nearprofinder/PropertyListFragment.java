package ru.paswd.nearprofinder;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

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
                activity.setViewPropertyItem(true);
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
        nav.getMenu().getItem(NPF.MENU_ITEM_PROPERTY_LIST).setChecked(true);
        getActivity().setTitle(getResources().getString(R.string.title_property_list));
    }

    void testFillPropertyList() {
        propertyItems.clear();
        for (int i = 0; i < 11; i++) {
            propertyItems.add(new PropertyItem("Объект " + i, "Описание", "Адрес", 100000 * i));
        }
    }
}
