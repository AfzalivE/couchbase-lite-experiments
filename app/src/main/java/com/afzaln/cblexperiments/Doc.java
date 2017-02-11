package com.afzaln.cblexperiments;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by afzal on 2017-02-11.
 */
public abstract class Doc {
    String created_at;

    public Doc() {
        Date date = Calendar.getInstance().getTime();
        created_at = Utils.sDateFormatter.format(date);
    }

    public Map<String, Object> toHashMap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", getType());
        hashMap.put("created_at", created_at);

        return hashMap;
    }

    abstract String getType();
}
