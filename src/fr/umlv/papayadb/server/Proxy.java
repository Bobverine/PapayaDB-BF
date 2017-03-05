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
import io.vertx.core.json.JsonObject;

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
			databases.put(file.getName(), new Database(file.getName()));
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
	
	public void onHTTPRequest(HttpServerRequest request) {
		//gestion utilisateur
		HttpServerResponse response = request.response();
		try {
			if(request.method().equals(HttpMethod.GET)) {
				if(request.headers().contains("db") && !request.getHeader("db").isEmpty()) {
					if(request.headers().contains("filter") && !request.getHeader("filter").isEmpty()) {
						StringBuilder sb = new StringBuilder();
						databases.get(request.getHeader("db")).getFiles(request.getHeader("db"), request.getHeader("filter")).forEach((address, name) -> {
							sb.append(address).append(" ").append(name).append("\n");
						});
						response.setStatusCode(200).end(sb.toString());
					}
					if(request.headers().contains("file") && !request.getHeader("file").isEmpty()) {
						File file = databases.get(request.getHeader("db")).getFile(request.getHeader("file"));
						response.setStatusCode(200).end();
					}
				} else {
					StringBuilder sb = new StringBuilder();
					databases.forEach((address, name) -> {
						sb.append(name).append("\n");
					});
					response.setStatusCode(200).end(sb.toString());
				}
			}
			if(request.method().equals(HttpMethod.POST)) {
				if(request.headers().contains("db") && !request.getHeader("db").isEmpty()) {
					if(request.headers().contains("file") && !request.getHeader("file").isEmpty()) {
						request.bodyHandler((Buffer buffer) -> {
							databases.get(request.getHeader("db")).insertFile(request.getHeader("db"), request.getHeader("file"), buffer.toString());
						});
						response.setStatusCode(200).end("File " + request.getHeader("file") + " inserted in " + request.getHeader("db"));
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
						databases.get(request.getHeader("db")).DeleteDatabase();
					}
				}
			}
		} catch (NullPointerException e) {
			System.out.println("Request not valid");
			response.setStatusCode(404).end("Request not valid");
			e.printStackTrace();
		}
	}
}
