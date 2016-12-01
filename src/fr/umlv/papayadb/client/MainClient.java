package fr.umlv.papayadb.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class MainClient {
	
	private static String server;
	
	
	private static void send(String server,String request) {
		try {
			CompletableFuture<HttpResponse> response = HttpRequest
			          .create(new URI(server))
			          .body(HttpRequest.fromString(request))
			          .POST()
			          .responseAsync();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		if (args.length != 2){
			throw new IllegalArgumentException("Entrez l'adresse puis le port");
		}
		server = args[1];
		System.out.println("Tapez votre commande ou tapez help pour afficher les commandes et la syntaxe");
		
		Scanner sc = new Scanner(System.in);
		String request = sc.nextLine();
		while(!request.contentEquals("exit")){
			switch(request) {
			case "help":
				System.out.println("Voici les commandes possibles :");
				break;
			default:
				send(server, request);
				break;
			}
			request = sc.nextLine();
		}
	}

}
