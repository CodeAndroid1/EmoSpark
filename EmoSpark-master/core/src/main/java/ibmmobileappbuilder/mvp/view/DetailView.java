package ibmmobileappbuilder.mvp.view;

/**
 * View (as in MVP) for details
 */
public interface DetailView {
    void refresh();
    void close(boolean shouldRefresh);
    void showMessage(int message, boolean toast);
    void navigateToEditForm();
}
