package fr.umlv.papayadb.proxy;

import java.util.Objects;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;

public class RequestConverter {
	public static JsonObject UrlToJson(HttpServerRequest request) throws IllegalArgumentException {
		Objects.requireNonNull(request);
		JsonObject json = new JsonObject();
		
		return json;
	}
}
