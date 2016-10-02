/*
 * Copyright 2016.
 * This code is part of IBM Mobile App Builder
 */

package ibmmobileappbuilder.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ibmmobileappbuilder.core.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // set up generic event listeners
    //TODO do something else here....
//    public void onEvent(DatasourceFailureEvent event) {
//        Snackbar.make(getRootView(), R.string.error_data_generic,
//                Snackbar.LENGTH_SHORT).show();
//    }
//
//    public void onEvent(DatasourceUnauthorizedEvent event) {
//        Snackbar.make(getRootView(), R.string.error_data_unauthorized,
//                Snackbar.LENGTH_SHORT).show();
//    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private View getRootView(){
        return ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    }
}
