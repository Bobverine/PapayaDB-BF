package fr.umlv.papayadb.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HttpClientHandler {
	private String server;
	
	public HttpClientHandler(String server){
		this.server = server;
	}
	
	public void send(String request) {
		try {
			CompletableFuture<HttpResponse> response = HttpRequest
			          .create(new URI(this.server))
			          .body(HttpRequest.fromString(request))
			          .POST()
			          .responseAsync();
			
			HttpResponse r = response.get();
			System.out.println(r.body(HttpResponse.asString()));
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
