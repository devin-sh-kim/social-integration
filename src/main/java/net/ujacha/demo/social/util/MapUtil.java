package net.ujacha.demo.social.util;

import java.util.Map;

public class MapUtil {

    public static  <T> T get(Map<String, Object> map, String key, Class<T> returnAs){

        Object o = map.get(key);
        return returnAs.cast(o);

    }


}
