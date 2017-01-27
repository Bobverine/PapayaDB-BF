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

public class Client {
	private String server;

	public Client(String server) {
		this.server = server;
	}

	/**
	 * Envoi la commande au serveur en fonction de la requpete de l'utilisateur
	 * (GET, POST ...)
	 * 
	 * @param client
	 *            Client HTTP qui possède les informations du serveur cible
	 * 
	 * @param command
	 *            Commande de l'utilisateur à envoyer
	 * 
	 * @return void
	 */
	boolean executeCommand(String command) {
		String[] split = command.split(" ");
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < split.length; i++) {
			sb.append(split[i]);
		}
		String request = sb.toString();
		switch (split[0]) {
		case "help":
			printCommand();
			break;
		case "createDatabase":
			post(request);
			break;
		case "deleteDatabase":
			delete(request);
			break;
		case "selectDatabases":
			get("/");
			break;
		case "insertFileFromLocal":
			post(request);
			break;
		case "deleteFileFromDb":
			delete(request);
			break;
		case "selectFromDatabase":
			get(request);
			break;
		default:
			return false;
		}
		return true;
	}

	/**
	 * Affiche la liste des commandes possible
	 * 
	 * @return void
	 */
	private void printCommand() {
		System.out.println("Voici les commandes possibles : \n" + "Créer une database : createDatabase /:dbname \n"
				+ "Supprimer une database : deleteDatabase /:dbname \n"
				+ "Afficher toutes les bases de données : selectDatabases / \n"
				+ "Insérer dans une base de donnée : insertFileFromLocal /:dbname/:filename \n"
				+ "Supprimer dans une base de donnée : deleteFileFromDb /:dbname/:filename \n"
				+ "Afficher une donnée d'une base de donnée : selectFromDatabase /:dbname?filter=[...] \n");
	}

	/**
	 * Permet d'envoyer une requête HTTP de type POST
	 * 
	 * @param request
	 *            requête de l'utilisateur à transmettre
	 */
	private void post(String request) {
		Objects.requireNonNull(request);
		try {
			CompletableFuture<HttpResponse> response = HttpRequest.create(new URI(this.server))
					.body(HttpRequest.fromString(request)).POST().responseAsync();
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
	private void get(String request) {
		Objects.requireNonNull(request);
		try {
			CompletableFuture<HttpResponse> response = HttpRequest.create(new URI(this.server))
					.body(HttpRequest.fromString(request)).GET().responseAsync();

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
	// private void put(String request) {
	// Objects.requireNonNull(request);
	// try {
	// CompletableFuture<HttpResponse> response = HttpRequest
	// .create(new URI(this.server))
	// .body(HttpRequest.fromString(request))
	// .PUT()
	// .responseAsync();
	//
	// HttpResponse r = response.get();
	// System.out.println(r.body(HttpResponse.asString()));
	// } catch (URISyntaxException | InterruptedException | ExecutionException
	// e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * Permet d'envoyer une requête HTTP de type DELETE
	 * 
	 * @param request
	 *            requête de l'utilisateur à transmettre
	 */
	private void delete(String request) {
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
