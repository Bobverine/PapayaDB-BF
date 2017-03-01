package fr.umlv.papayadb.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Client {
	private String server;

	public Client(String server) {
		this.server = server;
		//Builder build = HttpClient.create();
		//HttpClient client = build.build();
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
		for (int i = 2; i < split.length; i++) {
			sb.append(split[i]);
		}
		String database = split[1];
		String request = sb.toString();
		System.out.println(request);
		switch (split[0]) {
		case "help":
			printCommand();
			break;
		case "createDatabase":
			post(database);
			break;
		case "deleteDatabase":
			delete(database);
			break;
		case "selectDatabases":
			get("");
			break;
		case "insertFileFromLocal":
			insertFileFromLocal(database ,request);
			break;
		case "deleteFileFromDb":
			delete(database, request);
			break;
		case "selectFromDatabase":
			get(database, request);
			break;
		default:
			return false;
		}
		return true;
	}
	
	/**
	 * Permet d'envoyer un fichier json/txt pour insersion dans la BDD
	 * @param file
	 */
	private void insertFileFromLocal(String database,String file) {
		try{
			 String[] fileNameRetriever = file.split("/");
			 String fileName = fileNameRetriever[fileNameRetriever.length];
	         InputStream ips=new FileInputStream(file);
	         InputStreamReader ipsr=new InputStreamReader(ips);
	         BufferedReader br=new BufferedReader(ipsr);
	         String line;
	         StringBuilder sb = new StringBuilder();
	         while ((line=br.readLine())!=null){
	            sb.append(line).append("\n");
	         }
	         br.close();
	         String content = sb.toString();
	         //System.out.println(content);
	         post(database, fileName, content);
	      }    
	      catch (Exception e){
	         System.out.println(e.toString());
	      }
	} // insertFileFromLocal /Users/Rayco/Documents/testJSON.json

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
	private void post(String database) {
		Objects.requireNonNull(database);
		try {
			CompletableFuture<HttpResponse> response = HttpRequest.create(new URI(this.server))
					.setHeader("database", database)
					.POST()
					.responseAsync();
			HttpResponse r = response.get();
			System.out.println(r.body(HttpResponse.asString()));
		} catch (URISyntaxException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet d'envoyer une requête HTTP de type POST
	 * 
	 * @param request
	 *            requête de l'utilisateur à transmettre
	 */
	private void post(String database ,String file,String request) {
		Objects.requireNonNull(request);
		try {
			CompletableFuture<HttpResponse> response = HttpRequest.create(new URI(this.server))
					.setHeader("database", database)
					.setHeader("file", file)
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
	private void get(String database) {
		Objects.requireNonNull(database);
		try {
			CompletableFuture<HttpResponse> response = HttpRequest.create(new URI(this.server))
					.header("database", database)
					.GET()
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
	private void get(String database,String filters) {
		Objects.requireNonNull(filters);
		try {
			CompletableFuture<HttpResponse> response = HttpRequest.create(new URI(this.server))
					.header("database", database)
					.header("filters", filters)
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
	private void delete(String database) {
		Objects.requireNonNull(database);
		URL uri;
		try {
			uri = new URL(this.server);
			HttpURLConnection connexion;
			connexion = (HttpURLConnection) uri.openConnection();
			connexion.setRequestMethod("DELETE");
			connexion.addRequestProperty("database", database);
			connexion.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet d'envoyer une requête HTTP de type DELETE
	 * 
	 * @param request
	 *            requête de l'utilisateur à transmettre
	 */
	private void delete(String database, String file) {
		Objects.requireNonNull(database);
		URL uri;
		try {
			uri = new URL(this.server);
			HttpURLConnection connexion;
			connexion = (HttpURLConnection) uri.openConnection();
			connexion.setRequestMethod("DELETE");
			connexion.addRequestProperty("database", database);
			connexion.addRequestProperty("file", file);
			connexion.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
