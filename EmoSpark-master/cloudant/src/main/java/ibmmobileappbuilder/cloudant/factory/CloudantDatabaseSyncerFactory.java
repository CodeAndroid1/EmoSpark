package ibmmobileappbuilder.cloudant.factory;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import ibmmobileappbuilder.cloudant.sync.datastore.CloudantDatabaseSyncer;

public class CloudantDatabaseSyncerFactory {

    private static final Map<String, CloudantDatabaseSyncer> CLOUDANT_DB_SYNCERS = new HashMap<>();

    public static CloudantDatabaseSyncer instanceFor(String dbName, URI uri) {
        CloudantDatabaseSyncer cloudantDatabaseSyncer = CLOUDANT_DB_SYNCERS.get(dbName);
        if (cloudantDatabaseSyncer == null) {
            cloudantDatabaseSyncer = new CloudantDatabaseSyncer(CloudantDatastoresFactory.create(dbName), uri);
            CLOUDANT_DB_SYNCERS.put(dbName, cloudantDatabaseSyncer);
        }
        return cloudantDatabaseSyncer;
    }
}
