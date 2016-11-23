package fr.umlv.papayadb.db;

import io.vertx.core.Vertx;

public class Main {
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		Database db = new Database();
		vertx.deployVerticle(db);
	}
}
