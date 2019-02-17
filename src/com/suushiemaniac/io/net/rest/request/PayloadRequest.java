package com.suushiemaniac.io.net.rest.request;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class PayloadRequest extends Request {
	public PayloadRequest(String endpoint, RequestVerb requestVerb) {
		super(endpoint, requestVerb);
	}

	@Override
	protected HttpURLConnection relayConnection(HttpURLConnection conn) throws IOException {
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(true);

		conn.setRequestProperty("Content-Length", String.valueOf(this.getBody().length()));

		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
		writer.write(this.getBody());
		writer.flush();

		writer.close();
		return conn;
	}
}