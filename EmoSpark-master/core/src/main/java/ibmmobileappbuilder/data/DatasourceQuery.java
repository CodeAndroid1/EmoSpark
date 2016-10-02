package ibmmobileappbuilder.data;

import ibmmobileappbuilder.ds.SearchOptions;

public interface DatasourceQuery<T> {
    T generateQuery(SearchOptions searchOptions);
}
