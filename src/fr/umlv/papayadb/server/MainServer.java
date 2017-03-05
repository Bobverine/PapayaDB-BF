package fr.umlv.papayadb.server;

import io.vertx.core.Vertx;

public class MainServer {

	public static void main(String[] args) {
		Proxy p = new Proxy();
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(p);
	}

}
