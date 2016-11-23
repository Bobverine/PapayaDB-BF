package fr.umlv.papayadb.db;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;

public class Database extends AbstractVerticle {
	private final NetServer server;
	private final NetServerOptions options;
	
	public Database() {
		this.options = new NetServerOptions().setHost("127.0.0.1").setPort(7070);
		this.server = getVertx().createNetServer(this.options);
		this.server.connectHandler(this::onTCPRequest);
	}
	
	@Override
	public void start() throws Exception {
		super.start();
		server.listen(res -> {
			if(res.succeeded()) {
				System.out.println("Database is up & ready to listen on " + options.getHost() + ":" + options.getPort());
				
			} else {
				System.out.println("Database has failed to start");
			}
		});
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
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
			System.out.println(buffer);
		});
	}
}
