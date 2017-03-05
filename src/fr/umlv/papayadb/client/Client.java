package fr.umlv.papayadb.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Builder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Client {
	private String server;
	private final HttpClient client;

	public Client(String server) {
		this.server = server;
		//char[] password = {'p','a','s','s','w','o','r','d'};
		//PasswordAuthentication pass = new PasswordAuthentication("root", password);
		//SSLContext ssl = getContextInstance();
		Builder build = HttpClient.create();
		//build.sslContext(ssl);
		this.client = build.build();
	}
	
	/*private SSLContext getContextInstance() {
		try {
			SSLContext sc = SSLContext.getInstance("SSLv3");
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			String ksName = "keystore";
			char ksPass[] = "papaya".toCharArray();
			char ctPass[] = "papaya".toCharArray();
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(ksName), ksPass);
			kmf.init(ks, ctPass);
			KeyManager[] kmList = kmf.getKeyManagers();
			sc.init(kmList, null, null);
	
			return sc;
		} catch (Exception e) {
			System.err.println(e.toString());
			return null;
		}
	}*/

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
		case "getFileFromDb":
			getFileFromDb(database, request);
			break;
		default:
			return false;
		}
		return true;
	}
	
	private void getFileFromDb(String database, String file) {
		Objects.requireNonNull(file);
		try {
			CompletableFuture<HttpResponse> response = client.request(new URI(this.server))
					.header("db", database)
					.header("file", file)
					.GET()
					.responseAsync();
			HttpResponse r = response.get();
			File newFile = new File("DL_"+file);
			r.body(HttpResponse.asFile(newFile.toPath()));
			System.out.println(r.body(HttpResponse.asString()));
		} catch (URISyntaxException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet d'envoyer un fichier json/txt pour insersion dans la BDD
	 * @param file
	 */
	private void insertFileFromLocal(String database,String file) {
		String[] fileNameRetriever = file.split("/");
		String fileName = fileNameRetriever[fileNameRetriever.length];
		try {
			CompletableFuture<HttpResponse> response = client.request(new URI(this.server))
					.setHeader("db", database)
					.setHeader("file", fileName)
					.body(HttpRequest.fromString(readFile(file)))
					.POST()
					.responseAsync();
			HttpResponse r = response.get();
			System.out.println(r.body(HttpResponse.asString()));
		} catch (URISyntaxException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	} // insertFileFromLocal /Users/Rayco/Documents/testJSON.json

	private String readFile(String file) {
		try{
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
	         return content;
	      }    
	      catch (Exception e){
	         System.out.println(e.toString());
	         return null;
	      }
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
	private void post(String database) {
		Objects.requireNonNull(database);
		try {
			CompletableFuture<HttpResponse> response = client.request(new URI(this.server))
					.setHeader("db", database)
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
			CompletableFuture<HttpResponse> response = client.request(new URI(this.server))
					.header("db", database)
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
	private void get(String database,String filter) {
		Objects.requireNonNull(filter);
		try {
			CompletableFuture<HttpResponse> response = client.request(new URI(this.server))
					.header("db", database)
					.header("filter", filter)
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
			connexion.addRequestProperty("db", database);
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
			connexion.addRequestProperty("db", database);
			connexion.addRequestProperty("file", file);
			connexion.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
