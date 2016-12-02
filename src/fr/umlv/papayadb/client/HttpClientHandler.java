package fr.umlv.papayadb.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HttpClientHandler {
	private String server;
	
	public HttpClientHandler(String server){
		this.server = server;
	}
	
	public void post(String request) {
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
	
	public void get(String request) {
		try {
			CompletableFuture<HttpResponse> response = HttpRequest
			          .create(new URI(this.server))
			          .body(HttpRequest.fromString(request))
			          .GET()
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
	
	public void put(String request) {
		try {
			CompletableFuture<HttpResponse> response = HttpRequest
			          .create(new URI(this.server))
			          .body(HttpRequest.fromString(request))
			          .PUT()
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
	
	public void delete(String request){
			URL uri;
			try {
				uri = new URL(this.server);
				HttpURLConnection connexion;
				connexion = (HttpURLConnection) uri.openConnection();
				connexion.setRequestMethod("DELETE");
				connexion.connect();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	

}
