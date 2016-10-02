package ibmmobileappbuilder.data.cloudant;

import com.cloudant.sync.query.IndexManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ibmmobileappbuilder.data.DatasourceSort;
import ibmmobileappbuilder.ds.SearchOptions;

public class CloudantDatasourceSort implements DatasourceSort<List<Map<String, String>>> {

    private final IndexManager indexManager;

    public CloudantDatasourceSort(IndexManager indexManager) {
        this.indexManager = indexManager;
    }

    @Override
    public List<Map<String, String>> generateSort(SearchOptions searchOptions) {
        if (searchOptions == null || searchOptions.getSortColumn() == null) {
            return null;
        } else {
            String sortColumn = searchOptions.getSortColumn();
            List<Map<String, String>> sortDocument = new ArrayList<>();
            Map<String, String> sortByName = new HashMap<>();
            indexManager.ensureIndexed(Collections.<Object>singletonList(sortColumn),
                    "index_" + sortColumn
            );
            sortByName.put(sortColumn, searchOptions.isSortAscending() ? "asc" : "desc");
            sortDocument.add(sortByName);
            return sortDocument;
        }
    }
}
