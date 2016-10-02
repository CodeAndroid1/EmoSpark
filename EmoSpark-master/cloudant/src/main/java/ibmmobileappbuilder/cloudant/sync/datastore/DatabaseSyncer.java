package ibmmobileappbuilder.cloudant.sync.datastore;

public interface DatabaseSyncer {

    void sync(DatabaseSyncFinishedListener databaseSyncFinishedListener);
    boolean isSyncing();
}
