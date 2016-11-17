package fr.umlv.papayadb.server;

import io.vertx.core.Vertx;

public class Server {

	public static void main(String[] args) {
	    Vertx vertx = Vertx.vertx();
	    RequestHandler requestHandler = new RequestHandler();
	    vertx.deployVerticle(requestHandler);
	}

}
