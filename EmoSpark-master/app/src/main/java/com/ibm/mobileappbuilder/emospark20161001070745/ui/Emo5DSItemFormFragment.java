
package com.ibm.mobileappbuilder.emospark20161001070745.ui;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo5DSItem;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo5DSService;
import com.ibm.mobileappbuilder.emospark20161001070745.presenters.FearFormPresenter;
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
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo5DSItem;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo5DS;

public class Emo5DSItemFormFragment extends FormFragment<Emo5DSItem> {

    private CrudDatasource<Emo5DSItem> datasource;

    public static Emo5DSItemFormFragment newInstance(Bundle args){
        Emo5DSItemFormFragment fr = new Emo5DSItemFormFragment();
        fr.setArguments(args);

        return fr;
    }

    public Emo5DSItemFormFragment(){
        super();
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        // the presenter for this view
        setPresenter(new FearFormPresenter(
                (CrudDatasource) getDatasource(),
                this));

            }

    @Override
    protected Emo5DSItem newItem() {
        return new Emo5DSItem();
    }

    private Emo5DSService getRestService(){
        return Emo5DSService.getInstance();
    }

    @Override
    protected int getLayout() {
        return R.layout.fear_form;
    }

    @Override
    @SuppressLint("WrongViewCast")
    public void bindView(final Emo5DSItem item, View view) {
        
        bindString(R.id.emo5ds_quotes, item.quotes, new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                item.quotes = s.toString();
            }
        });
        
        
        bindImage(R.id.emo5ds_picture,
            item.picture != null ?
                getRestService().getImageUrl(item.picture) : null,
            0,
            new ImagePicker.Callback(){
                @Override
                public void imageRemoved(){
                    item.picture = null;
                    item.pictureUri = null;
                    ((ImagePicker) getView().findViewById(R.id.emo5ds_picture)).clear();
                }
            }
        );
        
    }

    @Override
    public Datasource<Emo5DSItem> getDatasource() {
      if (datasource != null) {
        return datasource;
      }
       datasource = Emo5DS.getInstance(new SearchOptions());
        return datasource;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            ImagePicker picker = null;
            Uri imageUri = null;
            Emo5DSItem item = getItem();

            if((requestCode & ImagePicker.GALLERY_REQUEST_CODE) == ImagePicker.GALLERY_REQUEST_CODE) {
              imageUri = data.getData();
              switch (requestCode - ImagePicker.GALLERY_REQUEST_CODE) {
                        
                        case 0:   // picture field
                            item.pictureUri = imageUri;
                            item.picture = "cid:picture";
                            picker = (ImagePicker) getView().findViewById(R.id.emo5ds_picture);
                            break;
                        
                default:
                  return;
              }

              picker.setImageUri(imageUri);
            } else if((requestCode & ImagePicker.CAPTURE_REQUEST_CODE) == ImagePicker.CAPTURE_REQUEST_CODE) {
				      switch (requestCode - ImagePicker.CAPTURE_REQUEST_CODE) {
                        
                        case 0:   // picture field
                            picker = (ImagePicker) getView().findViewById(R.id.emo5ds_picture);
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

