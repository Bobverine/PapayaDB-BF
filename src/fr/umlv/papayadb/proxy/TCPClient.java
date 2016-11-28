package fr.umlv.papayadb.proxy;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

public class TCPClient extends AbstractVerticle {
	
	public void startAndSend(JsonObject json){
		Vertx vertx = Vertx.vertx();
		NetClientOptions options = new NetClientOptions().setConnectTimeout(10000);
		NetClient client = vertx.createNetClient(options);
		client.connect(7070, "127.0.0.1", res -> {
		  if (res.succeeded()) {
		    System.out.println("Connected!");
		    NetSocket socket = res.result();
		    socket.write((Buffer) json);
		  } else {
		    System.out.println("Failed to connect: " + res.cause().getMessage());
		  }
		});
	}
}
