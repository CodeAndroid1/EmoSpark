package ibmmobileappbuilder.analytics.model;

import java.util.HashMap;
import java.util.Map;

public class AnalyticsInfo {

    private final String action;
    private final String target;
    private final String dataSource;

    public String getAction() {
        return action;
    }

    public String getTarget() {
        return target;
    }

    public String getDataSource() {
        return dataSource;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>(2);
        putEntryIfNotNull(map, "action", action);
        putEntryIfNotNull(map, "target", target);
        putEntryIfNotNull(map, "datasource", dataSource);
        return map;

    }

    private void putEntryIfNotNull(Map<String, String> map, String key, String value) {
        if (value != null) {
            map.put(key, value);
        }
    }

    private AnalyticsInfo(Builder builder) {
        this.action = builder.action;
        this.target = builder.target;
        this.dataSource = builder.dataSource;
    }

    public static class Builder {
        private String action;
        private String target;
        private String dataSource;

        public static Builder analyticsInfo() {
            return new Builder();
        }

        public Builder withAction(String action) {
            this.action = action;
            return this;
        }

        public Builder withTarget(String target) {
            this.target = target;
            return this;
        }

        public Builder withDataSource(String dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public AnalyticsInfo build() {
            return new AnalyticsInfo(this);
        }
    }
}
