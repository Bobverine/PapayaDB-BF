package fr.umlv.papayadb.proxy;

import io.vertx.core.Vertx;

public class MainProxy {
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		Proxy server = new Proxy();
		vertx.deployVerticle(server);
	}
}
