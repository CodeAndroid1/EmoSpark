

package com.ibm.mobileappbuilder.emospark20161001070745.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ibm.mobileappbuilder.emospark20161001070745.R;

import ibmmobileappbuilder.ui.BaseListingActivity;
/**
 * EmoSparkActivity list activity
 */
public class EmoSparkActivity extends BaseListingActivity {

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.emoSparkActivity));
    }

    @Override
    protected Class<? extends Fragment> getFragmentClass() {
        return EmoSparkFragment.class;
    }

}

