package ibmmobileappbuilder.behaviors;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.ui.Refreshable;

/**
 * Add refresh pattern to lists
 */
public class RefreshBehavior extends NoOpBehavior {

    private Refreshable refreshable;

    public RefreshBehavior(Refreshable fragment) {
        refreshable = fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            refreshable.refresh();
            return true;
        }
        return false;
    }
}
