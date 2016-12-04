package fr.umlv.papayadb.db;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;

public class TCPHandler extends AbstractVerticle {
	private NetServer server;
	private final NetServerOptions options;
	//private final FileChannel file;
	
	public TCPHandler()/* throws FileNotFoundException*/ {
		this.options = new NetServerOptions().setHost("127.0.0.1").setPort(7070);
		//RandomAccessFile aFile = new RandomAccessFile("papaya.db", "rw");
		//this.file = aFile.getChannel();
	}
	
	@Override
	public void start() throws Exception {
		server = vertx.createNetServer(this.options);
		server.connectHandler(this::onTCPRequest);
		
		server.listen(res -> {
			if(res.succeeded()) {
				System.out.println("Database is up & ready to listen on " + options.getHost() + ":" + options.getPort());
			} else {
				System.out.println("Database has failed to start : " + res.cause());
			}
		});
	}
	
	@Override
	public void stop() throws Exception {
		server.close(res -> {
			if(res.succeeded()) {
				System.out.println("Database is down");
			} else {
				System.out.println("Database has failed to stop");
			}
		});
	}
	
	public void onTCPRequest(NetSocket socket) {
		socket.handler(buffer -> {
			JsonObject json = new JsonObject();
			json.readFromBuffer(0, buffer);
			System.out.println(json);
		});
	}
}
