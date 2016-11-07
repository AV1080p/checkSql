package burp;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.entity.ContentType;

import bsmali4.FuzzVul;

public class BurpExtender implements IBurpExtender, IScannerCheck {
	public IBurpExtenderCallbacks callbacks;
	public IExtensionHelpers helpers;
	public PrintWriter stdout;

	public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
		this.callbacks = callbacks;
		stdout = new PrintWriter(callbacks.getStdout(), true);
		this.helpers = callbacks.getHelpers();

		callbacks.setExtensionName("Time-based sqlinject checks");

		callbacks.registerScannerCheck(this);

		System.out.println("Loaded Time-based sqlinject checks");
	}

	@Override
	public int consolidateDuplicateIssues(IScanIssue existingIssue,
			IScanIssue newIssue) {
		// TODO Auto-generated method stub
		return 0;
	}

	// 主动式扫描
	@Override
	public List<IScanIssue> doActiveScan(
			IHttpRequestResponse baseRequestResponse,
			IScannerInsertionPoint insertionPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	// 被动式扫描
	@Override
	public List<IScanIssue> doPassiveScan(
			IHttpRequestResponse baseRequestResponse) {

		String method = this.helpers.analyzeRequest(baseRequestResponse)
				.getMethod();
		String url = this.helpers.analyzeRequest(baseRequestResponse).getUrl()
				.toString();
		if (!url.contains("google.com")) {
			if (method != null && method.trim().equals("POST")) {
				FuzzVul.checkPost(baseRequestResponse, helpers, stdout);
			} else if (method.trim().equals("GET")) {
				FuzzVul.checkGet(baseRequestResponse, helpers, stdout);
			}
		}
		return null;
	}

}