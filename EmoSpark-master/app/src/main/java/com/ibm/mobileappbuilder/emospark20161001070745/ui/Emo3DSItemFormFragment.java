
package com.ibm.mobileappbuilder.emospark20161001070745.ui;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo3DSItem;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo3DSService;
import com.ibm.mobileappbuilder.emospark20161001070745.presenters.DepressedFormPresenter;
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
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo3DSItem;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo3DS;

public class Emo3DSItemFormFragment extends FormFragment<Emo3DSItem> {

    private CrudDatasource<Emo3DSItem> datasource;

    public static Emo3DSItemFormFragment newInstance(Bundle args){
        Emo3DSItemFormFragment fr = new Emo3DSItemFormFragment();
        fr.setArguments(args);

        return fr;
    }

    public Emo3DSItemFormFragment(){
        super();
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        // the presenter for this view
        setPresenter(new DepressedFormPresenter(
                (CrudDatasource) getDatasource(),
                this));

            }

    @Override
    protected Emo3DSItem newItem() {
        return new Emo3DSItem();
    }

    private Emo3DSService getRestService(){
        return Emo3DSService.getInstance();
    }

    @Override
    protected int getLayout() {
        return R.layout.depressed_form;
    }

    @Override
    @SuppressLint("WrongViewCast")
    public void bindView(final Emo3DSItem item, View view) {
        
        bindString(R.id.emo3ds_quotes, item.quotes, new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                item.quotes = s.toString();
            }
        });
        
        
        bindImage(R.id.emo3ds_picture,
            item.picture != null ?
                getRestService().getImageUrl(item.picture) : null,
            0,
            new ImagePicker.Callback(){
                @Override
                public void imageRemoved(){
                    item.picture = null;
                    item.pictureUri = null;
                    ((ImagePicker) getView().findViewById(R.id.emo3ds_picture)).clear();
                }
            }
        );
        
    }

    @Override
    public Datasource<Emo3DSItem> getDatasource() {
      if (datasource != null) {
        return datasource;
      }
       datasource = Emo3DS.getInstance(new SearchOptions());
        return datasource;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            ImagePicker picker = null;
            Uri imageUri = null;
            Emo3DSItem item = getItem();

            if((requestCode & ImagePicker.GALLERY_REQUEST_CODE) == ImagePicker.GALLERY_REQUEST_CODE) {
              imageUri = data.getData();
              switch (requestCode - ImagePicker.GALLERY_REQUEST_CODE) {
                        
                        case 0:   // picture field
                            item.pictureUri = imageUri;
                            item.picture = "cid:picture";
                            picker = (ImagePicker) getView().findViewById(R.id.emo3ds_picture);
                            break;
                        
                default:
                  return;
              }

              picker.setImageUri(imageUri);
            } else if((requestCode & ImagePicker.CAPTURE_REQUEST_CODE) == ImagePicker.CAPTURE_REQUEST_CODE) {
				      switch (requestCode - ImagePicker.CAPTURE_REQUEST_CODE) {
                        
                        case 0:   // picture field
                            picker = (ImagePicker) getView().findViewById(R.id.emo3ds_picture);
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

