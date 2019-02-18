package ru.paswd.nearprofinder;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingsFragment extends Fragment {
    private BottomNavigationView nav;
    private View view;

    public void setNavigation(BottomNavigationView navigation) {
        nav = navigation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, null);
        Button buttonExit = view.findViewById(R.id.buttonExit);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AuthActivity.class));
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //nav.setSelectedItemId(R.id.navigation_settings);
        nav.getMenu().getItem(NPF.MENU_ITEM_SETTINGS).setChecked(true);
    }
}
