/*
 * Copyright 2016.
 * This code is part of IBM Mobile App Builder
 */

package ibmmobileappbuilder.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.SwitchCompat;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.net.URL;
import java.util.Date;

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.ds.restds.GeoPoint;
import ibmmobileappbuilder.mvp.presenter.FormPresenter;
import ibmmobileappbuilder.mvp.view.FormView;
import ibmmobileappbuilder.util.Constants;
import ibmmobileappbuilder.validation.Validator;
import ibmmobileappbuilder.views.DatePicker;
import ibmmobileappbuilder.views.DateTimePicker;
import ibmmobileappbuilder.views.GeoPicker;
import ibmmobileappbuilder.views.ImagePicker;
import ibmmobileappbuilder.views.TristateBooleanPicker;

/**
 * Fragments to show a edit form (edit or create mode)
 * It will allow to add validations
 *
 * DetailFragments expect the {@link Constants#CONTENT} to be passed in
 * That argument is optional, given that you implement the {@link #getDatasource()}
 * method.
 */
public abstract class FormFragment<T>
        extends DetailFragment<T> implements FormView<T>{

    public static final String IS_UPDATING_KEY = "_isupdating_";

    /**
     * The form mode (EDIT/CREATE)
     */
    private int mode;
    private ProgressDialog mProgressDialog;

    public FormFragment() {
        super();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle state) {
        super.onCreate(state);
        Bundle args = getArguments();

        mode = args.getInt(Constants.MODE);

        boolean isUpdating = state != null && state.getBoolean(IS_UPDATING_KEY);
        if(isUpdating)
            showProgress();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save state
        outState.putBoolean(IS_UPDATING_KEY, isUpdating());
    }

    @Override
    protected void initView(View view, Bundle state){
        // Overrided to enable editing

        if(mode == Constants.MODE_EDIT) {
            if (item != null) {
                // show the form with inital data
                bindView(item, view);
                setContentShown(true);
            } else {
                // ask datasource for data
                refresh();
            }
        }
        else {
            // no data to display. We'll show the plain form
            item = newItem();
            bindView(item, view);
            setContentShown(true);
        }
    }

    // Create and handle "SAVE" menu item

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(Menu.NONE, R.id.action_save, Menu.FIRST, android.R.string.ok)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save){
            showProgress();
            if(mode == Constants.MODE_EDIT)
                getPresenter().save(getItem());
            else
                getPresenter().create(getItem());
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgress() {
        mProgressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.saving_changes), true);
    }

    @Override
    public FormPresenter<T> getPresenter(){
        return (FormPresenter<T>) super.getPresenter();
    }

    @Override
    public void close(boolean shouldRefresh) {
        dismissPendingProgress();
        // TODO: Support tablet layouts
        Intent data = new Intent();
        data.putExtra(Constants.CONTENT, (Parcelable) getItem());
        getActivity().setResult(
                shouldRefresh ?
                        Constants.CONTENT_UPDATED : Constants.CONTENT_NOT_UPDATED,
                data);
        getActivity().finish();
    }

    @Override
    public void navigateToEditForm() {
        // do nothing, since we already are in edit form
    }

    @Override
    public void showMessage(int message, boolean toast) {
        super.showMessage(message, toast);
        dismissPendingProgress();
    }



    protected void dismissPendingProgress() {
        // cancel any progress dialog
        if(isUpdating())
            mProgressDialog.dismiss();
    }

    private boolean isUpdating() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    // Binding methods

    public void bindBoolean(int viewId, Boolean value, View.OnClickListener listener){
        SwitchCompat view = (SwitchCompat) getView().findViewById(viewId);
        view.setChecked(value != null ? value : false);
        view.setOnClickListener(listener);

        // initialize value
        if(value == null)
            listener.onClick(view);
    }

    public void bindNullableBoolean(int viewId, Boolean value, TristateBooleanPicker.ChoiceListener listener){
        TristateBooleanPicker view = (TristateBooleanPicker) getView().findViewById(viewId);
        view.setValue(value);
        view.setListener(listener);
    }

    // Binding utilities

    public void bindLong(int viewId, Long value, TextWatcher listener){
        bindLong(viewId, value, listener, null);
    }

    public void bindLong(int viewId, Long value, TextWatcher listener, Validator<T> validator){
        String strVal = value != null ? String.valueOf(value) : null;
        bindString(viewId, strVal, listener, validator);
    }

    public void bindDouble(int viewId, Double value, TextWatcher listener){
        bindDouble(viewId, value, listener, null);
    }

    public void bindDouble(int viewId, Double value, TextWatcher listener, Validator<T> validator){
        String strVal = value != null ? String.valueOf(value) : null;
        bindString(viewId, strVal, listener, validator);
    }

    public void bindString(int viewId, String value, TextWatcher listener){
        bindString(viewId, value, listener, null);
    }

    public void bindString(int viewId, String value, TextWatcher listener, Validator<T> validator){
        EditText view = (EditText) getView().findViewById(viewId);
        view.setText(value);
        view.addTextChangedListener(listener);
        if(validator != null)
            getPresenter().addValidator(viewId, validator);
    }

    public void bindDatePicker(int viewId, Date value, DatePicker.DateSelectedListener listener){
        bindDatePicker(viewId, value, listener, null);
    }

    public void bindDatePicker(int viewId, Date value, DatePicker.DateSelectedListener listener, Validator<T> validator){
        DatePicker view = (DatePicker) getView().findViewById(viewId);
        view.setDate(value);
        view.setListener(listener);
        if(validator != null)
            getPresenter().addValidator(viewId, validator);
    }

    public void bindDateTimePicker(int viewId, Date value, DateTimePicker.DateSelectedListener listener){
        bindDateTimePicker(viewId, value, listener, null);
    }

    public void bindDateTimePicker(int viewId, Date value, DateTimePicker.DateSelectedListener listener, Validator<T> validator){
        DateTimePicker view = (DateTimePicker) getView().findViewById(viewId);
        view.setDateTime(value);
        view.setDateTimeSelectedListener(listener);
        if(validator != null)
            getPresenter().addValidator(viewId, validator);
    }

    public void bindImage(int viewId, URL value, int index, ImagePicker.Callback callback) {
        this.bindImage(viewId, value, index, null, callback);
    }

    public void bindImage(int viewId, URL value, int index, Validator<T> validator, ImagePicker.Callback callback){
        ImagePicker picker = (ImagePicker) getView().findViewById(viewId);
        picker.setTargetFragment(this);

        if(callback != null)
            picker.setCallback(callback);

        // initial value
        if (value != null)
            picker.setImageUrl(value.toExternalForm());

        if (validator != null)
            getPresenter().addValidator(viewId, validator);
    }

    public void bindLocation(int viewId, GeoPoint value, GeoPicker.PointChangedListener listener){
        bindLocation(viewId, value, listener, null);
    }

    public void bindLocation(int viewId, GeoPoint value, GeoPicker.PointChangedListener listener, Validator<T> validator){
        GeoPicker view = (GeoPicker) getView().findViewById(viewId);
        view.setPoint(value);
        view.setListener(listener);
        if(validator != null)
            getPresenter().addValidator(viewId, validator);
    }

    /**
     * Creates a new item for editing
     * @return a new item.
     */
    protected abstract T newItem();
}
