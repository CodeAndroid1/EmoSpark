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

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.util.Constants;
import ibmmobileappbuilder.util.FragmentUtils;

/**
 * Activity for single detail fragments
 */
public abstract class BaseDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.detail_activity);

        // enable up navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

        Intent intent = getIntent();
        String title = intent.getStringExtra(Constants.TITLE);

        if (title != null) {
            setTitle(title);
        }

        // this adapter is for fragment creation based on spinner selection
        Bundle args = intent.getExtras();
        if(args == null)
            args = new Bundle();

        loadFragment(args);
    }

    private void loadFragment(Bundle args){
        // setup navigation
        Class<? extends Fragment> fClass = getFragmentClass();

        FragmentManager manager = getSupportFragmentManager();

        // show first (listing) page
        if (fClass != null) {
            String tag = this.getClass().getName();

            Fragment fr = manager.findFragmentByTag(tag);
            if (fr == null) {
                fr = FragmentUtils.instantiate(fClass, args);

                manager.beginTransaction()
                    .replace(R.id.content_frame, fr, tag)
                    .commit();
            }
        }

    }

    protected abstract Class<? extends Fragment> getFragmentClass();
}


