package ru.paswd.nearprofinder;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.Deque;

public class MainActivity extends AppCompatActivity {
    //private TextView mTextMessage;
    private FragmentTransaction fTrans;
    //private int fragmentStatus;

    private PropertyListFragment propertyListFragment;
    //private PropertyItemFragment propertyItemFragment;
    private FilterFragment filterFragment;
    private SettingsFragment settingsFragment;
    private PointsListFragment pointsListFragment;
    private Deque<Integer> fragmentStatusStack;

    private BottomNavigationView navigation;

    public Integer removeNull(Integer num) {
        return (num != null ? num : -1);
    }

    public void setViewPropertyList(boolean addToStack) {
        if (removeNull(fragmentStatusStack.peek()) != NPF.Fragment.PROPERTY_LIST) {
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.fragmentContainer, propertyListFragment);
            if (addToStack) {
                fTrans.addToBackStack(null);
            }
            fTrans.commit();
            //setTitle(R.string.title_property_list);
            //fragmentStatus = NPF.FRAGMENT_PROPERTY_LIST;
            fragmentStatusStack.push(NPF.Fragment.PROPERTY_LIST);
        }
    }

    /*public void setViewPropertyItem(boolean addToStack) {
        if (removeNull(fragmentStatusStack.peek()) != NPF.FRAGMENT_PROPERTY_ITEM) {
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.fragmentContainer, propertyItemFragment);

            if (addToStack) {
                fTrans.addToBackStack(null);
            }
            fTrans.commit();
            //setTitle("Объект");
            //fragmentStatus = NPF.FRAGMENT_PROPERTY_ITEM;
            fragmentStatusStack.push(NPF.FRAGMENT_PROPERTY_ITEM);
        }
    }*/

    public void setViewFilter(boolean addToStack) {
        if (removeNull(fragmentStatusStack.peek()) != NPF.Fragment.FILTER) {
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.fragmentContainer, filterFragment);
            if (addToStack) {
                fTrans.addToBackStack(null);
            }
            fTrans.commit();
            //setTitle(R.string.title_filter);
            fragmentStatusStack.push(NPF.Fragment.FILTER);
        }
    }

    public void setViewSettings(boolean addToStack) {
        if (removeNull(fragmentStatusStack.peek()) != NPF.Fragment.SETTINGS) {
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.fragmentContainer, settingsFragment);
            if (addToStack) {
                fTrans.addToBackStack(null);
            }
            fTrans.commit();
            //setTitle(R.string.title_settings);
            fragmentStatusStack.push(NPF.Fragment.SETTINGS);
        }
    }

    public void setViewPointsList(boolean addToStack) {
        if (removeNull(fragmentStatusStack.peek()) != NPF.Fragment.POINTS_LIST) {
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.fragmentContainer, pointsListFragment);
            if (addToStack) {
                fTrans.addToBackStack(null);
            }
            fTrans.commit();
            //setTitle(R.string.title_settings);
            fragmentStatusStack.push(NPF.Fragment.POINTS_LIST);
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

        fragmentStatusStack = new ArrayDeque<>();

        propertyListFragment = new PropertyListFragment();
        //propertyListFragment.init(this, propertyListFragment.getView());
        propertyListFragment.setContext(this);
        //propertyItemFragment = new PropertyItemFragment();
        filterFragment = new FilterFragment();
        settingsFragment = new SettingsFragment();
        pointsListFragment = new PointsListFragment();
        pointsListFragment.setContext(this);

        //mTextMessage = (TextView) findViewById(R.id.message);
        setViewPropertyList(false);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        propertyListFragment.setActivity(this);
        pointsListFragment.setActivity(this);
        filterFragment.setActivity(this);

        propertyListFragment.setNavigation(navigation);
        //propertyItemFragment.setNavigation(navigation);
        filterFragment.setNavigation(navigation);
        settingsFragment.setNavigation(navigation);
        //propertyItemFragment.setNavigation(navigation);
        pointsListFragment.setNavigation(navigation);

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });
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
        android.app.FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
            fragmentStatusStack.pop();
        } else {
            finish();
        }
    }

}
