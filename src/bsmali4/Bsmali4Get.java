package bsmali4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.omg.CORBA.PUBLIC_MEMBER;

/*封装的http请求*/
public class Bsmali4Get {
	// 参数 1.ip 2.port 3.data
	private String url;
	private String parameters = "";
	private URL realUrl;
	private int TIMEOUT = 5000;
	private HttpGet httpget;
	private CloseableHttpClient httpclient;
	private CloseableHttpResponse httpresponse;
	private Map<String, String> headersmap = new HashMap<String, String>();
	/* 响应信息 */
	private StatusLine resstatus;
	private long reslen;
	private String rescontent;
	private Header[] resheader;

	public Bsmali4Get(String url) {
		this.url = url;
		httpclient = HttpClients.createDefault();
	}

	// 获得参数
	public String getGetparams() {
		return this.parameters;
	}

	// 获得url
	public String getUrl() {
		return this.url;
	}

	// 增加数据
	public void addData(String parameters) {
		if (parameters != null && !parameters.toString().trim().equals("")) {
			if (this.parameters.equals("")) {
				this.parameters = this.parameters + "?" + parameters;
			} else {
				this.parameters = this.parameters + "&" + parameters;
			}
		}
	}

	// 增加文件头,包含cookies
	public void addHeader(String key, String value) {
		this.headersmap.put(key, value);
	}

	// 添加header到http请求
	public void addHeadertoHttp() {
		Iterator<Map.Entry<String, String>> entries = this.headersmap
				.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<String, String> entry = entries.next();
			httpget.addHeader(entry.getKey(), entry.getValue());
		}
	}

	// 获得响应求内容
	public String getResContent() {
		return this.rescontent;
	}

	// 获得响应状态吗
	public StatusLine getResStatus() {
		return this.resstatus;
	}

	// 获得响应内容
	public long getResLen() {
		return this.reslen;
	}

	// 获取响应头
	public Header[] getResHeaders() {
		/*
		 * for(Header h:heads){
		 * System.out.println(h.getName()+":"+h.getValue()); }
		 */
		return this.resheader;
	}

	// 提交请求
	public void doGet() {

		try {
			this.url = this.url + this.parameters;
			this.httpget = new HttpGet(this.url);
			this.addHeadertoHttp();
			this.httpresponse = httpclient.execute(httpget);
			// 获取响应内容 1.状态吗 2.响应httpheader 3.响应内容
			// 打印响应状态
			this.resheader = (Header[]) this.httpresponse.getAllHeaders();
			this.resstatus = this.httpresponse.getStatusLine();
			HttpEntity entity = this.httpresponse.getEntity();
			if (entity != null) {
				// 响应内容长度
				this.reslen = entity.getContentLength();
				// 响应内容
				this.rescontent = EntityUtils.toString(entity);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.httpresponse.close();
				this.httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
