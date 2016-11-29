package fr.umlv.papayadb.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;

public class HttpClientHandler extends AbstractVerticle {
	private final HttpClientOptions options;
	private HttpClient client;
	
	public HttpClientHandler(String address, int port){
		this.options = new HttpClientOptions().setDefaultHost(address).setDefaultPort(port);
	}
	
	@Override
	public void start() throws Exception {
		this.client = vertx.createHttpClient(options);
	}
	
	public void send(String request) {
		client.getNow(request, response -> {
			  System.out.println("Received response with status code " + response.statusCode());
		});
	}
	

}
