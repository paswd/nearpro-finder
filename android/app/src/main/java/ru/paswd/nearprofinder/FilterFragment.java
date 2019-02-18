package ru.paswd.nearprofinder;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FilterFragment extends Fragment {
    private BottomNavigationView nav;
    private View view;

    public void setNavigation(BottomNavigationView navigation) {
        nav = navigation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_filter, null);
        //Button buttonExit = (Button) view.findViewById(R.id.)
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //nav.setSelectedItemId(R.id.navigation_filter);
        nav.getMenu().getItem(NPF.MENU_ITEM_FILTER).setChecked(true);
    }
}
