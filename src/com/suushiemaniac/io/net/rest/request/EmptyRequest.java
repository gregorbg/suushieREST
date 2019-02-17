package com.suushiemaniac.io.net.rest.request;

import java.io.IOException;
import java.net.HttpURLConnection;

public class EmptyRequest extends Request {
	public EmptyRequest(String endpoint, RequestVerb requestVerb) {
		super(endpoint, requestVerb);
	}

	@Override
	protected HttpURLConnection relayConnection(HttpURLConnection conn) throws IOException {
		return conn; // TODO move this up as default implementation?
	}
}