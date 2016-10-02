package ibmmobileappbuilder.data;

import ibmmobileappbuilder.ds.SearchOptions;

public interface DatasourceSort<T> {

    T generateSort(SearchOptions searchOptions);
}
