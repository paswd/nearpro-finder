package ru.paswd.nearprofinder;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //private TextView mTextMessage;
    FragmentTransaction fTrans;
    int fragmentStatus;
    PropertyListFragment propertyListFragment;
    FilterFragment filterFragment;
    SettingsFragment settingsFragment;

    BottomNavigationView navigation;

    private void setViewPropertyList() {
        if (fragmentStatus != NPF.FRAGMENT_PROPERTY_LIST) {
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.fragmentContainer, propertyListFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();
            setTitle(R.string.title_property_list);
        }
    }

    private void setViewFilter() {
        if (fragmentStatus != NPF.FRAGMENT_FILTER) {
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.fragmentContainer, filterFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();
            setTitle(R.string.title_filter);
        }
    }

    private void setViewSettings() {
        if (fragmentStatus != NPF.FRAGMENT_SETTINGS) {
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.fragmentContainer, settingsFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();
            setTitle(R.string.title_settings);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_property_list:
                    //mTextMessage.setText(R.string.title_property_list);
                    setViewPropertyList();
                    return true;
                case R.id.navigation_filter:
                    //mTextMessage.setText(R.string.title_filter);
                    setViewFilter();
                    return true;
                case R.id.navigation_settings:
                    //mTextMessage.setText(R.string.title_settings);
                    setViewSettings();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        propertyListFragment = new PropertyListFragment();
        //propertyListFragment.init(this, propertyListFragment.getView());
        propertyListFragment.setContext(this);
        filterFragment = new FilterFragment();
        settingsFragment = new SettingsFragment();

        //mTextMessage = (TextView) findViewById(R.id.message);
        setViewPropertyList();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        propertyListFragment.setNavigation(navigation);
        filterFragment.setNavigation(navigation);
        settingsFragment.setNavigation(navigation);
    }

}
