package fr.umlv.papayadb.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class RequestHandler extends AbstractVerticle {

	@Override
	public void start(Future<Void> arg0) throws Exception {
		Router router = Router.router(vertx);
	    
	    // route to JSON REST APIs 
	    /*router.get("/all").handler(this::getAllDBs);
	    router.get("/get/:name/:id").handler(this::getARecord);*/
	    
	    // otherwise serve static pages
	    router.route().handler(StaticHandler.create());

	    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	    System.out.println("Request server listening on port 8080");
	}
}
