package com.afzaln.cblexperiments;

import com.jenzz.pojobuilder.api.Builder;

import java.util.Map;

/**
 * Created by afzal on 2017-02-11.
 */

@Builder
public class Beta extends Doc {
    int num;

    @Override
    public Map<String, Object> toHashMap() {
        Map<String, Object> hashMap = super.toHashMap();
        hashMap.put("num", num);
        return hashMap;
    }

    @Override
    String getType() {
        return "beta";
    }
}
