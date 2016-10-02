

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
 * "Emo2DS" data source. (e37eb8dc-6eb2-4635-8592-5eb9696050e3)
 */
public class Emo2DS extends AppNowDatasource<Emo2DSItem>{

    // default page size
    private static final int PAGE_SIZE = 20;

    private Emo2DSService service;

    public static Emo2DS getInstance(SearchOptions searchOptions){
        return new Emo2DS(searchOptions);
    }

    private Emo2DS(SearchOptions searchOptions) {
        super(searchOptions);
        this.service = Emo2DSService.getInstance();
    }

    @Override
    public void getItem(String id, final Listener<Emo2DSItem> listener) {
        if ("0".equals(id)) {
                        getItems(new Listener<List<Emo2DSItem>>() {
                @Override
                public void onSuccess(List<Emo2DSItem> items) {
                    if(items != null && items.size() > 0) {
                        listener.onSuccess(items.get(0));
                    } else {
                        listener.onSuccess(new Emo2DSItem());
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    listener.onFailure(e);
                }
            });
        } else {
                      service.getServiceProxy().getEmo2DSItemById(id, new Callback<Emo2DSItem>() {
                @Override
                public void success(Emo2DSItem result, Response response) {
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
    public void getItems(final Listener<List<Emo2DSItem>> listener) {
        getItems(0, listener);
    }

    @Override
    public void getItems(int pagenum, final Listener<List<Emo2DSItem>> listener) {
        String conditions = getConditions(searchOptions, getSearchableFields());
        int skipNum = pagenum * PAGE_SIZE;
        String skip = skipNum == 0 ? null : String.valueOf(skipNum);
        String limit = PAGE_SIZE == 0 ? null: String.valueOf(PAGE_SIZE);
        String sort = getSort(searchOptions);
                service.getServiceProxy().queryEmo2DSItem(
                skip,
                limit,
                conditions,
                sort,
                null,
                null,
                new Callback<List<Emo2DSItem>>() {
            @Override
            public void success(List<Emo2DSItem> result, Response response) {
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
    public void create(Emo2DSItem item, Listener<Emo2DSItem> listener) {
                    
        if(item.pictureUri != null){
            service.getServiceProxy().createEmo2DSItem(item,
                TypedByteArrayUtils.fromUri(item.pictureUri),
                callbackFor(listener));
        }
        else
            service.getServiceProxy().createEmo2DSItem(item, callbackFor(listener));
        
    }

    private Callback<Emo2DSItem> callbackFor(final Listener<Emo2DSItem> listener) {
      return new Callback<Emo2DSItem>() {
          @Override
          public void success(Emo2DSItem item, Response response) {
                            listener.onSuccess(item);
          }

          @Override
          public void failure(RetrofitError error) {
                            listener.onFailure(error);
          }
      };
    }

    @Override
    public void updateItem(Emo2DSItem item, Listener<Emo2DSItem> listener) {
                    
        if(item.pictureUri != null){
            service.getServiceProxy().updateEmo2DSItem(item.getIdentifiableId(),
                item,
                TypedByteArrayUtils.fromUri(item.pictureUri),
                callbackFor(listener));
        }
        else
            service.getServiceProxy().updateEmo2DSItem(item.getIdentifiableId(), item, callbackFor(listener));
        
    }

    @Override
    public void deleteItem(Emo2DSItem item, final Listener<Emo2DSItem> listener) {
                service.getServiceProxy().deleteEmo2DSItemById(item.getIdentifiableId(), new Callback<Emo2DSItem>() {
            @Override
            public void success(Emo2DSItem result, Response response) {
                                listener.onSuccess(result);
            }

            @Override
            public void failure(RetrofitError error) {
                                listener.onFailure(error);
            }
        });
    }

    @Override
    public void deleteItems(List<Emo2DSItem> items, final Listener<Emo2DSItem> listener) {
                service.getServiceProxy().deleteByIds(collectIds(items), new Callback<List<Emo2DSItem>>() {
            @Override
            public void success(List<Emo2DSItem> item, Response response) {
                                listener.onSuccess(null);
            }

            @Override
            public void failure(RetrofitError error) {
                                listener.onFailure(error);
            }
        });
    }

    protected List<String> collectIds(List<Emo2DSItem> items){
        List<String> ids = new ArrayList<>();
        for(Emo2DSItem item: items){
            ids.add(item.getIdentifiableId());
        }
        return ids;
    }

}

