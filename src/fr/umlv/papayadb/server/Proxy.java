package fr.umlv.papayadb.server;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;

public class Proxy extends AbstractVerticle implements Runnable {
	private final BlockingQueue<JsonObject> queue;
	private HttpServer server;
	private final HttpServerOptions httpOptions;
	
	public Proxy(BlockingQueue<JsonObject> queue) {
		this.queue = queue;
		this.httpOptions = new HttpServerOptions().setHost("127.0.0.1").setPort(8080);
	}
	
	@Override
	public void run() {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(this);
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
		JsonObject json = UrlToJson(request);
		try {
			queue.put(json);
		} catch (InterruptedException e) {
			request.response().end("Something happened, sorry try again");
			e.printStackTrace();
		}
		request.response().end("Hello world");
	}
	
	public static JsonObject UrlToJson(HttpServerRequest request) {
		Objects.requireNonNull(request);
		String[] splitedRequest = request.path().split("/");
		List<String> requestList = Arrays.stream(splitedRequest).filter(e -> !e.isEmpty()).collect(Collectors.toList());

		JsonObject json = new JsonObject();
		json.put("method", request.method());
		if(requestList.isEmpty()) {
			json.put("db", "*");
		}
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
