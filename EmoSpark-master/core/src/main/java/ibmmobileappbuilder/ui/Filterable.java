/*
 * Copyright 2016.
 * This code is part of IBM Mobile App Builder
 */

package ibmmobileappbuilder.ui;

import ibmmobileappbuilder.ds.filter.Filter;

/**
 * UI component (activity or fragment) that supports search operations
 */
public interface Filterable {

    /**
     * Set the search parameter
     */
    void onSearchTextChanged(String s);

    /**
     * filters
     *
     * @param filter
     */
    void addFilter(Filter filter);

    void clearFilters();

}
