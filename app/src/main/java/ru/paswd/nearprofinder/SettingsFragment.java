package ru.paswd.nearprofinder;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends Fragment {
    private BottomNavigationView nav;

    public void setNavigation(BottomNavigationView navigation) {
        nav = navigation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        nav.setSelectedItemId(R.id.navigation_settings);
    }
}
