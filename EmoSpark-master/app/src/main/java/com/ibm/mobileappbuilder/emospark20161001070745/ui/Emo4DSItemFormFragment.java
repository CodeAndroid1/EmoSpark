
package com.ibm.mobileappbuilder.emospark20161001070745.ui;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo4DSItem;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo4DSService;
import com.ibm.mobileappbuilder.emospark20161001070745.presenters.AngryFormPresenter;
import com.ibm.mobileappbuilder.emospark20161001070745.R;
import ibmmobileappbuilder.ui.FormFragment;
import ibmmobileappbuilder.util.StringUtils;
import ibmmobileappbuilder.views.ImagePicker;
import ibmmobileappbuilder.views.TextWatcherAdapter;
import java.io.IOException;
import java.io.File;

import static android.net.Uri.fromFile;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.ds.CrudDatasource;
import ibmmobileappbuilder.ds.SearchOptions;
import ibmmobileappbuilder.ds.filter.Filter;
import java.util.Arrays;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo4DSItem;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo4DS;

public class Emo4DSItemFormFragment extends FormFragment<Emo4DSItem> {

    private CrudDatasource<Emo4DSItem> datasource;

    public static Emo4DSItemFormFragment newInstance(Bundle args){
        Emo4DSItemFormFragment fr = new Emo4DSItemFormFragment();
        fr.setArguments(args);

        return fr;
    }

    public Emo4DSItemFormFragment(){
        super();
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        // the presenter for this view
        setPresenter(new AngryFormPresenter(
                (CrudDatasource) getDatasource(),
                this));

            }

    @Override
    protected Emo4DSItem newItem() {
        return new Emo4DSItem();
    }

    private Emo4DSService getRestService(){
        return Emo4DSService.getInstance();
    }

    @Override
    protected int getLayout() {
        return R.layout.angry_form;
    }

    @Override
    @SuppressLint("WrongViewCast")
    public void bindView(final Emo4DSItem item, View view) {
        
        bindString(R.id.emo4ds_quotes, item.quotes, new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                item.quotes = s.toString();
            }
        });
        
        
        bindImage(R.id.emo4ds_picture,
            item.picture != null ?
                getRestService().getImageUrl(item.picture) : null,
            0,
            new ImagePicker.Callback(){
                @Override
                public void imageRemoved(){
                    item.picture = null;
                    item.pictureUri = null;
                    ((ImagePicker) getView().findViewById(R.id.emo4ds_picture)).clear();
                }
            }
        );
        
    }

    @Override
    public Datasource<Emo4DSItem> getDatasource() {
      if (datasource != null) {
        return datasource;
      }
       datasource = Emo4DS.getInstance(new SearchOptions());
        return datasource;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            ImagePicker picker = null;
            Uri imageUri = null;
            Emo4DSItem item = getItem();

            if((requestCode & ImagePicker.GALLERY_REQUEST_CODE) == ImagePicker.GALLERY_REQUEST_CODE) {
              imageUri = data.getData();
              switch (requestCode - ImagePicker.GALLERY_REQUEST_CODE) {
                        
                        case 0:   // picture field
                            item.pictureUri = imageUri;
                            item.picture = "cid:picture";
                            picker = (ImagePicker) getView().findViewById(R.id.emo4ds_picture);
                            break;
                        
                default:
                  return;
              }

              picker.setImageUri(imageUri);
            } else if((requestCode & ImagePicker.CAPTURE_REQUEST_CODE) == ImagePicker.CAPTURE_REQUEST_CODE) {
				      switch (requestCode - ImagePicker.CAPTURE_REQUEST_CODE) {
                        
                        case 0:   // picture field
                            picker = (ImagePicker) getView().findViewById(R.id.emo4ds_picture);
                            imageUri = fromFile(picker.getImageFile());
                        		item.pictureUri = imageUri;
                            item.picture = "cid:picture";
                            break;
                        
                default:
                  return;
              }
              picker.setImageUri(imageUri);
            }
        }
    }
}

