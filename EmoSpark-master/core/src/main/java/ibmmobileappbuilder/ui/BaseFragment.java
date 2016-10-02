/*
 * Copyright 2016.
 * This code is part of IBM Mobile App Builder
 */

package ibmmobileappbuilder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ibmmobileappbuilder.actions.Action;
import ibmmobileappbuilder.behaviors.Behavior;
import ibmmobileappbuilder.mvp.presenter.Presenter;

/**
 * Base fragment with common support code (bus registration, etc)
 */
public class BaseFragment extends Fragment {

    protected List<Behavior> behaviors = new ArrayList<>();
    protected Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // we want to add buttons to the actionbar
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        for (Behavior behavior : behaviors) {
            behavior.onStart();
        }
    }

    // fragment lifecygle

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.startPresenting();
        }

        for (Behavior b : behaviors) {
            b.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null) {
            presenter.stopPresenting();
        }
        for (Behavior b : behaviors) {
            b.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        for (Behavior behavior : behaviors) {
            behavior.onStop();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (Behavior b : behaviors) {
            b.onViewCreated(view, savedInstanceState);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        for (Behavior b : behaviors) {
            b.onCreateOptionsMenu(menu, inflater);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean managed = false;
        for (Behavior b : behaviors) {
            managed = managed || b.onOptionsItemSelected(item);
        }
        return managed;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Behavior b : behaviors) {
            b.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // clear resources
        behaviors.clear();
    }

    /**
     * Adds a {@link Behavior} to this fragment
     *
     * @param behavior the behavior to add to this fragment
     */
    public void addBehavior(Behavior behavior) {
        this.behaviors.add(behavior);
    }

    /**
     * Sets the {@link Presenter} instance for this fragment view
     * This is not mandatory, but calling this method will sync fragment and presenter life cycles
     *
     * @param presenter the presenter for this view
     */
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Returns the {@link Presenter} configured for this view
     *
     * @return
     */
    public Presenter getPresenter() {
        return this.presenter;
    }

    /**
     * Binds a view to an action, so that the action is executed on click event
     *
     * @param view   the view to bind the action to
     * @param action the action to bind to the view
     */
    protected void bindAction(View view, final Action action) {
        view.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        action.execute(v.getContext());
                        for (Behavior behavior : behaviors) {
                            behavior.onActionClick(action.getAnalyticsInfo());
                        }
                    }
                }
        );
    }

    /**
     * Generic event handler
     */
    public void onEvent(Object o) {
        // noop
    }
}
