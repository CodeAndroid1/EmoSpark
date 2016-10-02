package ibmmobileappbuilder.ds;

import java.util.List;

import ibmmobileappbuilder.ui.Filterable;

/**
 * Public interface for Asynchronous datasource
 */
public interface Datasource<T> extends Filterable{

    /**
     * Get all items
     *
     * @param listener the callback to call when this operation has finished
     */
    void getItems(Listener<List<T>> listener);

    /**
     * Get a concrete item
     *
     * @param id       the id of the item in the datasource
     * @param listener the callback to call when this operation has finished
     */
    void getItem(String id, Listener<T> listener);

    /**
     * Public interface for datasource operation callbacks
     *
     * @param <RESULT> the type of the results
     */
    interface Listener<RESULT> {

        /**
         * Called on successful operations
         *
         * @param result the result of the operation
         */
        void onSuccess(RESULT result);

        /**
         * Called when something has failed
         *
         * @param e the thrown exception
         */
        void onFailure(Exception e);
    }
}
