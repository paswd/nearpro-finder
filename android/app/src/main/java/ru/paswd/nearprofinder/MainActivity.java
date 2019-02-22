package ru.paswd.nearprofinder;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //private TextView mTextMessage;
    private FragmentTransaction fTrans;
    private int fragmentStatus;
    private PropertyListFragment propertyListFragment;
    private PropertyItemFragment propertyItemFragment;
    private FilterFragment filterFragment;
    private SettingsFragment settingsFragment;

    private BottomNavigationView navigation;

    public void setViewPropertyList(boolean addToStack) {
        if (fragmentStatus != NPF.FRAGMENT_PROPERTY_LIST) {
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.fragmentContainer, propertyListFragment);
            if (addToStack) {
                fTrans.addToBackStack(null);
            }
            fTrans.commit();
            //setTitle(R.string.title_property_list);
            fragmentStatus = NPF.FRAGMENT_PROPERTY_LIST;
        }
    }

    public void setViewPropertyItem(boolean addToStack) {
        if (fragmentStatus != NPF.FRAGMENT_PROPERTY_ITEM) {
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.fragmentContainer, propertyItemFragment);
            if (addToStack) {
                fTrans.addToBackStack(null);
            }
            fTrans.commit();
            //setTitle("Объект");
            fragmentStatus = NPF.FRAGMENT_PROPERTY_ITEM;
        }
    }

    public void setViewFilter(boolean addToStack) {
        if (fragmentStatus != NPF.FRAGMENT_FILTER) {
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.fragmentContainer, filterFragment);
            if (addToStack) {
                fTrans.addToBackStack(null);
            }
            fTrans.commit();
            //setTitle(R.string.title_filter);
            fragmentStatus = NPF.FRAGMENT_FILTER;
        }
    }

    public void setViewSettings(boolean addToStack) {
        if (fragmentStatus != NPF.FRAGMENT_SETTINGS) {
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.fragmentContainer, settingsFragment);
            if (addToStack) {
                fTrans.addToBackStack(null);
            }
            fTrans.commit();
            //setTitle(R.string.title_settings);
            fragmentStatus = NPF.FRAGMENT_SETTINGS;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_property_list:
                    //mTextMessage.setText(R.string.title_property_list);
                    setViewPropertyList(true);
                    return true;
                case R.id.navigation_filter:
                    //mTextMessage.setText(R.string.title_filter);
                    setViewFilter(true);
                    return true;
                case R.id.navigation_settings:
                    //mTextMessage.setText(R.string.title_settings);
                    setViewSettings(true);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        propertyListFragment = new PropertyListFragment();
        //propertyListFragment.init(this, propertyListFragment.getView());
        propertyListFragment.setContext(this);
        propertyItemFragment = new PropertyItemFragment();
        filterFragment = new FilterFragment();
        settingsFragment = new SettingsFragment();

        //mTextMessage = (TextView) findViewById(R.id.message);
        setViewPropertyList(false);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        propertyListFragment.setActivity(this);

        propertyListFragment.setNavigation(navigation);
        propertyItemFragment.setNavigation(navigation);
        filterFragment.setNavigation(navigation);
        settingsFragment.setNavigation(navigation);
        propertyItemFragment.setNavigation(navigation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onBackPressed() {
        /*FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            finish();
        }*/
    }

}
