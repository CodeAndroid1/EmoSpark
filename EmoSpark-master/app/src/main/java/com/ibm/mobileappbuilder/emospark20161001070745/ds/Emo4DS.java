

package com.ibm.mobileappbuilder.emospark20161001070745.ds;

import android.content.Context;

import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import ibmmobileappbuilder.ds.SearchOptions;
import ibmmobileappbuilder.ds.restds.AppNowDatasource;
import ibmmobileappbuilder.util.StringUtils;
import ibmmobileappbuilder.ds.restds.TypedByteArrayUtils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * "Emo4DS" data source. (e37eb8dc-6eb2-4635-8592-5eb9696050e3)
 */
public class Emo4DS extends AppNowDatasource<Emo4DSItem>{

    // default page size
    private static final int PAGE_SIZE = 20;

    private Emo4DSService service;

    public static Emo4DS getInstance(SearchOptions searchOptions){
        return new Emo4DS(searchOptions);
    }

    private Emo4DS(SearchOptions searchOptions) {
        super(searchOptions);
        this.service = Emo4DSService.getInstance();
    }

    @Override
    public void getItem(String id, final Listener<Emo4DSItem> listener) {
        if ("0".equals(id)) {
                        getItems(new Listener<List<Emo4DSItem>>() {
                @Override
                public void onSuccess(List<Emo4DSItem> items) {
                    if(items != null && items.size() > 0) {
                        listener.onSuccess(items.get(0));
                    } else {
                        listener.onSuccess(new Emo4DSItem());
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    listener.onFailure(e);
                }
            });
        } else {
                      service.getServiceProxy().getEmo4DSItemById(id, new Callback<Emo4DSItem>() {
                @Override
                public void success(Emo4DSItem result, Response response) {
                                        listener.onSuccess(result);
                }

                @Override
                public void failure(RetrofitError error) {
                                        listener.onFailure(error);
                }
            });
        }
    }

    @Override
    public void getItems(final Listener<List<Emo4DSItem>> listener) {
        getItems(0, listener);
    }

    @Override
    public void getItems(int pagenum, final Listener<List<Emo4DSItem>> listener) {
        String conditions = getConditions(searchOptions, getSearchableFields());
        int skipNum = pagenum * PAGE_SIZE;
        String skip = skipNum == 0 ? null : String.valueOf(skipNum);
        String limit = PAGE_SIZE == 0 ? null: String.valueOf(PAGE_SIZE);
        String sort = getSort(searchOptions);
                service.getServiceProxy().queryEmo4DSItem(
                skip,
                limit,
                conditions,
                sort,
                null,
                null,
                new Callback<List<Emo4DSItem>>() {
            @Override
            public void success(List<Emo4DSItem> result, Response response) {
                                listener.onSuccess(result);
            }

            @Override
            public void failure(RetrofitError error) {
                                listener.onFailure(error);
            }
        });
    }

    private String[] getSearchableFields() {
        return new String[]{"quotes"};
    }

    // Pagination

    @Override
    public int getPageSize(){
        return PAGE_SIZE;
    }

    @Override
    public void getUniqueValuesFor(String searchStr, final Listener<List<String>> listener) {
        String conditions = getConditions(searchOptions, getSearchableFields());
                service.getServiceProxy().distinct(searchStr, conditions, new Callback<List<String>>() {
             @Override
             public void success(List<String> result, Response response) {
                                  result.removeAll(Collections.<String>singleton(null));
                 listener.onSuccess(result);
             }

             @Override
             public void failure(RetrofitError error) {
                                  listener.onFailure(error);
             }
        });
    }

    @Override
    public URL getImageUrl(String path) {
        return service.getImageUrl(path);
    }

    @Override
    public void create(Emo4DSItem item, Listener<Emo4DSItem> listener) {
                    
        if(item.pictureUri != null){
            service.getServiceProxy().createEmo4DSItem(item,
                TypedByteArrayUtils.fromUri(item.pictureUri),
                callbackFor(listener));
        }
        else
            service.getServiceProxy().createEmo4DSItem(item, callbackFor(listener));
        
    }

    private Callback<Emo4DSItem> callbackFor(final Listener<Emo4DSItem> listener) {
      return new Callback<Emo4DSItem>() {
          @Override
          public void success(Emo4DSItem item, Response response) {
                            listener.onSuccess(item);
          }

          @Override
          public void failure(RetrofitError error) {
                            listener.onFailure(error);
          }
      };
    }

    @Override
    public void updateItem(Emo4DSItem item, Listener<Emo4DSItem> listener) {
                    
        if(item.pictureUri != null){
            service.getServiceProxy().updateEmo4DSItem(item.getIdentifiableId(),
                item,
                TypedByteArrayUtils.fromUri(item.pictureUri),
                callbackFor(listener));
        }
        else
            service.getServiceProxy().updateEmo4DSItem(item.getIdentifiableId(), item, callbackFor(listener));
        
    }

    @Override
    public void deleteItem(Emo4DSItem item, final Listener<Emo4DSItem> listener) {
                service.getServiceProxy().deleteEmo4DSItemById(item.getIdentifiableId(), new Callback<Emo4DSItem>() {
            @Override
            public void success(Emo4DSItem result, Response response) {
                                listener.onSuccess(result);
            }

            @Override
            public void failure(RetrofitError error) {
                                listener.onFailure(error);
            }
        });
    }

    @Override
    public void deleteItems(List<Emo4DSItem> items, final Listener<Emo4DSItem> listener) {
                service.getServiceProxy().deleteByIds(collectIds(items), new Callback<List<Emo4DSItem>>() {
            @Override
            public void success(List<Emo4DSItem> item, Response response) {
                                listener.onSuccess(null);
            }

            @Override
            public void failure(RetrofitError error) {
                                listener.onFailure(error);
            }
        });
    }

    protected List<String> collectIds(List<Emo4DSItem> items){
        List<String> ids = new ArrayList<>();
        for(Emo4DSItem item: items){
            ids.add(item.getIdentifiableId());
        }
        return ids;
    }

}

