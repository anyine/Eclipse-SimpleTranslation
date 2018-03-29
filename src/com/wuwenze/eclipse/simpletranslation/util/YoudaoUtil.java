package com.wuwenze.eclipse.simpletranslation.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public final class YoudaoUtil {
    private final static String youdaoHost = "http://openapi.youdao.com/api";
    
    public static String translate(String appKey,String appSecret,String sourceText) {
        try {
            String salt = String.valueOf(System.currentTimeMillis());
            String sign = Md5Util.md5(appKey + sourceText + salt + appSecret);
            Map<String, String> params = new HashMap<>();
            params.put("q", sourceText);
            params.put("from", "AUTO");
            params.put("to", "AUTO");
            params.put("appKey", appKey);
            params.put("salt", salt);
            params.put("sign", sign);
            return HttpUtil.get(youdaoHost, params);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String analysis(String translate) {
        List<String> translation = new ArrayList<String>();
        Map<String, Object> map = JSON.parseObject(translate, Map.class);
        if ("0".equals(map.get("errorCode").toString())) {
            translation = JSON.parseObject(map.get("translation").toString(), List.class);
        }

        StringBuffer stringBuffer = new StringBuffer();
        for (Object o : translation) {
            stringBuffer.append(o).append("\r");
        }
        return stringBuffer.length() > 0 ? stringBuffer.toString() : "Please check YOUDAO_APP_KEY / YOUDAO_APP_SECRET\n[Window -> Preference -> SimpleTranslation]";
    }
}
