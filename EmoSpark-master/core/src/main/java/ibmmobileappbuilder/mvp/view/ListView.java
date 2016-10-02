package ibmmobileappbuilder.mvp.view;

/**
 * View (as in MVP) for listings
 */
public interface ListView<T> {
    void refresh();
    void showMessage(int messageRes);
    void showDetail(T item, int position);
}
