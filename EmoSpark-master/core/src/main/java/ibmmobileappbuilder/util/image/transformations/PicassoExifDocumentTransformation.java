package ibmmobileappbuilder.util.image.transformations;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;

import com.squareup.picasso.Transformation;

import ibmmobileappbuilder.util.image.BitmapUtil;

public class PicassoExifDocumentTransformation implements Transformation {
    final Context context;
    final Uri uri;

    public PicassoExifDocumentTransformation(Context context, Uri uri) {
        this.context = context;
        this.uri = uri;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return source;
        }
        if (!DocumentsContract.isDocumentUri(context, uri)) {
            return source;
        }
        int exifRotation = BitmapUtil.getExifOrientation(context, uri);
        if (exifRotation != 0) {
            return BitmapUtil.rotateBitmap(source, exifRotation);
        }
        return source;
    }

    @Override
    public String key() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT ||
                !DocumentsContract.isDocumentUri(context, uri)) {
            return "documentTransform()";
        }
        return "documentExifTransform(" + DocumentsContract.getDocumentId(uri) + ")";
    }

}