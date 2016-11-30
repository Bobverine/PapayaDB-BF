package fr.umlv.papayadb.proxy;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;

public class RequestConverter {
	public static JsonObject UrlToJson(HttpServerRequest request) {
		Objects.requireNonNull(request);
		String[] splitedRequest = request.path().split("/");
		List<String> requestList = Arrays.stream(splitedRequest).filter(e -> !e.isEmpty()).collect(Collectors.toList());

		JsonObject json = new JsonObject();
		json.put("method", request.method());
		if(requestList.isEmpty()) {
			json.put("db", "*");
		}
		if(requestList.size() == 1){
			json.put("db", requestList.get(0));
			JsonObject filter = new JsonObject();
			for(Entry<String, String> entry : request.params()) {
				filter.put(entry.getKey(), entry.getValue());
			}
			json.put("filter", filter);
		}
		if(requestList.size() == 2) {
			json.put("db", requestList.get(0));
			json.put("file", requestList.get(1));
		}
		
		return json;
	}
}
