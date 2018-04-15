package com.wuwenze.eclipse.simpletranslation.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.JSON;
import com.wuwenze.eclipse.simpletranslation.pojo.Constants;
import com.wuwenze.eclipse.simpletranslation.pojo.YoudaoApiRet;

public final class YoudaoUtil implements Constants {
	private static Map<String, String> mErrorCodeMessageMap = new HashMap<>();
	static {
		mErrorCodeMessageMap.put("101",	"缺少必填的参数，出现这个情况还可能是et的值和实际加密方式不对应");
		mErrorCodeMessageMap.put("102",	"不支持的语言类型");
		mErrorCodeMessageMap.put("103",	"翻译文本过长");
		mErrorCodeMessageMap.put("104",	"不支持的API类型");
		mErrorCodeMessageMap.put("105",	"不支持的签名类型");
		mErrorCodeMessageMap.put("106",	"不支持的响应类型");
		mErrorCodeMessageMap.put("107",	"不支持的传输加密类型");
		mErrorCodeMessageMap.put("108",	"appKey无效，注册账号， 登录后台创建应用和实例并完成绑定， 可获得应用ID和密钥等信息，其中应用ID就是appKey（ 注意不是应用密钥）");
		mErrorCodeMessageMap.put("109",	"batchLog格式不正确");
		mErrorCodeMessageMap.put("110",	"无相关服务的有效实例");
		mErrorCodeMessageMap.put("111",	"开发者账号无效");
		mErrorCodeMessageMap.put("113",	"q不能为空");
		mErrorCodeMessageMap.put("201",	"解密失败，可能为DES,BASE64,URLDecode的错误");
		mErrorCodeMessageMap.put("202",	"签名检验失败");
		mErrorCodeMessageMap.put("203",	"访问IP地址不在可访问IP列表");
		mErrorCodeMessageMap.put("301",	"辞典查询失败");
		mErrorCodeMessageMap.put("302",	"翻译查询失败");
		mErrorCodeMessageMap.put("303",	"服务端的其它异常");
		mErrorCodeMessageMap.put("401",	"账户已经欠费");
		mErrorCodeMessageMap.put("411",	"访问频率受限,请稍后访问");
		mErrorCodeMessageMap.put("412",	"长请求过于频繁，请稍后访问");
		mErrorCodeMessageMap.put("10086", MSG_API_NO_RESPONSE);
		mErrorCodeMessageMap.put("10010", MSG_NO_APP_KEY_AND_SECRET);
	}
	
    public static String translate(String appKey,String appSecret,String from, String to, String query) {
    	if (StringUtil.isEmpty(appKey) || StringUtil.isEmpty(appSecret)) {
    		return errorResult("10010");
    	}
        try {
            String salt = String.valueOf(System.currentTimeMillis());
            String sign = Md5Util.md5(appKey + query + salt + appSecret);
            Map<String, String> params = new HashMap<>();
            params.put("q", query);
            params.put("from", from);
            params.put("to", to);
            params.put("appKey", appKey);
            params.put("salt", salt);
            params.put("sign", sign);
            String retJsonString = HttpUtil.get(YOUDAO_API_HOST, params);
            if (!StringUtil.isEmpty(retJsonString)) {
            	YoudaoApiRet ret = JSON.parseObject(retJsonString, YoudaoApiRet.class);
            	if ("0".equals(ret.getErrorCode())) {
            		return analysisResult(ret);
            	}
            	return errorResult(ret.getErrorCode());
            }
        } catch (Exception e) {
            return errorResult(null, e.getMessage());
        }
        return errorResult("10086");
    }
    
    private static String errorResult(String errorCode) {
    	return errorResult(errorCode, null);
    }
    
    private static String errorResult(String errorCode, String defaultMsg) {
    	return new StringBuilder()
    			.append("[出现问题]\n")
    			.append(StringUtil.isEmpty(defaultMsg) ? mErrorCodeMessageMap.getOrDefault(errorCode, "Unknown error") : defaultMsg)
    			.toString();
    }

	private static String analysisResult(YoudaoApiRet ret) {
		boolean haveWeb = null != ret.getWeb() && !ret.getWeb().isEmpty();
		boolean haveBasic = null != ret.getBasic() && null != ret.getBasic().getExplains() && !ret.getBasic().getExplains().isEmpty();
		
		StringBuilder stringBuilder = new StringBuilder();
		// 翻译结果
		if (haveBasic || haveWeb) {
			stringBuilder.append("[翻译结果]\n");
		}
		int translationSize = ret.getTranslation().size();
		AtomicInteger translationCount = new AtomicInteger(1);
		ret.getTranslation().forEach((t) -> {
			stringBuilder.append(t);
			if (translationSize > 1 && translationCount.get() != translationSize) {
				stringBuilder.append("\n");
			}
			translationCount.getAndIncrement();
		});
		
		if (haveBasic) {
			// 词典
			stringBuilder.append("\n\n[有道词典]\n");
			AtomicInteger explainsCount = new AtomicInteger(1);
			int explainsSize = ret.getBasic().getExplains().size();
			ret.getBasic().getExplains().forEach((e) -> {
				stringBuilder.append(e);
				if (explainsSize > 1 && explainsCount.get() != explainsSize) {
					stringBuilder.append("\n");
				}
				explainsCount.getAndIncrement();
			});
		}
		
		if (haveWeb) {
			// 网络释义
			stringBuilder.append("\n\n[网络释义]\n");
			int webSize = ret.getWeb().size();
			ret.getWeb().forEach((web) -> {
				stringBuilder.append(web.getKey()).append(" - ");
				AtomicInteger valueCount = new AtomicInteger(0);
				web.getValue().forEach((value) -> {
					stringBuilder.append(value).append("；");
					valueCount.getAndIncrement();
				});
				if (valueCount.get() > 0) {
					stringBuilder.deleteCharAt(stringBuilder.length() - 1);
				}
				if (webSize > 1) {
					stringBuilder.append("\n");
				}
			});
		}
		return stringBuilder.toString();
	}
	
	public static void main(String[] args) {
		String appKey = "0fb73659748c4ae0";
		String appSecret = "3qzukTjwWSTnthTwHU6hptGlZEeX0Oox";
		String from = "auto";
		String to = "auto";
		String query = "word";
		System.out.println(translate(appKey, appSecret, from, to, query));
	}
}
