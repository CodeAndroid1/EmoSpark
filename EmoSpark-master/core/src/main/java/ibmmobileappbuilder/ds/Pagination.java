package ibmmobileappbuilder.ds;

import java.util.List;

/**
 * interface to paginated datasources
 *
 * @param <T> the item type
 */
public interface Pagination<T> {

    /**
     * return the preferred pagesize for this datasource
     *
     * @return the page size
     */
    int getPageSize();

    /**
     * Return paginated items
     *
     * @param pagenum       the page number
     *                      #getPageSize()}
     * @param listener      the listener to send the results to
     */
    void getItems(int pagenum, Datasource.Listener<List<T>> listener);
}
