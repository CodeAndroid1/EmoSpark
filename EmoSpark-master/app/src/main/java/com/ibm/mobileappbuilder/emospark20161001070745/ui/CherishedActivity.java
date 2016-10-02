

package com.ibm.mobileappbuilder.emospark20161001070745.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ibm.mobileappbuilder.emospark20161001070745.R;

import ibmmobileappbuilder.ui.BaseListingActivity;
/**
 * CherishedActivity list activity
 */
public class CherishedActivity extends BaseListingActivity {

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.cherishedActivity));
    }

    @Override
    protected Class<? extends Fragment> getFragmentClass() {
        return CherishedFragment.class;
    }

}

