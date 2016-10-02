package ibmmobileappbuilder.dialogs;

import android.support.v4.app.DialogFragment;

import java.util.ArrayList;

/**
 * Abstract base class for selection dialogs.
 * Developers must subclass appropriately. This is useful for master-detail and selection scenarios
 *
 * @see ValuesSelectionDialog
 */
public abstract class SelectionDialog extends DialogFragment {
    public static final String INITIAL_VALUES = "InitialValues";

    protected SelectionListener selectionListener;
    protected boolean multipleChoice;
    protected boolean haveSearch;
    protected String title;

    /**
     * If this dialog will allow multiple choices
     *
     * @param multipleChoice true to enable multiple choices
     */
    public SelectionDialog setMultipleChoice(boolean multipleChoice) {
        this.multipleChoice = multipleChoice;
        return this;
    }

    /**
     * Enable a search edit text in this selection dialog.
     *
     * @param hs true to enable searches
     */
    public SelectionDialog setHaveSearch(boolean hs) {
        this.haveSearch = hs;
        return this;
    }

    public SelectionDialog setTitle(String title) {
        this.title = title;
        return this;
    }


    /**
     * The listener to be called when a selection is done (single or multiple)
     *
     * @param listener an object implementing the {@link SelectionListener} interface.
     */
    public void setSelectionListener(SelectionListener listener) {
        this.selectionListener = listener;
    }

    /**
     * interface for dialog dismiss actions
     */
    public interface SelectionListener {

        void onSelected(ArrayList<String> terms);
    }
}
