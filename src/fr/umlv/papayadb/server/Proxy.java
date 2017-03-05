package fr.umlv.papayadb.server;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;


public class Proxy extends AbstractVerticle {
	private final HashMap<String, Database> databases;
	private HttpServer server;
	private final HttpServerOptions httpOptions;
	
	public Proxy() {
		this.databases = new HashMap<>();
		File directory = new File("./");
		FileFilter db_filter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getName().endsWith(".db");
			}
		};
		for(File file : directory.listFiles(db_filter)){
			String name = file.getName().substring(0, file.getName().length() - 3);
			databases.put(name, new Database(name));
		}
		this.httpOptions = new HttpServerOptions().setHost("127.0.0.1").setPort(8080);
	}
	
	@Override
	public void start() throws Exception {
		this.server = vertx.createHttpServer(this.httpOptions);
		this.server.requestHandler(this::onHTTPRequest);
		
		server.listen(res -> {
			if(res.succeeded()) {
				System.out.println("HttpServer is up & ready to listen on " + httpOptions.getHost() + ":" + httpOptions.getPort());
			} else {
				System.out.println("HttpServer has failed to start : " + res.cause());
			}
		});
	}
	
	@Override
	public void stop() throws Exception {
		server.close(res -> {
			if(res.succeeded()) {
				System.out.println("HttpServer is down");
			} else {
				System.out.println("HttpServer has failed to stop");
			}
		});
	}
	
	/**
	 * Permet d'appeler selon la requête reçu la méthode de la BDD correspondante
	 * 
	 * @param request la requête HTTP du client
	 */
	public void onHTTPRequest(HttpServerRequest request) {
		//gestion utilisateur
		HttpServerResponse response = request.response();
		try {
			if(request.method().equals(HttpMethod.GET)) {
				if(request.headers().contains("db") && !request.getHeader("db").isEmpty() ) {
					if(!databases.containsKey(request.getHeader("db"))){
						response.setStatusCode(200).end("Database " + request.getHeader("db") + " does not exists");
					}
					else if(request.headers().contains("filter") && !request.getHeader("filter").isEmpty()) {
						StringBuilder sb = new StringBuilder();
						databases.get(request.getHeader("db")).getFiles(request.getHeader("filter")).forEach((address, name) -> {
							sb.append(address).append(" ").append(name).append("\n");
						});
						response.setStatusCode(200).end(sb.toString());
					}
					else if(request.headers().contains("file") && !request.getHeader("file").isEmpty()) {
						String file = databases.get(request.getHeader("db")).getFile(request.getHeader("file"));
						response.setStatusCode(200).end(file);
					} else {
						StringBuilder sb = new StringBuilder();
						databases.get(request.getHeader("db")).getFiles().forEach((address, name) -> {
							sb.append(address).append(" ").append(name).append("\n");
						});
						response.setStatusCode(200).end(sb.toString());
					}
				} else {
					StringBuilder sb = new StringBuilder();
					databases.forEach((name, database) -> {
						sb.append(name).append("\n");
					});
					response.setStatusCode(200).end(sb.toString());
				}
			}
			if(request.method().equals(HttpMethod.POST)) {
				if(request.headers().contains("db") && !request.getHeader("db").isEmpty()) {
					if(request.headers().contains("file") && !request.getHeader("file").isEmpty()) {
						if(!databases.containsKey(request.getHeader("db"))){
							response.setStatusCode(200).end("Database " + request.getHeader("db") + " does not exists");
						}
						else {
							request.bodyHandler((Buffer buffer) -> {
								databases.get(request.getHeader("db")).insertFile(request.getHeader("file"), buffer.toString());
							});
							response.setStatusCode(200).end("File " + request.getHeader("file") + " inserted in " + request.getHeader("db"));
						}
					} else {
						if(databases.containsKey(request.getHeader("db"))) {
							response.setStatusCode(200).end("Database " + request.getHeader("db") + " already exists");
						} else {
							databases.put(request.getHeader("db"), new Database(request.getHeader("db")));
							response.setStatusCode(200).end("Database " + request.getHeader("db") + " created");
						}
					}
				}
			}
			if(request.method().equals(HttpMethod.DELETE)) {
				if(request.headers().contains("db") && !request.getHeader("db").isEmpty()) {
					if(request.headers().contains("file") && !request.getHeader("file").isEmpty()) {
						databases.get(request.getHeader("db")).DeleteFile(request.getHeader("file"));
					} else {
						if(databases.get(request.getHeader("db")).DeleteDatabase()){
							databases.remove(request.getHeader("db"));
							response.setStatusCode(200).end("Database " + request.getHeader("db") + " deleted");
						} else {
							response.setStatusCode(200).end("Problems encountered with deleting database " + request.getHeader("db"));
						}
					}
				}
			}
		} catch (NullPointerException e) {
			System.out.println("Request not valid");
			response.setStatusCode(404).end("Request not valid");
		}
	}
}
