package ibmmobileappbuilder.navigation;

import android.support.v4.app.Fragment;
import android.util.SparseArray;

import ibmmobileappbuilder.ui.DrawerActivity;

/**
 * Interface for Navigation activities. Navigation Activities are mainly used for the upper
 * navigation level, and are composed by sections, implemented as {@link
 * Fragment}.
 * Navigation between sections is up to concrete implementations.
 * Each concrete activity may implement its own navigation pattern.
 * See {@link DrawerActivity}
 */
public interface Navigation {

    /**
     * Get the array of fragment classes that implements each section
     *
     * @return an array of fragment classes
     */
    SparseArray<Class<? extends Fragment>> getSectionFragmentClasses();

}
