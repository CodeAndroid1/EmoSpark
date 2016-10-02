package ibmmobileappbuilder.cloudant.sync.datastore;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.Vector;

public class CompositeDatabaseSyncFinishedListener implements DatabaseSyncFinishedListener {

    private final Vector<WeakReference<DatabaseSyncFinishedListener>> listeners;

    public CompositeDatabaseSyncFinishedListener() {
        listeners = new Vector<>();
    }

    @Override
    public void onSynchronized() {
        for (WeakReference<DatabaseSyncFinishedListener> listenerWeakReference : listeners) {
            DatabaseSyncFinishedListener listener = listenerWeakReference.get();
            if (listener != null) {
                listener.onSynchronized();
            }
        }
    }

    public void setListeners(@NonNull DatabaseSyncFinishedListener... listeners) {
        this.listeners.clear();
        for (DatabaseSyncFinishedListener listener : listeners) {
            this.listeners.add(new WeakReference<>(listener));
        }
    }
}
