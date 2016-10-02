package ibmmobileappbuilder.cloudant.sync.datastore;

import android.support.annotation.Nullable;

import com.cloudant.sync.datastore.Datastore;
import com.cloudant.sync.replication.Replicator;
import com.cloudant.sync.replication.ReplicatorBuilder;

import java.net.URI;

public class CloudantDatabaseSyncer implements DatabaseSyncer, DatabaseSyncFinishedListener {

    private final Replicator pullReplicator;
    private final Replicator pushReplicator;
    private final CompositeDatabaseSyncFinishedListener compositeDatabaseSyncFinishedListener;
    private final CloudantReplicationListener replicationListener;

    public CloudantDatabaseSyncer(Datastore datastore, URI uri) {
        pullReplicator = ReplicatorBuilder.pull().from(uri).to(datastore).build();
        pushReplicator = ReplicatorBuilder.push().from(datastore).to(uri).build();
        compositeDatabaseSyncFinishedListener = new CompositeDatabaseSyncFinishedListener();
        replicationListener = new CloudantReplicationListener(compositeDatabaseSyncFinishedListener);
    }


    @Override
    public void sync(@Nullable DatabaseSyncFinishedListener databaseSyncFinishedListener) {
        //if we are already syncing just add the listener
        if (isSyncing() && databaseSyncFinishedListener != null) {
            compositeDatabaseSyncFinishedListener.setListeners(databaseSyncFinishedListener, this);
            return;
        }
        replicationListener.startSyncing();
        compositeDatabaseSyncFinishedListener.setListeners(databaseSyncFinishedListener, this);
        registerListenerAndStart(replicationListener, pullReplicator);
        registerListenerAndStart(replicationListener, pushReplicator);
    }

    @Override
    public boolean isSyncing() {
        return replicationListener.isSyncing();
    }

    @Override
    public void onSynchronized() {
        pullReplicator.getEventBus().unregister(replicationListener);
        pushReplicator.getEventBus().unregister(replicationListener);
        pullReplicator.stop();
        pushReplicator.stop();

    }

    private void registerListenerAndStart(CloudantReplicationListener replicationListener,
                                          Replicator replicator) {
        replicator.getEventBus().register(replicationListener);
        replicator.start();
    }
}
