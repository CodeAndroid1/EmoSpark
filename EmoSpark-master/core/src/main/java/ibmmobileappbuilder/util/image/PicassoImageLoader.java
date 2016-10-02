package ibmmobileappbuilder.util.image;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import ibmmobileappbuilder.util.image.transformations.PicassoExifDocumentTransformation;

public class PicassoImageLoader implements ImageLoader {

    private static final String TAG = PicassoImageLoader.class.getSimpleName();
    private final Picasso picasso;
    private final Context context;

    public PicassoImageLoader(Context context) {
        this.context = context;
        this.picasso = Picasso.with(context);
    }

    @Override
    public void load(ImageLoaderRequest request) {
        picasso.setIndicatorsEnabled(request.isDebugging());
        RequestCreator requestCreator;
        Uri requestUri = request.getUri();
        if (request.getPath() != null) {
            requestCreator = picasso.load(request.getPath());
        } else if (requestUri != null) {
            requestCreator = picasso
                    .load(requestUri)
                    .transform(new PicassoExifDocumentTransformation(context, requestUri));
        } else if (request.getResourceToLoad() != 0) {
            requestCreator = picasso.load(request.getResourceToLoad());
        } else {
            requestCreator = picasso.load((String) null);
            Log.w(TAG, "Attempted to load image without a valid source.");
        }

        if (request.shouldFit()) {
            requestCreator.centerInside();
            requestCreator.fit();
        }

        requestCreator
                .placeholder(request.getPlaceholder())
                .error(request.getError())
                .into(request.getTargetView());
    }
}
