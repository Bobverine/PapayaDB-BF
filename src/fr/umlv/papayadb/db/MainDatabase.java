package fr.umlv.papayadb.db;

import io.vertx.core.Vertx;

public class MainDatabase {
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		TCPHandler db = new TCPHandler();
		vertx.deployVerticle(db);
	}
}
