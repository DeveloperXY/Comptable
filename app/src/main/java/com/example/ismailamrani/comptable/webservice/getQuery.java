package com.example.ismailamrani.comptable.webservice;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Redouane on 24/03/2016.
 */
public class getQuery {

    public String getQuery(Map<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        return result.toString();
    }

}
