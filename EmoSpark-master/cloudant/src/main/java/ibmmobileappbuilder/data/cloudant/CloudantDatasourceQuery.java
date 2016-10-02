package ibmmobileappbuilder.data.cloudant;

import android.util.Log;

import com.cloudant.sync.query.IndexManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import ibmmobileappbuilder.data.DatasourceQuery;
import ibmmobileappbuilder.ds.SearchOptions;
import ibmmobileappbuilder.ds.filter.ContainsFilter;
import ibmmobileappbuilder.ds.filter.Filter;
import ibmmobileappbuilder.ds.filter.IdentityFilter;
import ibmmobileappbuilder.ds.filter.InFilter;
import ibmmobileappbuilder.ds.filter.RangeFilter;
import ibmmobileappbuilder.util.MapUtils;

public class CloudantDatasourceQuery implements DatasourceQuery<Map<String, Object>> {

    private static final String TAG = CloudantDatasourceQuery.class.getSimpleName();
    private final IndexManager indexManager;
    private final String[] searchableFields;

    public CloudantDatasourceQuery(IndexManager indexManager, String[] searchableFields) {
        this.indexManager = indexManager;
        this.searchableFields = searchableFields;
    }


    @Override
    public Map<String, Object> generateQuery(SearchOptions searchOptions) {
        Map<String, Object> queryMap = new HashMap<>();
        addSearch(searchOptions.getSearchText(), queryMap);
        addFilters(searchOptions.getFixedFilters(), queryMap);
        addFilters(searchOptions.getFilters(), queryMap);
        createRequiredIndexes(queryMap.keySet());
        return queryMap;
    }

    private void addFilters(List<Filter> fixedFilters, Map<String, Object> queryMap) {
        for (Filter fixedFilter : fixedFilters) {
            if (fixedFilter instanceof IdentityFilter) {
                queryMap.put(fixedFilter.getField(), ((IdentityFilter) fixedFilter).getValue());
            }
            if (fixedFilter instanceof InFilter) {
                queryMap.put(fixedFilter.getField(),
                        MapUtils.singleEntryMap("$in", ((InFilter) fixedFilter).getValues())
                );
            }
            if (fixedFilter instanceof RangeFilter) {
                Log.w(TAG,
                        "RangeFilters AKA multiple conditions filters are not fully supported in cloudant-sync"
                );
            }
            if (fixedFilter instanceof ContainsFilter) {
                Log.w(TAG,
                        "ContainsFilter AKA $regex are not fully supported in cloudant-sync, adding an identityfilter instead"
                );
                queryMap.put(fixedFilter.getField(), ((ContainsFilter) fixedFilter).getValue());
            }
        }
    }

    private void addSearch(String searchText, Map<String, Object> queryMap) {
        if (searchText != null) {
            Map<String, Object> search = MapUtils.<String, Object>singleEntryMap("$search",
                    searchText + "*"
            );
            queryMap.put("$text", search);
        }
    }

    private void createRequiredIndexes(Set<String> keySet) {
        if (searchableFields != null) {
            indexManager.ensureIndexed(Arrays.<Object>asList(searchableFields),
                    "index_text",
                    "text"
            );
        }
        for (String key : keySet) {
            if (shouldAddIndex(key)) {
                indexManager.ensureIndexed(Collections.<Object>singletonList(key), "index_" + key);
            }
        }
    }

    private boolean shouldAddIndex(String key) {
        return key != null && !key.startsWith("$");
    }
}
