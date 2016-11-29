package fr.umlv.papayadb.proxy;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;

public class RequestConverter {
	public static JsonObject UrlToJson(HttpServerRequest request) throws IllegalArgumentException {
		Objects.requireNonNull(request);
		String[] splitedRequest = request.path().split("/");
		List<String> requestList = Arrays.stream(splitedRequest).filter(e -> !e.isEmpty()).collect(Collectors.toList());
		if(requestList.size() == 0) {
			throw new IllegalArgumentException("request is empty");
		}
		int index = requestList.indexOf("db");
		if(index != 0) {
			throw new IllegalArgumentException("request is not correct");
		}
		JsonObject json = new JsonObject();
		json.put("method", request.method());
		// Get all dbs
		if(requestList.size() == 1) {
			json.put("db", "*");
		} else if(requestList.get(index + 1).equals("new")) { // Post new db
			if(requestList.size() == 3) {
				json.put("dbname", requestList.get(index + 2));
			} else {
				throw new IllegalArgumentException("request missing new dbname");
			}
		} else { // Get db
			json.put("db", requestList.get(index + 1));
		}
		
		return json;
	}
}
