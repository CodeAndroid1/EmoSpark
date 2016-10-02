package ibmmobileappbuilder.analytics;

public class NetworkResponse {

    private final String url;
    private final String statusCode;
    private final String body;

    public String getUrl() {
        return url;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    private NetworkResponse(Builder builder) {
        this.url = builder.url;
        this.statusCode = builder.statusCode;
        this.body = builder.body;
    }

    public static class Builder {
        private String url;
        private String statusCode;
        private String body;

        public static Builder networkResponse() {
            return new Builder();
        }

        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withStatusCode(String statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder withBody(String body) {
            this.body = body;
            return this;
        }

        public NetworkResponse build() {
            return new NetworkResponse(this);
        }
    }
}
