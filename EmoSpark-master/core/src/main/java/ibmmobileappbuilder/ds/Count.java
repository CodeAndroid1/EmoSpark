package ibmmobileappbuilder.ds;

/**
 * Use this interface to mark datasources as "countable"
 */
public interface Count {

    /**
     * Get the size of this datasource
     *
     * @return the number of elements that this datasource provides
     */
    int getCount();
}
