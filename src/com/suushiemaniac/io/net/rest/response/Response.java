package com.suushiemaniac.io.net.rest.response;

import java.util.HashMap;
import java.util.Map;

public class Response {
	protected int responseCode;
	protected Map<String, String> headers;
	protected String body;

	public Response(int responseCode, Map<String, String> headers, String body) {
		this.responseCode = responseCode;
		this.headers = headers;
		this.body = body;
	}

	public int getResponseCode() {
		return this.responseCode;
	}

	public int getCode() {
		return this.getResponseCode();
	}

	public Map<String, String> getHeaders() {
		return new HashMap<>(this.headers);
	}

	public String getHeader(String index) {
		return this.headers.get(index);
	}

	public String getBody() {
		return this.body;
	}
}