package fr.umlv.papayadb.proxy;

import io.vertx.core.Vertx;

public class Proxy {
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		HttpHandler server = new HttpHandler();
		vertx.deployVerticle(server);
		
	}

}
