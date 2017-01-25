package fr.umlv.papayadb.server;

import java.util.concurrent.BlockingQueue;

import io.vertx.core.json.JsonObject;

public class Database implements Runnable {
	private final BlockingQueue<JsonObject> queue;

	public Database(BlockingQueue<JsonObject> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		while(true){ //à changer, c'était pour faire un test
			try {
				System.out.println(queue.take());
	        } catch (InterruptedException e) {
	            
	        }
		}
	}

}
