package ru.paswd.nearprofinder;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private ListView pointsListView;
    private View view;
    private BottomNavigationView navigation;
    private MainActivity activity;
    private FloatingActionButton fab_add;

    private BottomNavigationView nav;

    public void setContext(Context ctx) {
        context = ctx;
    }

    public void setNavigation(BottomNavigationView navigation) {
        nav = navigation;
    }

    public void setActivity(MainActivity act) {
        activity = act;
        fab_add = activity.findViewById(R.id.fab_add);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_property_list, null);
        testFillPropertyList();
        adapter = new PointsListAdapter(context, pointsItems);
        pointsListView = (ListView) view.findViewById(R.id.propertyListView);
        pointsListView.setAdapter(adapter);
        pointsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView v, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView v, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (fab_add == null) {
                    return;
                }
                ListView plv = view.findViewById(R.id.propertyListView);
                //if (firstVisibleItem + visibleItemCount > totalItemCount - 2 && visibleItemCount <= totalItemCount) {
                int a = plv.getHeight();
                int bottom = (plv.getHeight() > 0 ?
                        plv.getChildAt(plv.getChildCount() - 1).getBottom() : 0);
                boolean isOnBottom = plv.getLastVisiblePosition() == plv.getAdapter().getCount() -1 &&
                        bottom == plv.getHeight();
                /*Log.e("IsOnBottom", Boolean.toString(isOnBottom));
                Log.e("Count", Integer.toString(plv.getAdapter().getCount()));
                Log.e("LastVisiblePosition", Integer.toString(plv.getLastVisiblePosition()));
                Log.e("Bottom", Integer.toString(bottom));
                Log.e("Height", Integer.toString(plv.getHeight()));*/
                if (isOnBottom && plv.getHeight() > 0) {
                    if (fab_add.getVisibility() == View.VISIBLE) {
                        fab_add.hide();
                    }
                } else {
                    if (fab_add.getVisibility() != View.VISIBLE) {
                        fab_add.show();
                    }
                }
            }
        });

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

    @Override
    public void onPause() {
        super.onPause();
        fab_add.hide();
    }

    void testFillPropertyList() {
        pointsItems.clear();
        for (int i = 0; i < 6; i++) {
            pointsItems.add(new PointsItem("Точка " + i, "Адрес", "55°45.35′N, 37°37.06′E"));
        }
    }
}
