package bsmali4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.omg.CORBA.PUBLIC_MEMBER;

/*封装的http请求*/
public class Bsmali4Post {
	private String rescode;
	// 参数 1.ip 2.port 3.data
	private String url;
	private String parameters = "";
	private URL realUrl;
	private int TIMEOUT = 5000;
	private HttpPost httppost;
	private CloseableHttpClient httpclient;
	private CloseableHttpResponse httpresponse;
	private Map<String, String> headersmap = new HashMap<String, String>();
	// 提交请求参数
	public List<NameValuePair> postparams = new ArrayList<NameValuePair>();

	/* 响应信息 */
	private StatusLine resstatus;
	private long reslen;
	private String rescontent;
	private Header[] resheader;

	public Bsmali4Post(String url) {
		this.url = url;
		httpclient = HttpClients.createDefault();
	}
	//获得postparams
	public  List<NameValuePair> getPostparams()
	{
		return this.postparams;
	}
	
	// 增加数据
	public void addData(String key, String value) {
		if (key != null && !key.toString().trim().equals("") && value != null && !value.toString().trim().equals("")) {
			this.postparams.add(new BasicNameValuePair(key, value));
		}
	}

	// 增加文件头,包含cookies
	public void addHeader(String key, String value) {
		this.headersmap.put(key, value);
	}

	// 添加header到http请求
	public void addHeadertoHttp() {
		Iterator<Map.Entry<String, String>> entries = this.headersmap.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<String, String> entry = entries.next();
			httppost.addHeader(entry.getKey(), entry.getValue());
		}
	}

	// 增加数据到http请求
	public void addDatatoHttp() {
		if (this.postparams.size() > 0) {
			try {
				UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(this.postparams, "UTF-8");
				this.httppost.setEntity(uefEntity);
			} catch (UnsupportedEncodingException e) {
				System.out.println("444");
				e.printStackTrace();
			}
		}
	}

	// 设置响应编码
	public void setResCode(String code) {
		if (code.equals("UTF-8") && code.equals("utf-8")) {
			this.rescode = "UTF-8";
		} else if (code.equals("GBK") && code.equals("gbk")) {
			this.rescode = "GBK";
		} else {
			this.rescode = "UTF-8";
		}
	}

	public String getResCode() {
		return this.rescode;
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
	public void doPost() {
		try {
			this.httppost = new HttpPost(this.url);
			this.addHeadertoHttp();
			this.addDatatoHttp();
			this.httpresponse = httpclient.execute(httppost);
			// 获取响应内容 1.状态吗 2.响应httpheader 3.响应内容
			// 打印响应状态
			this.resheader = (Header[]) this.httpresponse.getAllHeaders();
			this.resstatus = this.httpresponse.getStatusLine();
			HttpEntity entity = this.httpresponse.getEntity();
			if (entity != null) {
				// 响应内容长度
				this.reslen = entity.getContentLength();
				// 响应内容
				this.rescontent = EntityUtils.toString(entity, this.rescode);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("222");
		} finally {
			try {
				this.httpresponse.close();
				this.httpclient.close();
			} catch (IOException e) {
				System.out.println("333");
				e.printStackTrace();
			}
		}

	}
}