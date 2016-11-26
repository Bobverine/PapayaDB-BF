package fr.umlv.papayadb.proxy;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;

class HttpHandler extends AbstractVerticle {
	
	private HttpServer server;
	private final HttpServerOptions options;
	
	public HttpHandler() {
		this.options = new HttpServerOptions().setHost("127.0.0.1").setPort(8080);
	}
	
	@Override
	public void start() throws Exception {
		this.server = vertx.createHttpServer(this.options);
		this.server.requestHandler(this::onHTTPRequest);
		/*Router router = Router.router(vertx);
		server.requestHandler(router::accept);*/

		server.listen(res -> {
			if(res.succeeded()) {
				System.out.println("HttpServer is up & ready to listen on " + options.getHost() + ":" + options.getPort());
			} else {
				System.out.println("HttpServer has failed to start : " + res.cause());
			}
		});
	}
	
	@Override
	public void stop() throws Exception {
		server.close(res -> {
			if(res.succeeded()) {
				System.out.println("HttpServer is down");
			} else {
				System.out.println("HttpServer has failed to stop");
			}
		});
	}
	
	public void onHTTPRequest(HttpServerRequest request) {
		request.handler(buffer -> {
			System.out.println(buffer);
		});
	}

}
