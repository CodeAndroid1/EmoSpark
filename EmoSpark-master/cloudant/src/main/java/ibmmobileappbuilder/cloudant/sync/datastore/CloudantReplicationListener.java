package ibmmobileappbuilder.cloudant.sync.datastore;

import com.cloudant.sync.notifications.ReplicationCompleted;
import com.cloudant.sync.notifications.ReplicationErrored;
import com.google.common.eventbus.Subscribe;

import java.util.concurrent.CountDownLatch;

/**
 * A {@code ReplicationListener} that sets a latch when it's told the
 * replication has finished.
 */
public class CloudantReplicationListener {

    private final DatabaseSyncFinishedListener databaseSyncFinishedListener;

    private CountDownLatch countDownLatch;

    public CloudantReplicationListener(DatabaseSyncFinishedListener databaseSyncFinishedListener) {
        this.databaseSyncFinishedListener = databaseSyncFinishedListener;
        countDownLatch = new CountDownLatch(0);
    }

    public void startSyncing(){
        countDownLatch = new CountDownLatch(2);
    }

    public boolean isSyncing() {
        return countDownLatch.getCount() != 0;
    }

    @Subscribe
    public void complete(ReplicationCompleted event) {
        countDownAndNotifyIfDone();
    }

    @Subscribe
    public void error(ReplicationErrored event) {
        countDownAndNotifyIfDone();
    }

    private void countDownAndNotifyIfDone() {
        countDownLatch.countDown();
        if (countDownLatch.getCount() == 0) {
            databaseSyncFinishedListener.onSynchronized();
        }
    }

}
