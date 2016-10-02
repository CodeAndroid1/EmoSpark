package ibmmobileappbuilder.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.dialogs.ImagePickerOptionsDialog;
import ibmmobileappbuilder.util.image.ImageLoader;
import ibmmobileappbuilder.util.image.PicassoImageLoader;

import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static ibmmobileappbuilder.util.image.ImageLoaderRequest.Builder.imageLoaderRequest;

public class ImagePicker extends LinearLayout implements View.OnClickListener, ImagePickerOptionsDialog.OnOptionSelectedListener {
    public static final int GALLERY_REQUEST_CODE = 1024;
    public static final int CAPTURE_REQUEST_CODE = 2048;
    public static final String FILE_PICKER_TAG = "FilePicker";
    private static final String SUFFIX_JPG = ".jpg";

    private final ImageLoader imageLoader;

    private final TextView errorView;
    private final ImageView imageView;
    private ImagePickerOptionsDialog imagePickerDialog;
    private final FragmentManager fragmentManager;
    private Fragment fragment;
    private final int index;
    private boolean hasImage;
    private Callback callback;
    private File imageFile;

    public ImagePicker(Context context) {
        this(context, null);
    }

    public ImagePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        imageLoader = new PicassoImageLoader(context);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ImagePicker, 0, 0
        );
        String label = a.getString(R.styleable.ImagePicker_label);

        index = a.getInt(R.styleable.ImagePicker_index, 0);

        a.recycle();

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.file_picker, this, true);

        errorView = (TextView) findViewById(R.id.error);
        TextView labelView = (TextView) findViewById(R.id.label);
        labelView.setText(label);

        imageView = (ImageView) findViewById(R.id.image);

        labelView.setOnClickListener(this);
        imageView.setOnClickListener(this);

        // Try to restore previous suspended fragment
        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
        imagePickerDialog = (ImagePickerOptionsDialog) fragmentManager.findFragmentByTag(
                FILE_PICKER_TAG
        );
        if (imagePickerDialog == null) {
            imagePickerDialog = new ImagePickerOptionsDialog();
        }
    }

    public void setImageBitmap(Bitmap bm) {
        hasImage = true;
        this.imageView.setImageBitmap(bm);
    }

    public void setImageUrl(@NonNull String url) {
        hasImage = true;
        imageLoader.load(imageLoaderRequest().withPath(url).withTargetView(imageView).fit().build());
    }

    public void setImageUri(@NonNull Uri uri) {
        hasImage = true;
        imageLoader.load(imageLoaderRequest().withUri(uri).withTargetView(imageView).fit().build());
    }

    public void setTargetFragment(Fragment fr) {
        fragment = fr;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void clear() {
        hasImage = false;
        imageLoader.load(imageLoaderRequest()
                        .withResourceToLoad(R.drawable.ic_image_photo)
                        .withTargetView(imageView)
                        .build()
        );
    }

    @Override
    public void onClick(View v) {
        if (hasImage) {
            imagePickerDialog.setRemoveEnabled(true);
        }
        imagePickerDialog.setListener(this);
        imagePickerDialog.show(fragmentManager, FILE_PICKER_TAG);
    }

    @Override
    public void fromStorage() {
        // Choose from folder
        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileIntent.setType("image/*");

        // picked image will be received in the container fragment
        fragment.startActivityForResult(
                Intent.createChooser(fileIntent, null),
                GALLERY_REQUEST_CODE + index
        );
    }

    @Override
    public void fromCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(createImageFile()));
        // picked image will be received in the container fragment
        fragment.startActivityForResult(
                Intent.createChooser(cameraIntent, null),
                CAPTURE_REQUEST_CODE + index
        );
    }

    private File createImageFile() {
        if (imageFile == null) {
            String imageFileName = "BU_" + UUID.randomUUID();
            File storageDir = getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
            try {
                imageFile = File.createTempFile(imageFileName, SUFFIX_JPG, storageDir);
            } catch (IOException e) {
                throw new RuntimeException("Could not create image");
            }
        }

        return imageFile;
    }

    @Override
    public void remove() {
        if (callback != null) {
            callback.imageRemoved();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        return new ImagePickerSavedState(parcelable, imageFile);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        ImagePickerSavedState savedState = (ImagePickerSavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        imageFile = savedState.getImageFile();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setError(int errorRes) {
        setError(getContext().getString(errorRes));
    }

    public void setError(CharSequence errorMsg) {
        if (errorMsg != null) {
            if (errorView.getVisibility() == GONE) {
                errorView.setText(errorMsg);
                errorView.setAlpha(0.0F);
                errorView.setVisibility(VISIBLE);
                errorView.animate()
                        .alpha(1.0F)
                        .setDuration(200L)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setListener(null).start();
            }
        } else {
            errorView.animate()
                    .alpha(0.0F)
                    .setDuration(200L)
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                                     @Override
                                     public void onAnimationEnd(Animator animation) {
                                         errorView.setText(null);
                                         errorView.setVisibility(GONE);
                                     }
                                 }
                    );
        }
    }

    public interface Callback {
        void imageRemoved();
    }

    static class ImagePickerSavedState extends BaseSavedState {
        private final File mImageFile;

        public ImagePickerSavedState(Parcelable parcelable, File imageFile) {
            super(parcelable);
            this.mImageFile = imageFile;
        }

        private ImagePickerSavedState(Parcel in) {
            super(in);
            mImageFile = (File) in.readSerializable();
        }

        public File getImageFile() {
            return mImageFile;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeSerializable(mImageFile);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<ImagePickerSavedState> CREATOR =
                new Parcelable.Creator<ImagePickerSavedState>() {
                    public ImagePickerSavedState createFromParcel(Parcel in) {
                        return new ImagePickerSavedState(in);
                    }

                    public ImagePickerSavedState[] newArray(int size) {
                        return new ImagePickerSavedState[size];
                    }
                };
    }
}
