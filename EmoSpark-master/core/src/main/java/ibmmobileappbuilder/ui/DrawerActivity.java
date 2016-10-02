/*
 * Copyright 2016.
 * This code is part of IBM Mobile App Builder
 */

package ibmmobileappbuilder.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.navigation.Navigation;
import ibmmobileappbuilder.util.Constants;
import ibmmobileappbuilder.util.FragmentUtils;

/**
 * Base activity for drawer-based navigations
 */
public abstract class DrawerActivity extends BaseActivity implements Navigation,
        NavigationView.OnNavigationItemSelectedListener {

    public static final String FR_DRAWER = "FrDrawer";
    public static String DRAWER_POSITION_KEY = "DRAWER_POSITION";

    private DrawerLayout mDrawerLayout;
    private int lastPosition = -1;
    private ActionBarDrawerToggle mDrawerToggle;
    private SparseArray<Class<? extends Fragment>> mFragments;
    private FragmentManager mFragmentManager;
    private Toolbar mToolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.drawer_activity_main);

        // inject views
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        navigationView = (NavigationView) findViewById(R.id.drawerNavigationView);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(this);

        mFragments = getSectionFragmentClasses();
        // setup drawer (if there are at least two sections, or at least one action)
        setupDrawer();

        mFragmentManager = getSupportFragmentManager();

        // select first item
        int currentItem = mFragments.keyAt(0);
        if (savedInstance != null) {
            currentItem = savedInstance.getInt(DRAWER_POSITION_KEY, currentItem);
            lastPosition = currentItem;
            selectDrawerItem(currentItem);
        } else {
            //We are creating the activity so we will select the first element
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // forces a call to prepareOptionsMenu
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // forces a call to prepareOptionsMenu
                supportInvalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(DRAWER_POSITION_KEY, lastPosition);
    }

    @Override
    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);

        //Syncs the drawer indicator
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerIsOpen = mDrawerLayout.isDrawerOpen(navigationView);

        // if the drawer is open, hide all options
        if (drawerIsOpen) {
            for (int index = 0; index < menu.size(); index++) {
                MenuItem item = menu.getItem(index);
                item.setVisible(false);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);

        // Pass the config object to the drawer toggle
        mDrawerToggle.onConfigurationChanged(config);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        return (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item))
                || super.onOptionsItemSelected(item);
    }

    public void selectDrawerItem(int itemId) {
        if (positionChanged(itemId)) {
            String tag = FR_DRAWER + itemId;
            Fragment fr = mFragmentManager.findFragmentByTag(tag);
            if (fr == null) {
                fr = FragmentUtils.instantiate(mFragments.get(itemId), new Bundle());
            }

            FragmentTransaction replaceTransaction = mFragmentManager.beginTransaction();
            replaceTransaction.replace(R.id.content_frame, fr, tag);
            replaceTransaction.commit();
        }
    }

    private boolean positionChanged(int position) {
        if (position != lastPosition) {
            lastPosition = position;
            return true;
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if we have opened the detail, we are in charge of updating the list
        if (resultCode == Constants.CONTENT_UPDATED) {
            refreshList();
        }
    }

    private void refreshList() {
        Fragment fr = mFragmentManager.findFragmentByTag(FR_DRAWER + lastPosition);
        if (fr instanceof ListGridFragment) {
            ((ListGridFragment) fr).refresh();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        mDrawerLayout.closeDrawers();
        selectDrawerItem(item.getItemId());
        setTitle(item.getTitle());
        return true;
    }
}
