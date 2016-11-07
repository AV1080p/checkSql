package bsmali4;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import sqlinject.SQLINJECT;
import sqlinject.SQLINJECT_BAKS;

import burp.IExtensionHelpers;
import burp.IHttpRequestResponse;
import burp.IParameter;
import burp.IRequestInfo;

public class FuzzVul {

	public static void checkPost(IHttpRequestResponse baseRequestResponse,
			IExtensionHelpers helpers, PrintWriter stdout) {
		List<String> headerStrings = new ArrayList<String>();
		List<IParameter> parameters = new ArrayList<IParameter>();
		URL url = helpers.analyzeRequest(baseRequestResponse).getUrl();
		byte contenttype = helpers.analyzeRequest(baseRequestResponse)
				.getContentType();
		headerStrings = helpers.analyzeRequest(baseRequestResponse)
				.getHeaders();
		parameters = helpers.analyzeRequest(baseRequestResponse)
				.getParameters();
		// stdout.println(url.toString());
		// stdout.println(contenttype);
		// stdout.println(helpers.analyzeRequest(baseRequestResponse).getMethod());
		// stdout.println(headerStrings);
		// print 函数
		/*
		 * for (int i = 0; i < parameters.size(); i++) { IParameter parameter =
		 * parameters.get(i); stdout.println(parameter.getName() + ":" +
		 * parameter.getValue()); }
		 */
		switch (contenttype) {
		case IRequestInfo.CONTENT_TYPE_URL_ENCODED:
			checkURLENCODEDPost(baseRequestResponse, helpers, stdout);
			break;

		default:
			break;
		}

	}

	/* 根据post内容的类型来分工 */
	/* AMF类型 */
	private static void checkAMFPost() {

	}

	/* JSON类型 */
	private static void checkJSONPost() {

	}

	/* MULTIPART类型 */
	private static void checkMULTIPARTPost() {

	}

	/* NONE类型 */
	private static void checkNONEPost() {

	}

	/* UNKNOWN类型 */
	private static void checkUNKNOWNPost(
			IHttpRequestResponse baseRequestResponse,
			IExtensionHelpers helpers, PrintWriter stdout) {
		// SQLINJECT.checkPostSqlinject(baseRequestResponse, helpers, stdout);
		// SQLINJECT.checkPostSqlinject(baseRequestResponse, helpers, stdout);

	}

	/* URL_ENCODED类型 */
	private static void checkURLENCODEDPost(
			IHttpRequestResponse baseRequestResponse,
			IExtensionHelpers helpers, PrintWriter stdout) {
		SQLINJECT.checkPostSqlinject(baseRequestResponse, helpers, stdout);
	}

	/* XML类型 */
	private static void checkXMLPost() {

	}

	/* 检查get方法 */
	public static void checkGet(IHttpRequestResponse baseRequestResponse,
			IExtensionHelpers helpers, PrintWriter stdout) {
		List<String> headerStrings = new ArrayList<String>();
		List<IParameter> parameters = new ArrayList<IParameter>();
		URL url = helpers.analyzeRequest(baseRequestResponse).getUrl();
		byte contenttype = helpers.analyzeRequest(baseRequestResponse)
				.getContentType();
		headerStrings = helpers.analyzeRequest(baseRequestResponse)
				.getHeaders();
		parameters = helpers.analyzeRequest(baseRequestResponse)
				.getParameters();
		//stdout.println(url.toString());
		//stdout.println(contenttype);
		//stdout.println(helpers.analyzeRequest(baseRequestResponse).getMethod());
		//stdout.println(headerStrings);
		/*
		 * print 函数 for (int i = 0; i < parameters.size(); i++) { IParameter
		 * parameter = parameters.get(i); stdout.println(parameter.getName() +
		 * ":" + parameter.getValue()); }
		 */
		switch (contenttype) {
		case IRequestInfo.CONTENT_TYPE_URL_ENCODED:
			checkURLENCODEDGet(baseRequestResponse, helpers, stdout);
			break;
		case IRequestInfo.CONTENT_TYPE_NONE:
			checkUNKNOWNGet(baseRequestResponse, helpers, stdout);
			break;
		default:
			break;
		}
	}

	/* 根据get内容的类型来分工 */
	/* AMF类型 */
	private static void checkAMFGet() {

	}

	/* JSON类型 */
	private static void checkJSONGet() {

	}

	/* MULTIPART类型 */
	private static void checkMULTIPARTGet() {

	}

	/* NONE类型 */
	private static void checkNONEGet() {

	}

	/* UNKNOWN类型 */
	private static void checkUNKNOWNGet(
			IHttpRequestResponse baseRequestResponse,
			IExtensionHelpers helpers, PrintWriter stdout) {
		SQLINJECT.checkGetSqlinject(baseRequestResponse, helpers, stdout);
	}

	/* URL_ENCODED类型 */
	private static void checkURLENCODEDGet(
			IHttpRequestResponse baseRequestResponse,
			IExtensionHelpers helpers, PrintWriter stdout) {
		SQLINJECT.checkGetSqlinject(baseRequestResponse, helpers, stdout);
	}

	/* XML类型 */
	private static void checkXMLGet() {

	}

}