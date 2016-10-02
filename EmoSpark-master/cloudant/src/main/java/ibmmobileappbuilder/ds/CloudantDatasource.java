package ibmmobileappbuilder.ds;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.cloudant.sync.datastore.Datastore;
import com.cloudant.sync.datastore.DocumentException;
import com.cloudant.sync.datastore.MutableDocumentRevision;
import com.cloudant.sync.query.IndexManager;
import com.cloudant.sync.query.QueryResult;

import java.net.URI;
import java.util.List;
import java.util.Map;

import ibmmobileappbuilder.cloudant.factory.CloudantDatabaseSyncerFactory;
import ibmmobileappbuilder.cloudant.sync.datastore.BeanToMapDocumentBody;
import ibmmobileappbuilder.cloudant.sync.datastore.CloudantQueryResultToBeanList;
import ibmmobileappbuilder.cloudant.sync.datastore.DatabaseSyncFinishedListener;
import ibmmobileappbuilder.cloudant.sync.datastore.DatabaseSyncer;
import ibmmobileappbuilder.data.DatasourceQuery;
import ibmmobileappbuilder.data.DatasourceSort;
import ibmmobileappbuilder.data.cloudant.CloudantDatasourceQuery;
import ibmmobileappbuilder.data.cloudant.CloudantDatasourceSort;
import ibmmobileappbuilder.ds.filter.Filter;
import ibmmobileappbuilder.mvp.model.MutableIdentifiableBean;

public class CloudantDatasource<T extends MutableIdentifiableBean> implements CrudDatasource<T>, DatabaseSyncFinishedListener, Pagination<T>, GeoDatasource {

    private final Datastore datastore;
    private final DatabaseSyncer databaseSyncer;
    private final Class<T> beanClass;
    private final CloudantQueryResultToBeanList cloudantQueryResultToBeanList;
    private final DatasourceSort<List<Map<String, String>>> datasourceSort;
    private final DatasourceQuery<Map<String, Object>> datasourceQuery;
    private final IndexManager indexManager;
    private final Handler mainHandler;

    //SearchOptions should be passed in the constructor
    private SearchOptions searchOptions;
    private Listener<List<T>> listener;

    public CloudantDatasource(Datastore datastore,
                              DatabaseSyncer databaseSyncer,
                              Class<T> beanClass,
                              SearchOptions searchOptions,
                              @NonNull String[] searchableFields) {
        this.datastore = datastore;
        this.databaseSyncer = databaseSyncer;
        this.beanClass = beanClass;
        this.searchOptions = searchOptions;
        cloudantQueryResultToBeanList = new CloudantQueryResultToBeanList();
        indexManager = new IndexManager(this.datastore);
        datasourceSort = new CloudantDatasourceSort(indexManager);
        datasourceQuery = new CloudantDatasourceQuery(indexManager, searchableFields);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static <T extends MutableIdentifiableBean> CrudDatasource<T> cloudantDatasource(
            Datastore datastore,
            URI cloudantUri,
            Class<T> beanClass,
            SearchOptions searchOptions,
            String... searchableColumns) {
        return new CloudantDatasource<>(
                datastore,
                CloudantDatabaseSyncerFactory.instanceFor(datastore.getDatastoreName(), cloudantUri),
                beanClass,
                searchOptions,
                searchableColumns != null ? searchableColumns : new String[]{}
        );
    }

    @Override
    public void getItems(Listener<List<T>> listener) {

    }

    @Override
    public void getItem(String s, Listener<T> listener) {

    }

    @Override
    public int getPageSize() {
        return 20;
    }

    @Override
    public void getItems(int pageNum,  Listener<List<T>> listener) {
        getItems(pageNum, getPageSize(), listener, pageNum == 0);
    }

    @Override
    public void onSynchronized() {
        getItems(0, getPageSize(), listener, false);
    }

    //Yeeeea!! one overload more... one nice day this will be refactored vastly
    public void getItems(int pageNum, int pageSize, final Listener<List<T>> listener, boolean syncDb) {
        //If we have to force the sync or the db is already syncing we have to call sync again as this will attach the listener
        if (syncDb || databaseSyncer.isSyncing()) {
            this.listener = listener;
            databaseSyncer.sync(this);
            return;
        }

        QueryResult retrieved = indexManager.find(
                datasourceQuery.generateQuery(searchOptions),
                pageNum * pageSize,
                pageSize,
                null,
                datasourceSort.generateSort(searchOptions)
        );

        notifyOfSuccessOnMainThread(listener, cloudantQueryResultToBeanList.transform(retrieved, beanClass));
    }

    @Override
    public void create(T item, Listener<T> listener) {
        MutableDocumentRevision documentRevision = new MutableDocumentRevision();
        try {
            documentRevision.body = new BeanToMapDocumentBody<>(item);
            datastore.createDocumentFromRevision(documentRevision);
            listener.onSuccess(item);
        } catch (DocumentException e) {
            listener.onFailure(e);
        }
    }

    @Override
    public void updateItem(T item, Listener<T> listener) {
        MutableDocumentRevision mutableDocumentRevision;
        try {
            mutableDocumentRevision = datastore.getDocument(item.getIdentifiableId()).mutableCopy();
            mutableDocumentRevision.body = new BeanToMapDocumentBody<>(item);
            datastore.updateDocumentFromRevision(mutableDocumentRevision);
            listener.onSuccess(item);
        } catch (DocumentException e) {
            listener.onFailure(e);
        }
    }

    @Override
    public void deleteItem(T item, Listener<T> listener) {
        try {
            datastore.deleteDocument(item.getIdentifiableId());
            listener.onSuccess(item);
        } catch (DocumentException e) {
            listener.onFailure(e);
        }

    }

    @Override
    public void deleteItems(List<T> items, Listener<T> listener) {
        for (T item : items) {
            try {
                datastore.deleteDocument(item.getIdentifiableId());
            } catch (DocumentException e) {
                listener.onFailure(e);
                return;
            }
        }

        T firstItem = items.get(0);
        firstItem.setIdentifiableId("all");
        listener.onSuccess(firstItem);
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

    private void notifyOfSuccessOnMainThread(final Listener<List<T>> listener, final List<T> transformedItems) {
        //TODO notify of end reached here.
        if (Looper.getMainLooper() == Looper.myLooper()) {
            listener.onSuccess(transformedItems);
        } else {
            mainHandler.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess(transformedItems);
                        }
                    }
            );
        }
    }
}
