package fr.umlv.papayadb.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HttpClientHandler {
	private String server;
	
	public HttpClientHandler(String server) {
		this.server = server;
	}
	
	/**
	 * Permet d'envoyer une requête HTTP de type POST
	 * 
	 * @param request
	 *            requête de l'utilisateur à transmettre
	 */
	public void post(String request) {
		Objects.requireNonNull(request);
		try {
			CompletableFuture<HttpResponse> response = HttpRequest
			          .create(new URI(this.server))
			          .body(HttpRequest.fromString(request))
			          .POST()
			          .responseAsync();
			HttpResponse r = response.get();
			System.out.println(r.body(HttpResponse.asString()));	
		} catch (URISyntaxException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet d'envoyer une requête HTTP de type GET
	 * 
	 * @param request
	 *            requête de l'utilisateur à transmettre
	 */
	public void get(String request) {
		Objects.requireNonNull(request);
		try {
			CompletableFuture<HttpResponse> response = HttpRequest
			          .create(new URI(this.server))
			          .body(HttpRequest.fromString(request))
			          .GET()
			          .responseAsync();
			
			HttpResponse r = response.get();
			System.out.println(r.body(HttpResponse.asString()));	
		} catch (URISyntaxException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet d'envoyer une requête HTTP de type PUT
	 * 
	 * @param request
	 *            requête de l'utilisateur à transmettre
	 */
	public void put(String request) {
		Objects.requireNonNull(request);
		try {
			CompletableFuture<HttpResponse> response = HttpRequest
			          .create(new URI(this.server))
			          .body(HttpRequest.fromString(request))
			          .PUT()
			          .responseAsync();
			
			HttpResponse r = response.get();
			System.out.println(r.body(HttpResponse.asString()));
		} catch (URISyntaxException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet d'envoyer une requête HTTP de type DELETE
	 * 
	 * @param request
	 *            requête de l'utilisateur à transmettre
	 */
	public void delete(String request) {
		Objects.requireNonNull(request);
		URL uri;
		try {
			uri = new URL(this.server);
			HttpURLConnection connexion;
			connexion = (HttpURLConnection) uri.openConnection();
			connexion.setRequestMethod("DELETE");
			connexion.connect();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	

}
