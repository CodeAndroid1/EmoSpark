package ibmmobileappbuilder.cloudant.sync.datastore;


import com.cloudant.sync.datastore.DocumentBody;
import com.cloudant.sync.datastore.DocumentBodyFactory;
import com.google.gson.Gson;

import java.util.Map;

import ibmmobileappbuilder.injectors.GsonInjector;

public class BeanToMapDocumentBody<T> implements DocumentBody {

    private final T item;
    private final Gson gson = GsonInjector.cloudantGson();

    public BeanToMapDocumentBody(T item) {
        this.item = item;
    }

    @Override
    public Map<String, Object> asMap() {
        return DocumentBodyFactory.create(gson.toJson(item).getBytes()).asMap();
    }

    @Override
    public byte[] asBytes() {
        return gson.toJson(item).getBytes();
    }
}
