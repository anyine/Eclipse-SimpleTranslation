package com.wuwenze.eclipse.simpletranslation.pojo;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * http://ai.youdao.com/docs/doc-trans-api.s#p03
 * @author by wenzewoo on 2018/4/14
 */
public class YoudaoApiRet {
	/**
	 * tSpeakUrl :
	 * http://openapi.youdao.com/ttsapi?q=1&langType=en&sign=D1B13962796E0BB48BF8A6DF6A382463&salt=1523684075413&voice=4&format=mp3&appKey=0fb73659748c4ae0
	 * web : [{"value":["纽约地铁1号线","1 (B1A4专辑)","1
	 * (披头士专辑)"],"key":"1"},{"value":["香港1号干线","首都高速1号羽田线","名古屋高速1号楠线"],"key":"Route
	 * 1"},{"value":["月球车1号"],"key":"Lunokhod 1"}] query : 1 translation : ["1"]
	 * errorCode : 0 dict : {"url":"yddict://m.youdao.com/dict?le=eng&q=1"} webdict
	 * : {"url":"http://m.youdao.com/dict?le=eng&q=1"} basic :
	 * {"us-phonetic":"w?n","phonetic":"w?n","uk-phonetic":"w?n","explains":["one"]}
	 * l : zh-CHS2EN speakUrl :
	 * http://openapi.youdao.com/ttsapi?q=1&langType=zh-CHS&sign=D1B13962796E0BB48BF8A6DF6A382463&salt=1523684075413&voice=4&format=mp3&appKey=0fb73659748c4ae0
	 */

	private String tSpeakUrl;
	private String query;
	private String errorCode;
	private DictBean dict;
	private WebdictBean webdict;
	private BasicBean basic;
	private String l;
	private String speakUrl;
	private List<WebBean> web;
	private List<String> translation;

	public YoudaoApiRet() {
		super();
	}

	public YoudaoApiRet(String errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public String getTSpeakUrl() {
		return tSpeakUrl;
	}

	public void setTSpeakUrl(String tSpeakUrl) {
		this.tSpeakUrl = tSpeakUrl;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public DictBean getDict() {
		return dict;
	}

	public void setDict(DictBean dict) {
		this.dict = dict;
	}

	public WebdictBean getWebdict() {
		return webdict;
	}

	public void setWebdict(WebdictBean webdict) {
		this.webdict = webdict;
	}

	public BasicBean getBasic() {
		return basic;
	}

	public void setBasic(BasicBean basic) {
		this.basic = basic;
	}

	public String getL() {
		return l;
	}

	public void setL(String l) {
		this.l = l;
	}

	public String getSpeakUrl() {
		return speakUrl;
	}

	public void setSpeakUrl(String speakUrl) {
		this.speakUrl = speakUrl;
	}

	public List<WebBean> getWeb() {
		return web;
	}

	public void setWeb(List<WebBean> web) {
		this.web = web;
	}

	public List<String> getTranslation() {
		return translation;
	}

	public void setTranslation(List<String> translation) {
		this.translation = translation;
	}
	
	
	@Override
	public String toString() {
		return "YoudaoApiRet [tSpeakUrl=" + tSpeakUrl + ", query=" + query + ", errorCode=" + errorCode + ", dict="
				+ dict + ", webdict=" + webdict + ", basic=" + basic + ", l=" + l + ", speakUrl=" + speakUrl + ", web="
				+ web + ", translation=" + translation + "]";
	}

	public static class DictBean {
		/**
		 * url : yddict://m.youdao.com/dict?le=eng&q=1
		 */

		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		@Override
		public String toString() {
			return "DictBean [url=" + url + "]";
		}
	}

	public static class WebdictBean {
		/**
		 * url : http://m.youdao.com/dict?le=eng&q=1
		 */

		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		@Override
		public String toString() {
			return "WebdictBean [url=" + url + "]";
		}
	}

	public static class BasicBean {
		/**
		 * us-phonetic : w?n phonetic : w?n uk-phonetic : w?n explains : ["one"]
		 */

		@JSONField(name = "us-phonetic")
		private String usphonetic;
		private String phonetic;
		@JSONField(name = "uk-phonetic")
		private String ukphonetic;
		private List<String> explains;

		public String getUsphonetic() {
			return usphonetic;
		}

		public void setUsphonetic(String usphonetic) {
			this.usphonetic = usphonetic;
		}

		public String getPhonetic() {
			return phonetic;
		}

		public void setPhonetic(String phonetic) {
			this.phonetic = phonetic;
		}

		public String getUkphonetic() {
			return ukphonetic;
		}

		public void setUkphonetic(String ukphonetic) {
			this.ukphonetic = ukphonetic;
		}

		public List<String> getExplains() {
			return explains;
		}

		public void setExplains(List<String> explains) {
			this.explains = explains;
		}

		@Override
		public String toString() {
			return "BasicBean [usphonetic=" + usphonetic + ", phonetic=" + phonetic + ", ukphonetic=" + ukphonetic
					+ ", explains=" + explains + "]";
		}
	}

	public static class WebBean {
		/**
		 * value : ["纽约地铁1号线","1 (B1A4专辑)","1 (披头士专辑)"] key : 1
		 */

		private String key;
		private List<String> value;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public List<String> getValue() {
			return value;
		}

		public void setValue(List<String> value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "WebBean [key=" + key + ", value=" + value + "]";
		}
	}
}
