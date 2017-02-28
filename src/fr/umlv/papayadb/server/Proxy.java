package fr.umlv.papayadb.server;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;

public class Proxy extends AbstractVerticle {
	private final Database database;
	private HttpServer server;
	private final HttpServerOptions httpOptions;
	
	public Proxy(Database db) {
		this.database = db;
		this.httpOptions = new HttpServerOptions().setHost("127.0.0.1").setPort(8080);
	}
	
	@Override
	public void start() throws Exception {
		this.server = vertx.createHttpServer(this.httpOptions);
		this.server.requestHandler(this::onHTTPRequest);
		
		server.listen(res -> {
			if(res.succeeded()) {
				System.out.println("HttpServer is up & ready to listen on " + httpOptions.getHost() + ":" + httpOptions.getPort());
			} else {
				System.out.println("HttpServer has failed to start : " + res.cause());
			}
		});
	}
	
	@Override
	public void stop() throws Exception {
		server.close(res -> {
			if(res.succeeded()) {
				System.out.println("HttpServer is down");
			} else {
				System.out.println("HttpServer has failed to stop");
			}
		});
	}
	
	public void onHTTPRequest(HttpServerRequest request) {
		//JsonObject json = UrlToJson(request);
		//gestion utilisateur
		//appel à la db
		request.formAttributes().forEach(entry -> {
			System.out.println(entry.getKey() + " " + entry.getValue());
		});
		
		try {
			//if(json.getString("method").equals(HttpMethod.GET.toString())) {
			if(request.method().equals(HttpMethod.GET)) {
				//if(!json.getString("db").isEmpty()) {
				if(!request.getFormAttribute("db").isEmpty()) {
					//database.getFiles(json.getString("db"), json.getString("filter"));
					database.getFiles(request.getFormAttribute("db"), request.getFormAttribute("filter"));
				} else {
					database.getDatabases();
				}
			}
			//if(json.getString("method").equals(HttpMethod.POST.toString())) {
			if(request.method().equals(HttpMethod.POST)) {
				//if(!json.getString("db").isEmpty()) {
				if(!request.getFormAttribute("db").isEmpty()) {
					//if(!json.getString("file").isEmpty()) {
					if(!request.getFormAttribute("file").isEmpty()) {
						database.insertFile(request.getFormAttribute("db"), request.getFormAttribute("file"));
					} else {
						database.createDatabase(request.getFormAttribute("db"));
					}
				}
			}
			//if(json.getString("method").equals(HttpMethod.DELETE.toString())) {
			if(request.method().equals(HttpMethod.DELETE)) {
				//if(!json.getString("db").isEmpty()) {
				if(!request.getFormAttribute("db").isEmpty()) {
					//if(!json.getString("file").isEmpty()) {
					if(!request.getFormAttribute("file").isEmpty()) {
						database.DeleteFile(request.getFormAttribute("db"), request.getFormAttribute("file"));
					} else {
						database.DeleteDatabase(request.getFormAttribute("db"));
					}
				}
			}
		} catch (NullPointerException e) {
			System.out.println("Request not valid");
		}
		
		request.response().end("Hello world");
	}
	
	public static JsonObject UrlToJson(HttpServerRequest request) {
		Objects.requireNonNull(request);
		String[] splitedRequest = request.path().split("/");
		List<String> requestList = Arrays.stream(splitedRequest).filter(e -> !e.isEmpty()).collect(Collectors.toList());

		JsonObject json = new JsonObject();
		json.put("method", request.method().toString());
		/*if(requestList.isEmpty()) {
			json.put("db", "*");
		}*/
		if(requestList.size() == 1) {
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
