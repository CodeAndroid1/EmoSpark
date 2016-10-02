package ibmmobileappbuilder.util.image;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

public class BitmapUtil {

    private static final String[] CONTENT_ORIENTATION = new String[]{MediaStore.Images.ImageColumns.ORIENTATION};

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap rotated = Bitmap.createBitmap(bitmap,
                0,
                0,
                bitmap.getWidth(),
                bitmap.getHeight(),
                matrix,
                true
        );
        if (bitmap != rotated) {
            Log.wtf("DMV", "Bitmpa rotated");
            bitmap.recycle();
        } else {
            Log.wtf("DMV", "Bitmap NOT rotated");
        }
        return rotated;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static int getExifOrientation(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            String id = DocumentsContract.getDocumentId(uri);
            id = id.split(":")[1];
            cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    CONTENT_ORIENTATION,
                    MediaStore.Images.Media._ID + " = ?",
                    new String[]{id},
                    null
            );
            if (cursor == null || !cursor.moveToFirst()) {
                return 0;
            }
            return cursor.getInt(0);
        } catch (RuntimeException ignored) {         // If the orientation column doesn't exist, assume no rotation.
            return 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
