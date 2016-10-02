package ibmmobileappbuilder;

import android.os.Parcel;
import android.os.Parcelable;

import ibmmobileappbuilder.actions.Action;

/**
 *
 */
public class MenuItem implements Parcelable {

    /**
     * Item name
     */
    String label;

    /**
     * An icon for this menu item
     */
    int iconResource;

    /**
     * an icon (external)
     */
    String iconUrl;

    /**
     * The class of the fragment represented by this menu item
     */
    Class fragmentClass;

    /**
     * The action to execute (it doesn't parcel)
     */
    Action action;

    public String getLabel() {
        return label;
    }

    public MenuItem setLabel(String label) {
        this.label = label;
        return this;
    }

    public int getIcon() {
        return iconResource;
    }

    public MenuItem setIcon(int imgRes) {
        this.iconResource = imgRes;
        return this;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public MenuItem setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
        return this;
    }

    public Action getAction() {
        return action;
    }

    public MenuItem setAction(Action action) {
        this.action = action;
        return this;
    }

    public Class getFragmentClass() {
        return fragmentClass;
    }

    public MenuItem setFragmentClass(Class fragmentClass) {
        this.fragmentClass = fragmentClass;
        return this;
    }

    public String toString() {
        return this.label;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(label);
        parcel.writeInt(iconResource);
        parcel.writeString(iconUrl);
    }

    public static final Creator CREATOR = new Creator() {

        @Override
        public Object createFromParcel(Parcel parcel) {
            return new MenuItem()
                    .setLabel(parcel.readString())
                    .setIcon(parcel.readInt())
                    .setIconUrl(parcel.readString());
        }

        @Override
        public Object[] newArray(int size) {
            return new MenuItem[size];
        }
    };
}
