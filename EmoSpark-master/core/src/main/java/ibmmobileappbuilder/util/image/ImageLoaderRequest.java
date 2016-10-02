package ibmmobileappbuilder.util.image;

import android.net.Uri;
import android.support.annotation.AnyRes;
import android.widget.ImageView;

import ibmmobileappbuilder.core.R;

public class ImageLoaderRequest {

    private final String path;
    private final Uri uri;
    private @AnyRes final int resourceToLoad;
    private @AnyRes final int placeholder;
    private @AnyRes final int error;
    private final boolean fit;
    private final boolean debug;
    private final ImageView targetView;

    private ImageLoaderRequest(Builder builder) {
        this.path = builder.path;
        this.uri = builder.uri;
        this.placeholder = builder.placeholder;
        this.error = builder.error;
        this.fit = builder.fit;
        this.debug = builder.debug;
        this.targetView = builder.targetView;
        this.resourceToLoad = builder.resourceToLoad;
    }

    public String getPath() {
        return path;
    }

    public Uri getUri() {
        return uri;
    }

    public @AnyRes int getResourceToLoad() {
        return resourceToLoad;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public int getError() {
        return error;
    }

    public boolean shouldFit() {
        return fit;
    }

    public boolean isDebugging() {
        return debug;
    }

    public ImageView getTargetView() {
        return targetView;
    }

    public static class Builder {
        private String path;
        private Uri uri;
        private int placeholder = R.color.ima_il_placeholder;
        private int error = R.drawable.ima_il_error;
        private boolean fit;
        private boolean debug;
        private ImageView targetView;
        private int resourceToLoad;

        public static Builder imageLoaderRequest() {
            return new Builder();
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public Builder withUri(Uri uri) {
            this.uri = uri;
            return this;
        }

        public Builder withPlaceholder(@AnyRes int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Builder withError(@AnyRes int error) {
            this.error = error;
            return this;
        }

        public Builder fit() {
            this.fit = true;
            return this;
        }

        public Builder debugging() {
            debug = true;
            return this;
        }

        public Builder withTargetView(ImageView targetView) {
            this.targetView = targetView;
            return this;
        }

        public Builder withResourceToLoad(@AnyRes int resourceToLoad) {
            this.resourceToLoad = resourceToLoad;
            return this;
        }

        public ImageLoaderRequest build() {
            return new ImageLoaderRequest(this);
        }
    }
}
