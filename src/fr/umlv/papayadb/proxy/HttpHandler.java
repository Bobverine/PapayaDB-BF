package fr.umlv.papayadb.proxy;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

class HttpHandler extends AbstractVerticle {
	
	private HttpServer server;
	private final HttpServerOptions httpOptions;
	private NetClient client;
	private final NetClientOptions tcpOptions;
	
	/**
	 * Constructeur par défaut qui définit les options du serveur http et du client tcp
	 */
	public HttpHandler() {
		this.httpOptions = new HttpServerOptions().setHost("127.0.0.1").setPort(8080)/*.setPort(443).setSsl(true).setKeyStoreOptions(
				new JksOptions().
		        setPath("src/fr/umlv/papayadb/proxy/keystore.jks").
		        setPassword("papaya")
				).setClientAuth(ClientAuth.REQUIRED).
			    setTrustStoreOptions(
			            new JksOptions().
			                setPath("src/fr/umlv/papayadb/proxy/cacerts.jks").
			                setPassword("papaya")
			    )*/;
		this.tcpOptions = new NetClientOptions().setConnectTimeout(10000);
	}
	
	/**
	 * Méthode permettant de démarer le serveur (est utilisé par deployVerticle de Vertx)
	 */
	@Override
	public void start() throws Exception {
		this.server = vertx.createHttpServer(this.httpOptions);
		this.server.requestHandler(this::onHTTPRequest);
		this.client = vertx.createNetClient(this.tcpOptions);
		server.listen(res -> {
			if(res.succeeded()) {
				System.out.println("HttpServer is up & ready to listen on " + httpOptions.getHost() + ":" + httpOptions.getPort());
			} else {
				System.out.println("HttpServer has failed to start : " + res.cause());
			}
		});
	}
	
	/**
	 * Méthode permettant d'arrèter le serveur TCP
	 */
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
	 * Défini ce qui sera fait lors de la réception d'une requête
	 * 
	 * @param request
	 *            requpete reçue
	 */
	public void onHTTPRequest(HttpServerRequest request) {
		JsonObject json = RequestConverter.UrlToJson(request);
		Buffer buffer = Buffer.buffer();
		json.writeToBuffer(buffer);
		client.connect(7070, "127.0.0.1", res -> {
			if(res.succeeded()) {
			    System.out.println("Connected!");
			    NetSocket socket = res.result();
			    socket.write(buffer);
			} else {
				System.out.println("Failed to connect: " + res.cause().getMessage());
			}
		});
		request.response().end("Hello world");
		client.close();
	}
}
