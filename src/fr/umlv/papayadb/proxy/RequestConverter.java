package fr.umlv.papayadb.proxy;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;

public class RequestConverter {
	public static JsonObject UrlToJson(HttpServerRequest request) throws IllegalArgumentException {
		Objects.requireNonNull(request);
		String[] splitedRequest = request.path().split("/");
		List<String> request2 = Arrays.stream(splitedRequest).filter(e -> !e.isEmpty()).collect(Collectors.toList());
		for(String s : request2) {
			System.out.println("--> " + s);
		}
		JsonObject json = new JsonObject();

		return json;
	}
}
