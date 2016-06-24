package com.ticketmaster.api.youtube;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Artur Bryzhatiy on 5/10/16.
 */
public class Utils {

    public static HashMap<String, String> jsonStringToMap(String jsonString) {
        HashMap <String, String> map = new HashMap<>();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            Iterator<?> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = (String)keys.next();
                String value = null;
                try {
                    value = jsonObject.getString(key);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (value != null) {
                    map.put(key, value);
                }
            }
        }
        return map;
    }

    public static HashMap<String, String> parametersStringToMap(String parametersString) {
        HashMap <String, String> map = new HashMap<>();

        List<String> fields = Arrays.asList(
                parametersString.split("\\&")
        );

        for (String keyValueParameter : fields) {

            String [] pair = keyValueParameter.split("=");
            if (pair.length == 2) {
                String key = pair[0];
                String value = pair[1];
                value = value.replace("+"," ");
                if (!map.containsKey(key)) {
                    map.put(key, value);
                }
            }
        }

        return map;
    }
}
