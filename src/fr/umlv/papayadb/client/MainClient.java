package fr.umlv.papayadb.client;

import java.util.Scanner;

public class MainClient {
	
	/**
	 * Affiche la liste des commandes possible
	 * 
	 * @return void
	 */
	private static void printCommand() {
		System.out.println("Voici les commandes possibles :");
		System.out.println("Créer une database : createDatabase /:dbname");
		System.out.println("Supprimer une database : deleteDatabase /:dbname");
		System.out.println("Afficher toutes les bases de données : selectDatabases /");
		System.out.println("Insérer dans une base de donnée : insertFileFromLocal /:dbname/:filename");
		System.out.println("Supprimer dans une base de donnée : deleteFileFromDb /:dbname/:filename");
		System.out.println("Afficher une donnée d'une base de donnée : selectFromDatabase /:dbname?filter=[...]");
	}
	
	/**
	 * Envoi la commande au serveur en fonction de la requpete de l'utilisateur (GET, POST ...)
	 * 
	 * @param client
	 *            Client HTTP qui possède les informations du serveur cible
	 *            
	 * @param command
	 * 			  Commande de l'utilisateur à envoyer
	 *            
	 * @return void
	 */
	private static void executeCommand(HttpClientHandler client, String command) {
		String[] split = command.split(" ");
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i < split.length; i++) {
			sb.append(split[i]);
		}
		String request = sb.toString();
		switch(split[0]) {
			case "help":
				printCommand();
				break;
			case "createDatabase":
				client.post(request);
				break;
			case "deleteDatabase":
				client.delete(request);
				break;
			case "selectDatabases":
				client.get("/");
				break;
			case "insertFileFromLocal":
				client.post(request);
				break;
			case "deleteFileFromDb":
				client.delete(request);
				break;
			case "selectFromDatabase":
				client.get(request);
				break;
			default:
				System.out.println("Bad request");
				break;
		}
	}
	
	
	
	public static void main(String[] args) {
		if (args.length != 1) {
			throw new IllegalArgumentException("Entrez l'adresse suivi du port ex : http://127.0.0.1:8080");
		}
		System.out.println(args[0]);
		HttpClientHandler client = new HttpClientHandler(args[0]);
		System.out.println("Tapez votre commande ou tapez help pour afficher les commandes et la syntaxe");
		Scanner sc = new Scanner(System.in);
		String command = sc.nextLine();
		while(!command.contentEquals("exit")) {
			executeCommand(client , command);
			command = sc.nextLine();
		}
		sc.close();
		
	}

}
