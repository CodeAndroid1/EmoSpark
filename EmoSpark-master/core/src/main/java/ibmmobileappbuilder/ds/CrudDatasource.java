package ibmmobileappbuilder.ds;

import java.util.List;

public interface CrudDatasource<T> extends Datasource<T> {

    /**
     * Creates an item in the Datasource
     *
     * @param item the item to store.
     * @param listener to get notified about the operation result
     */
    void create(T item, Listener<T> listener);

    /**
     * Updates an item in the Datasource
     *
     * @param item the item to update.
     * @param listener to get notified about the operation result
     */
    void updateItem(T item, Listener<T> listener);

    /**
     * Deletes an item in the Datasource
     *
     * @param item the item to delete.
     * @param listener to get notified about the operation result
     */
    void deleteItem(T item, Listener<T> listener);

    /**
     * Deletes multiple items in the Datasource
     *
     * @param items the items to delete.
     * @param listener to get notified about the operation result
     */
    void deleteItems(List<T> items, Listener<T> listener);
}
