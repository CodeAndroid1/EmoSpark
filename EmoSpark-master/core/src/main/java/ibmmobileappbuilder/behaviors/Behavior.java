package ibmmobileappbuilder.behaviors;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import ibmmobileappbuilder.analytics.model.AnalyticsInfo;

/**
 * Try to abstract some android patterns as plugable behaviors
 * in the fragment lifecycle
 */
public interface Behavior {

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onCreateOptionsMenu(Menu menu, MenuInflater inflater);

    boolean onOptionsItemSelected(MenuItem item);

    void onViewCreated(View view, Bundle savedInstanceState);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    // for lists
    void onItemClick(AdapterView<?> parent, View view, int position, long id);

    boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id);

    void onActionClick(AnalyticsInfo action);
}
