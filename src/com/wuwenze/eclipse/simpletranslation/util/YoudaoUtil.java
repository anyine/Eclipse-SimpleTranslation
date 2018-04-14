package com.wuwenze.eclipse.simpletranslation.util;

import java.io.UnsupportedEncodingException;
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
		mErrorCodeMessageMap.put("10086","δ֪�쳣����������������ʱ����");
	}
	
    public static String translate(String appKey,String appSecret,String from, String to, String query) {
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
//            System.out.println("translate prams: " + params);
            String retJsonString = HttpUtil.get(YOUDAO_API_HOST, params);
//            System.out.println("translate result: " + retJsonString);
            if (!StringUtil.isEmpty(retJsonString)) {
            	YoudaoApiRet ret = JSON.parseObject(retJsonString, YoudaoApiRet.class);
            	if ("0".equals(ret.getErrorCode())) {
            		return analysisResult(ret);
            	}
            	return mErrorCodeMessageMap.get(ret.getErrorCode());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return mErrorCodeMessageMap.get("10086");
    }

	private static String analysisResult(YoudaoApiRet ret) {
		StringBuilder stringBuilder = new StringBuilder();
		// ������
		stringBuilder.append("[������]\n");
		AtomicInteger translationCount = new AtomicInteger(1);
		ret.getTranslation().forEach((t) -> {
			int translationSize = ret.getTranslation().size();
			if (translationSize > 1) {
				stringBuilder.append(translationCount).append(": ");
			}
			stringBuilder.append(t).append("\n");
			translationCount.getAndIncrement();
		});
		
		if (null != ret.getBasic() && null != ret.getBasic().getExplains()) {
			stringBuilder.append("\n");
			// �ʵ�
			stringBuilder.append("[�е��ʵ�]\n");
			AtomicInteger explainsCount = new AtomicInteger(1);
			ret.getBasic().getExplains().forEach((e) -> {
				int explainsSize = ret.getBasic().getExplains().size();
				if (explainsSize > 1) {
					stringBuilder.append(explainsCount).append(": ");
				}
				stringBuilder.append(e).append("\n");
				explainsCount.getAndIncrement();
			});
		}
		
		if (null != ret.getWeb() && !ret.getWeb().isEmpty()) {
			stringBuilder.append("\n");
			// ��������
			stringBuilder.append("[��������]\n");
			ret.getWeb().forEach((web) -> {
				stringBuilder.append(web.getKey()).append(":\t");
				AtomicInteger valueCount = new AtomicInteger(0);
				web.getValue().forEach((value) -> {
					stringBuilder.append(value).append(";");
					valueCount.getAndIncrement();
				});
				if (valueCount.get() > 0) {
					stringBuilder.deleteCharAt(stringBuilder.length() - 1);
				}
				stringBuilder.append("\n");
			});
		}
		return stringBuilder.toString();
	}
	
	public static void main(String[] args) {
		String appKey = "";
		String appSecret = "";
		String from = "auto";
		String to = "auto";
		String query = "����";
		System.out.println(translate(appKey, appSecret, from, to, query));
	}
}
