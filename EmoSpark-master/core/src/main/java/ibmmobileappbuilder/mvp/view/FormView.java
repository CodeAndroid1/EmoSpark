package ibmmobileappbuilder.mvp.view;

/**
 * View (as in MVP) for forms
 */
public interface FormView<T> {
    /**
     * Show a message to user
     * @param toast if the message should be persistent (toast)
     * @param messageId
     */
    void showMessage(int messageId, boolean toast);

    /**
     * Close the form and return to the caller
     * @param shouldRefresh if the caller should refresh (usually a listview)
     */
    void close(boolean shouldRefresh);

    /**
     * sets the item to edit
     * @param item
     */
    void setItem(T item);
}
