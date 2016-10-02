

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
 * "Emo1DS" data source. (e37eb8dc-6eb2-4635-8592-5eb9696050e3)
 */
public class Emo1DS extends AppNowDatasource<Emo1DSItem>{

    // default page size
    private static final int PAGE_SIZE = 20;

    private Emo1DSService service;

    public static Emo1DS getInstance(SearchOptions searchOptions){
        return new Emo1DS(searchOptions);
    }

    private Emo1DS(SearchOptions searchOptions) {
        super(searchOptions);
        this.service = Emo1DSService.getInstance();
    }

    @Override
    public void getItem(String id, final Listener<Emo1DSItem> listener) {
        if ("0".equals(id)) {
                        getItems(new Listener<List<Emo1DSItem>>() {
                @Override
                public void onSuccess(List<Emo1DSItem> items) {
                    if(items != null && items.size() > 0) {
                        listener.onSuccess(items.get(0));
                    } else {
                        listener.onSuccess(new Emo1DSItem());
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    listener.onFailure(e);
                }
            });
        } else {
                      service.getServiceProxy().getEmo1DSItemById(id, new Callback<Emo1DSItem>() {
                @Override
                public void success(Emo1DSItem result, Response response) {
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
    public void getItems(final Listener<List<Emo1DSItem>> listener) {
        getItems(0, listener);
    }

    @Override
    public void getItems(int pagenum, final Listener<List<Emo1DSItem>> listener) {
        String conditions = getConditions(searchOptions, getSearchableFields());
        int skipNum = pagenum * PAGE_SIZE;
        String skip = skipNum == 0 ? null : String.valueOf(skipNum);
        String limit = PAGE_SIZE == 0 ? null: String.valueOf(PAGE_SIZE);
        String sort = getSort(searchOptions);
                service.getServiceProxy().queryEmo1DSItem(
                skip,
                limit,
                conditions,
                sort,
                null,
                null,
                new Callback<List<Emo1DSItem>>() {
            @Override
            public void success(List<Emo1DSItem> result, Response response) {
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
    public void create(Emo1DSItem item, Listener<Emo1DSItem> listener) {
                    
        if(item.pictureUri != null){
            service.getServiceProxy().createEmo1DSItem(item,
                TypedByteArrayUtils.fromUri(item.pictureUri),
                callbackFor(listener));
        }
        else
            service.getServiceProxy().createEmo1DSItem(item, callbackFor(listener));
        
    }

    private Callback<Emo1DSItem> callbackFor(final Listener<Emo1DSItem> listener) {
      return new Callback<Emo1DSItem>() {
          @Override
          public void success(Emo1DSItem item, Response response) {
                            listener.onSuccess(item);
          }

          @Override
          public void failure(RetrofitError error) {
                            listener.onFailure(error);
          }
      };
    }

    @Override
    public void updateItem(Emo1DSItem item, Listener<Emo1DSItem> listener) {
                    
        if(item.pictureUri != null){
            service.getServiceProxy().updateEmo1DSItem(item.getIdentifiableId(),
                item,
                TypedByteArrayUtils.fromUri(item.pictureUri),
                callbackFor(listener));
        }
        else
            service.getServiceProxy().updateEmo1DSItem(item.getIdentifiableId(), item, callbackFor(listener));
        
    }

    @Override
    public void deleteItem(Emo1DSItem item, final Listener<Emo1DSItem> listener) {
                service.getServiceProxy().deleteEmo1DSItemById(item.getIdentifiableId(), new Callback<Emo1DSItem>() {
            @Override
            public void success(Emo1DSItem result, Response response) {
                                listener.onSuccess(result);
            }

            @Override
            public void failure(RetrofitError error) {
                                listener.onFailure(error);
            }
        });
    }

    @Override
    public void deleteItems(List<Emo1DSItem> items, final Listener<Emo1DSItem> listener) {
                service.getServiceProxy().deleteByIds(collectIds(items), new Callback<List<Emo1DSItem>>() {
            @Override
            public void success(List<Emo1DSItem> item, Response response) {
                                listener.onSuccess(null);
            }

            @Override
            public void failure(RetrofitError error) {
                                listener.onFailure(error);
            }
        });
    }

    protected List<String> collectIds(List<Emo1DSItem> items){
        List<String> ids = new ArrayList<>();
        for(Emo1DSItem item: items){
            ids.add(item.getIdentifiableId());
        }
        return ids;
    }

}

