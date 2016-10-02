package ibmmobileappbuilder.ds.filter;

/**
 * Interface for datasource filtering
 * fixme its not flexible at all...REDO!
 */
public interface Filter<T> {

    /**
     * The field this filter is based on
     *
     * @return the field that this field is targeting
     */
    String getField();

    /**
     * Get the query string for this filter (for remote datasources)
     *
     * @return the query string representation for this filter, MongoDB format. Example:
     * "country":"Spain","date":"10/14"
     * <br>
     *     Note: Don't surround it with curly brackets.
     */
    String getQueryString();

    /**
     * Apply this filter
     */
    boolean applyFilter(T fieldValue);
}
