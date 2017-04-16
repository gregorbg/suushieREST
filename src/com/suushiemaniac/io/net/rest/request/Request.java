package com.suushiemaniac.io.net.rest.request;

import com.suushiemaniac.io.net.rest.response.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public abstract class Request {
	public enum RequestVerb {
		GET, POST, PUT, PATCH, DELETE, HEAD
	}

	protected String endpoint;
	protected RequestVerb requestVerb;

	protected Map<String, String> headers;
	protected Map<String, String> queryParams;
	protected String body;

	protected Request(String endpoint, RequestVerb requestVerb) {
		this.endpoint = endpoint;
		this.requestVerb = requestVerb;

		this.headers = new HashMap<>();
		this.queryParams = new HashMap<>();
		this.body = "";
	}

	public String getEndpoint() {
		return this.endpoint;
	}

	public String getHeader(String key) {
		return this.headers.get(key);
	}

	public void addHeader(String key, String value) {
		this.headers.put(key, value);
	}

	public void header(String key, String value) {
		this.addHeader(key, value);
	}

	public void basicAuth(String username, String password) {
		String basicAuthCreds = username + ":" + password;
		String basicAuth = "Basic " + Base64.getEncoder().encodeToString(basicAuthCreds.getBytes());

		this.addHeader("Authorization", basicAuth);
	}

	public void removeHeader(String key) {
		this.headers.remove(key);
	}

	public Map<String, String> getHeaders() {
		return new HashMap<>(this.headers);
	}

	public String getQueryParam(String key) {
		return this.queryParams.get(key);
	}

	public void addQueryParam(String key, String value) {
		this.queryParams.put(key, value);
	}

	public void queryParam(String key, String value) {
		this.addQueryParam(key, value);
	}

	public void queryString(String key, String value) {
		this.addQueryParam(key, value);
	}

	public void removeQueryParam(String key) {
		this.queryParams.remove(key);
	}

	public Map<String, String> getQueryParams() {
		return new HashMap<>(this.queryParams);
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void jsonBody(String json) {
		this.setBody(json);
		this.addHeader("Content-Type", "application/json");
	}

	public void formBody(String form) {
		this.setBody(form);
		this.addHeader("Content-Type", "application/x-www-form-urlencoded");
	}

	public Response send() throws IOException {
		String endpoint = this.getEndpoint();
		List<String> queryParts = new ArrayList<>();

		for (String queryParam : this.getQueryParams().keySet()) {
		    String encKey = URLEncoder.encode(queryParam, "UTF-8");
		    String encVal = URLEncoder.encode(this.getQueryParam(queryParam), "UTF-8");
			queryParts.add(encKey + "=" + encVal);
		}

		String fullQuery = String.join("&", queryParts);

		if (fullQuery.length() > 0) {
			endpoint += "?" + fullQuery;
		}

		URL url = new URL(endpoint); // TODO safer implementation!

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(this.requestVerb.name());

		for (String key : this.getHeaders().keySet()) {
		    conn.setRequestProperty(key, this.getHeader(key));
		}

		conn = this.relayConnection(conn);
		conn.connect();

		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
		}

		reader.close();

		Map<String, String> saneHeaders = new HashMap<>();
		Map<String, List<String>> connHeaders = conn.getHeaderFields();

		for (String connHeaderKey : connHeaders.keySet()) {
			saneHeaders.put(connHeaderKey, conn.getHeaderField(connHeaderKey));
		}

		return new Response(
				conn.getResponseCode(),
				saneHeaders,
				stringBuilder.toString().trim()
		);
	}

	protected abstract HttpURLConnection relayConnection(HttpURLConnection conn) throws IOException;
}
