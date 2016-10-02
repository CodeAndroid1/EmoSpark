package ibmmobileappbuilder.ds;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ibmmobileappbuilder.ds.filter.Filter;

public class SearchOptions {

    private String searchText;
    //Extract sortOptions class
    private String sortColumn;
    private Comparator sortComparator;
    private boolean sortAscending;
    private List<Filter> filters = new ArrayList<>();
    private List<Filter> fixedFilters = new ArrayList<>();     // set here filters users won't be able to change

    public SearchOptions() {
    }

    public SearchOptions(String searchText, String sortColumn, Comparator sortComparator,
                         boolean sortAscending) {
        this.searchText = searchText;
        this.sortColumn = sortColumn;
        this.sortComparator = sortComparator;
        this.sortAscending = sortAscending;
    }

    private SearchOptions(Builder builder) {
        this.searchText = builder.searchText;
        this.sortColumn = builder.sortColumn;
        this.sortComparator = builder.sortComparator;
        this.sortAscending = builder.sortAscending;
        this.filters = builder.filters;
        this.fixedFilters = builder.fixedFilters;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public Comparator getSortComparator() {
        return sortComparator;
    }

    public String getSortColumn() {
        return sortColumn;
    }


    public boolean isSortAscending() {
        return sortAscending;
    }

    public void addFilter(Filter filter) {
        if (this.filters == null) {
            this.filters = new ArrayList<>();
        }

        this.filters.add(filter);
    }

    public List<Filter> getFilters() {
        return this.filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public List<Filter> getFixedFilters() {
        return fixedFilters;
    }

    public static class Builder {
        private String searchText;
        private String sortColumn;
        private Comparator sortComparator;
        private boolean sortAscending;
        private List<Filter> filters = new ArrayList<>();
        private List<Filter> fixedFilters = new ArrayList<>();

        public static Builder searchOptions() {
            return new Builder();
        }

        public Builder withSearchText(String searchText) {
            this.searchText = searchText;
            return this;
        }

        public Builder withSortColumn(String sortColumn) {
            this.sortColumn = sortColumn;
            return this;
        }

        public Builder withSortComparator(Comparator sortComparator) {
            this.sortComparator = sortComparator;
            return this;
        }

        public Builder withSortAscending(boolean sortAscending) {
            this.sortAscending = sortAscending;
            return this;
        }

        public Builder withFilters(List<Filter> filters) {
            this.filters.clear();
            this.filters.addAll(filters);
            return this;
        }

        public Builder withFixedFilters(List<Filter> fixedFilters) {
            this.fixedFilters.clear();
            this.fixedFilters.addAll(fixedFilters);
            return this;
        }

        public SearchOptions build() {
            return new SearchOptions(this);
        }
    }
}