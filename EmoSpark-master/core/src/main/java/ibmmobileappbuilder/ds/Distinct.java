package ibmmobileappbuilder.ds;

import java.util.List;

/**
 * Interface for unique values retrieval operations
 */
public interface Distinct {

    /**
     * Get the unique values for a given column
     * @param columnName the column name in the datasource
     * @param listener the async listener for this operation
     * @return The list of unique values
     */
    void getUniqueValuesFor(String columnName, Datasource.Listener<List<String>> listener);
}
