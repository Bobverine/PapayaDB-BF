package fr.umlv.papayadb.server;

import io.vertx.core.Vertx;

public class MainServer {

	public static void main(String[] args) {
		Database db = new Database();
		Proxy p = new Proxy(db);
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(p);
	}

}
