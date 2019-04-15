package ru.paswd.nearprofinder;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

public class PointsListFragment extends Fragment {
    private Context context;
    private ArrayList<PointsItem> pointsItems = new ArrayList<>();
    private PointsListAdapter adapter;
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
        adapter = new PointsListAdapter(context, pointsItems);
        ListView pointsListView = (ListView) view.findViewById(R.id.propertyListView);
        pointsListView.setAdapter(adapter);
        /*pointsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView v, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE){
                    FloatingActionButton fab_add = view.findViewById(R.id.fab_add);
                    fab_add.animate().scaleX(1f).scalY(1f).start();
                    flag = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });*/

        /*pointsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activity.setViewPointsItem(true);
            }
        });*/

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
        getActivity().setTitle(getResources().getString(R.string.title_points_list));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    void testFillPropertyList() {
        pointsItems.clear();
        for (int i = 0; i < 11; i++) {
            pointsItems.add(new PointsItem("Точка " + i, "Адрес", "55°45.35′N, 37°37.06′E"));
        }
    }
}
