package ibmmobileappbuilder.cloudant.factory;

import android.content.Context;

import com.cloudant.sync.datastore.Datastore;
import com.cloudant.sync.datastore.DatastoreManager;
import com.cloudant.sync.datastore.DatastoreNotCreatedException;

import java.io.File;

import ibmmobileappbuilder.injectors.ApplicationInjector;

public class CloudantDatastoresFactory {

    private static final String CLOUDANT_DATASTORES_NAME = "cloudantDatastores";
    private static final File CLOUDANT_DATASTORE = ApplicationInjector.getApplicationContext().getDir(
            CLOUDANT_DATASTORES_NAME,
            Context.MODE_PRIVATE
    );
    private static final DatastoreManager DATASTORE_MANAGER = new DatastoreManager(
            CLOUDANT_DATASTORE
    );

    public static Datastore create(String dbName) {
        return openDatastore(dbName);
    }

    private static Datastore openDatastore(String datastoreName) {
        try {
            return DATASTORE_MANAGER.openDatastore(datastoreName);
        } catch (DatastoreNotCreatedException e) {
            throw new RuntimeException("Could not create datastore", e);
        }
    }
}
