package ibmmobileappbuilder.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

    public static <KEY, VALUE> Map<KEY, VALUE> singleEntryMap(KEY key, VALUE value) {
        Map<KEY, VALUE> map = new HashMap<>(1);
        map.put(key, value);
        return map;
    }
}
