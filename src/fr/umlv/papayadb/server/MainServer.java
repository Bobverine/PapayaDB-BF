package fr.umlv.papayadb.server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import io.vertx.core.json.JsonObject;

public class MainServer {

	public static void main(String[] args) {
		BlockingQueue<JsonObject> queue = new ArrayBlockingQueue<JsonObject>(1024);
		
		Proxy p = new Proxy(queue);
		Database db = new Database(queue);
		
		Thread proxy = new Thread(p);
		Thread database = new Thread(db);
		proxy.start();
		database.start();
	}

}
