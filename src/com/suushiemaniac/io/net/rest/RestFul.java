package com.suushiemaniac.io.net.rest;

import com.suushiemaniac.io.net.rest.request.EmptyRequest;
import com.suushiemaniac.io.net.rest.request.PayloadRequest;
import com.suushiemaniac.io.net.rest.request.Request;
import com.suushiemaniac.io.net.rest.request.Request.RequestVerb;

public abstract class RestFul {
	private RestFul() {} // closing constructor from outside access

	public static Request get(String url) {
		return new EmptyRequest(url, RequestVerb.GET);
	}

	public static Request head(String url) {
		return new EmptyRequest(url, RequestVerb.HEAD);
	}

	public static Request post(String url) {
		return new PayloadRequest(url, RequestVerb.POST);
	}

	public static Request put(String url) {
		return new PayloadRequest(url, RequestVerb.PUT);
	}

	public static Request patch(String url) {
		return new PayloadRequest(url, RequestVerb.PATCH);
	}

	public static Request delete(String url) {
		return new PayloadRequest(url, RequestVerb.DELETE);
	}
}
