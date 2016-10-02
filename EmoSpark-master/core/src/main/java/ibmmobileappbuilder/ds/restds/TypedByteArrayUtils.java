package ibmmobileappbuilder.ds.restds;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import ibmmobileappbuilder.util.image.BitmapUtil;
import retrofit.mime.TypedByteArray;

import static android.media.ExifInterface.TAG_ORIENTATION;
import static ibmmobileappbuilder.analytics.injector.AnalyticsReporterInjector.analyticsReporter;
import static ibmmobileappbuilder.injectors.ApplicationInjector.getApplicationContext;
import static ibmmobileappbuilder.util.image.BitmapUtil.rotateBitmap;

public class TypedByteArrayUtils {
    private static final Random RANDOM = new Random();

    @Nullable
    public static TypedByteArray fromBitmapPath(String bitmapPath) {
        if (bitmapPath == null) {
            return null;
        }

        Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ExifInterface exifInterface = new ExifInterface(bitmapPath);
            bitmap = rotateBitmapIfRequired(bitmap,
                    exifInterface.getAttributeInt(TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            );
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream);
        } catch (IOException e) {
            analyticsReporter(getApplicationContext()).sendHandledException("TypedByteArrayUtils",
                    "IOException",
                    e
            );
            return null;
        }
        return new TypedByteArrayWithFilename("image/jpeg", stream.toByteArray(), getTempName());
    }

    @Nullable
    public static TypedByteArray fromUri(Uri bitmapUri) {
        if (bitmapUri == null) {
            return null;
        }
        String path = bitmapUri.getPath();
        return new File(path).exists() ? fromBitmapPath(path) : fromGalleryUri(bitmapUri);
    }

    @Nullable
    private static TypedByteArray fromGalleryUri(Uri bitmapUri) {
        if (bitmapUri == null) {
            return null;
        }

        InputStream inputStream;
        try {
            inputStream = getApplicationContext().getContentResolver().openInputStream(
                    bitmapUri
            );
            int exifOrientation = BitmapUtil.getExifOrientation(getApplicationContext(), bitmapUri);
            return fromBitmap(rotateBitmap(BitmapFactory.decodeStream(inputStream),
                            exifOrientation
                    )
            );
        } catch (IOException e) {
            analyticsReporter(getApplicationContext()).sendHandledException("TypedByteArrayUtils",
                    "IOException",
                    e
            );
            return null;
        }
    }

    @Nullable
    private static TypedByteArray fromBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream);
        return new TypedByteArrayWithFilename("image/jpeg", stream.toByteArray(), getTempName());
    }

    private static Bitmap rotateBitmapIfRequired(Bitmap bitmap, int exifOrientation) {
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateBitmap(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateBitmap(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateBitmap(bitmap, 270);
            default:
                return bitmap;

        }
    }


    private static String getTempName() {
        return "image" + RANDOM.nextInt() + ".jpg";
    }
}
