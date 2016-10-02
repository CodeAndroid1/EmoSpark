/*
 * Copyright 2016.
 * This code is part of IBM Mobile App Builder
 */

package ibmmobileappbuilder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ibmmobileappbuilder.core.R;

/**
 * Generic activity for filter screens.
 * For now it only implements a basic toolbar and up navigation
 */
public abstract class FilterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);

        // enable up navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle args = new Bundle();

            // propagate values to fragment
            Intent inIntent = getIntent();
            Bundle extras = inIntent.getExtras();
            if(extras != null)
                args.putAll(extras);

            // if no saved state, then this activity is just created
            Fragment fr = getFragment();
            fr.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fr)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected abstract Fragment getFragment();

}
