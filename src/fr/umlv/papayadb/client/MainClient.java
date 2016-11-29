package fr.umlv.papayadb.client;

import java.util.Scanner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class MainClient extends AbstractVerticle{
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		if (args.length != 2){
			throw new IllegalArgumentException("Entrez l'adresse puis le port");
		}
		HttpClientHandler client = new HttpClientHandler(args[1], Integer.parseInt(args[2]));
		vertx.deployVerticle(client);
		
		System.out.println("Tapez votre commande ou tapez help pour afficher les commandes et la syntaxe");
		
		Scanner sc = new Scanner(System.in);
		String request = sc.nextLine();
		while(!request.contentEquals("exit")){
			switch(request) {
			case "help":
				System.out.println("Voici les commandes possibles :");
				break;
			default:
				client.send(request);
				break;
			}
			request = sc.nextLine();
		}
	}

}
