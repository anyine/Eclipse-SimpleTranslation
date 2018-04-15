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
		mErrorCodeMessageMap.put("101",	"ȱ�ٱ���Ĳ�����������������������et��ֵ��ʵ�ʼ��ܷ�ʽ����Ӧ");
		mErrorCodeMessageMap.put("102",	"��֧�ֵ���������");
		mErrorCodeMessageMap.put("103",	"�����ı�����");
		mErrorCodeMessageMap.put("104",	"��֧�ֵ�API����");
		mErrorCodeMessageMap.put("105",	"��֧�ֵ�ǩ������");
		mErrorCodeMessageMap.put("106",	"��֧�ֵ���Ӧ����");
		mErrorCodeMessageMap.put("107",	"��֧�ֵĴ����������");
		mErrorCodeMessageMap.put("108",	"appKey��Ч��ע���˺ţ� ��¼��̨����Ӧ�ú�ʵ������ɰ󶨣� �ɻ��Ӧ��ID����Կ����Ϣ������Ӧ��ID����appKey�� ע�ⲻ��Ӧ����Կ��");
		mErrorCodeMessageMap.put("109",	"batchLog��ʽ����ȷ");
		mErrorCodeMessageMap.put("110",	"����ط������Чʵ��");
		mErrorCodeMessageMap.put("111",	"�������˺���Ч");
		mErrorCodeMessageMap.put("113",	"q����Ϊ��");
		mErrorCodeMessageMap.put("201",	"����ʧ�ܣ�����ΪDES,BASE64,URLDecode�Ĵ���");
		mErrorCodeMessageMap.put("202",	"ǩ������ʧ��");
		mErrorCodeMessageMap.put("203",	"����IP��ַ���ڿɷ���IP�б�");
		mErrorCodeMessageMap.put("301",	"�ǵ��ѯʧ��");
		mErrorCodeMessageMap.put("302",	"�����ѯʧ��");
		mErrorCodeMessageMap.put("303",	"����˵������쳣");
		mErrorCodeMessageMap.put("401",	"�˻��Ѿ�Ƿ��");
		mErrorCodeMessageMap.put("411",	"����Ƶ������,���Ժ����");
		mErrorCodeMessageMap.put("412",	"���������Ƶ�������Ժ����");
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
    			.append("[��������]\n")
    			.append(StringUtil.isEmpty(defaultMsg) ? mErrorCodeMessageMap.getOrDefault(errorCode, "Unknown error") : defaultMsg)
    			.toString();
    }

	private static String analysisResult(YoudaoApiRet ret) {
		boolean haveWeb = null != ret.getWeb() && !ret.getWeb().isEmpty();
		boolean haveBasic = null != ret.getBasic() && null != ret.getBasic().getExplains() && !ret.getBasic().getExplains().isEmpty();
		
		StringBuilder stringBuilder = new StringBuilder();
		// ������
		if (haveBasic || haveWeb) {
			stringBuilder.append("[������]\n");
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
			// �ʵ�
			stringBuilder.append("\n\n[�е��ʵ�]\n");
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
			// ��������
			stringBuilder.append("\n\n[��������]\n");
			int webSize = ret.getWeb().size();
			ret.getWeb().forEach((web) -> {
				stringBuilder.append(web.getKey()).append(" - ");
				AtomicInteger valueCount = new AtomicInteger(0);
				web.getValue().forEach((value) -> {
					stringBuilder.append(value).append("��");
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
