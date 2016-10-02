package ibmmobileappbuilder.cloudant.sync.datastore;

import com.cloudant.sync.datastore.DocumentRevision;
import com.cloudant.sync.query.QueryResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ibmmobileappbuilder.mvp.model.MutableIdentifiableBean;

import static ibmmobileappbuilder.injectors.GsonInjector.cloudantGson;

public class CloudantQueryResultToBeanList {

    private final Gson gson = cloudantGson();

    public <T> List<T> transform(QueryResult queryResult, Class<T> beanClass) {
        List<T> result = new ArrayList<>();
        if (queryResult == null) {
            return result;
        }
        for (DocumentRevision document : queryResult) {
            //This should go on a try/catch as we can have multiple types here
            byte[] bytes = document.getBody().asBytes();
            // This should go in a try catch just in case there are different documents type
            T item = gson.fromJson(new String(bytes), beanClass);
            if (item instanceof MutableIdentifiableBean) {
                ((MutableIdentifiableBean) item).setIdentifiableId(document.getId());
            }
            result.add(item);
        }
        return result;
    }
}
