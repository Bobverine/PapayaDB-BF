package fr.umlv.papayadb.proxy;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;

class HttpHandler extends AbstractVerticle {
	
	private final HttpServer server;
	private final HttpServerOptions options;
	
	public HttpHandler() {
		this.options = new HttpServerOptions().setHost("127.0.0.1").setPort(80);
		this.server = Vertx.vertx().createHttpServer(this.options);
		this.server.requestHandler(this::onHTTPRequest);
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
	
	public void onHTTPRequest(HttpServerRequest request) {
		request.handler(buffer -> {
			System.out.println(buffer);
		});
	}

}
