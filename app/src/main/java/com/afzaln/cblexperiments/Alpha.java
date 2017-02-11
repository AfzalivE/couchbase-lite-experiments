package com.afzaln.cblexperiments;

import com.jenzz.pojobuilder.api.Builder;

import java.util.Map;

/**
 * Created by afzal on 2017-02-11.
 */

@Builder
public class Alpha extends Doc {
    String text;

    @Override
    public Map<String, Object> toHashMap() {
        Map<String, Object> hashMap = super.toHashMap();
        hashMap.put("text", text);
        return hashMap;
    }

    @Override
    String getType() {
        return "alpha";
    }

    public static Alpha fromMap(Map<String, Object> map) {
        Alpha alpha = AlphaBuilder.alpha()
                .text((String) map.get("text"))
                .build();

        alpha.created_at = (String) map.get("created_at");

        return alpha;
    }
}
