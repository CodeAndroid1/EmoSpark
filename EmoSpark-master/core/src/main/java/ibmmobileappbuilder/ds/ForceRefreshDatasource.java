package ibmmobileappbuilder.ds;

import java.util.List;

public interface ForceRefreshDatasource<T> extends Pagination<T> {
    /**
     * Return paginated items
     * @param pagenum       the page number
     * @param forceRefresh  true if the datasource should hit the network before fetching the data
     * @param listener      the listener to send the results to
     */
    void getItems(int pagenum, boolean forceRefresh, Datasource.Listener<List<T>> listener);
}
