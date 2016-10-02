
package com.ibm.mobileappbuilder.emospark20161001070745.ds;
import android.graphics.Bitmap;
import android.net.Uri;

import ibmmobileappbuilder.mvp.model.IdentifiableBean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Emo1DSItem implements Parcelable, IdentifiableBean {

    @SerializedName("quotes") public String quotes;
    @SerializedName("picture") public String picture;
    @SerializedName("id") public String id;
    @SerializedName("pictureUri") public transient Uri pictureUri;

    @Override
    public String getIdentifiableId() {
      return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quotes);
        dest.writeString(picture);
        dest.writeString(id);
    }

    public static final Creator<Emo1DSItem> CREATOR = new Creator<Emo1DSItem>() {
        @Override
        public Emo1DSItem createFromParcel(Parcel in) {
            Emo1DSItem item = new Emo1DSItem();

            item.quotes = in.readString();
            item.picture = in.readString();
            item.id = in.readString();
            return item;
        }

        @Override
        public Emo1DSItem[] newArray(int size) {
            return new Emo1DSItem[size];
        }
    };

}


