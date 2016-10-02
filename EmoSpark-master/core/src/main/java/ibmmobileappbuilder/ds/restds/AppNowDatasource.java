package ibmmobileappbuilder.ds.restds;

import android.text.TextUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ibmmobileappbuilder.ds.CrudDatasource;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.ds.Distinct;
import ibmmobileappbuilder.ds.GeoDatasource;
import ibmmobileappbuilder.ds.Pagination;
import ibmmobileappbuilder.ds.SearchOptions;
import ibmmobileappbuilder.ds.filter.Filter;
import retrofit.client.Response;

/**
 * AppNow Datasource
 */
public abstract class AppNowDatasource<T> implements CrudDatasource<T>, Pagination<T>, Distinct, GeoDatasource {

    protected SearchOptions searchOptions;

    public AppNowDatasource(SearchOptions searchOptions) {
        this.searchOptions = searchOptions;
    }

    @Override
    public void onSearchTextChanged(String s){
        searchOptions.setSearchText(s);
    }

    @Override
    public void addFilter(Filter filter){
        searchOptions.addFilter(filter);
    }

    @Override
    public void clearFilters() {
        searchOptions.setFilters(null);
    }

    // Utility methods
    protected String getConditions(SearchOptions options, String[] searchCols){
        if(options == null)
            return null;

        // Filters
        ArrayList<String> exps = new ArrayList<>();

        addFilters(exps, options.getFilters(), false);
        addFilters(exps, options.getFixedFilters(), true);

        // TODO: support full text search with $text
        String st = options.getSearchText();
        if (st != null && searchCols != null && searchCols.length > 0){
            ArrayList<String> searches = new ArrayList<>();
            for(String col: searchCols){
                searches.add("{\"" + col + "\":{\"$regex\":\"" + st + "\",\"$options\":\"i\"}}");
            }
            String searchExp = "\"$or\":[" + TextUtils.join(",", searches) + "]";
            exps.add(searchExp);
        }

        if (exps.size() > 0)
            return "{" + TextUtils.join(",", exps) + "}";

        return null;
    }

    private void addFilters(ArrayList<String> exps, List<Filter> filters, boolean fixed){

        if(filters != null){
            List<String> filterExps = new ArrayList<>();

            for(Filter filter: filters){
                String qs = filter.getQueryString();
                if (qs != null)
                    filterExps.add(qs);
            }

            if(filterExps.size() > 0) {
                if(fixed)
                    exps.add("\"$and\":[{" + TextUtils.join("},{", filterExps) + "}]");
                else
                    exps.addAll(filterExps);
            }
        }

    }

    protected String getSort(SearchOptions options){
        if(options == null)
            return null;

        String col = options.getSortColumn();
        boolean asc = options.isSortAscending();

        if(col == null)
            return null;

        if (!asc)
            col = "-" + col;

        return col;
    }

    /**
     * Get the url for a image resource in this datasource
     * @param path the image path (can be relative or absolute)
     * @return the URL object you can pass to an ImageLoader class
     */
    public abstract URL getImageUrl(String path);

}
