/*
 * Copyright 2016.
 * This code is part of IBM Mobile App Builder
 */

package ibmmobileappbuilder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.util.Constants;
import ibmmobileappbuilder.util.FragmentUtils;

/**
 * Base class for Listing Activities
 */
public abstract class BaseListingActivity extends BaseActivity {

    private FragmentManager mFragmentManager;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.listing_activity);

        mFragmentManager = getSupportFragmentManager();

        // enable up navigation
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        // setup navigation
        Class<? extends Fragment> mFragmentClass = getFragmentClass();

        // show first (listing) page
        if (mFragmentClass != null) {
            String tag = this.getClass().getName();

            Fragment fr = mFragmentManager.findFragmentByTag(tag);
            if (fr == null) {
                fr = FragmentUtils.instantiate(mFragmentClass, new Bundle());

                mFragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fr, tag)
                        .commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // in tablet layouts, the activity is in charge of update the list
        // because the edit form opens in another activity
        if (resultCode == Constants.CONTENT_UPDATED &&
                getResources().getBoolean(R.bool.tabletLayout)) {

            // update list
            Fragment fr = mFragmentManager.findFragmentByTag(this.getClass().getName());
            if (fr != null && (fr instanceof Refreshable)) {
                ((Refreshable) fr).refresh();
            }
        }
    }

    /**
     * get the fragment associated with this activity
     *
     * @return
     */
    protected abstract Class<? extends Fragment> getFragmentClass();
}
