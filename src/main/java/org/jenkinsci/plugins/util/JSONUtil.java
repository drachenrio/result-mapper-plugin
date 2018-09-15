package org.jenkinsci.plugins.util;

import org.json.JSONObject;

public class JSONUtil {

    public static Boolean getBoolean(JSONObject obj, String key) {
        try {
            return obj.getBoolean(key);
        } catch (Exception e) {
            return null;
        }
    }

    public static JSONObject getJSONObject(JSONObject obj, String key) {
        try {
            return obj.getJSONObject(key);
        } catch (Exception e) {
            return null;
        }
    }
}
