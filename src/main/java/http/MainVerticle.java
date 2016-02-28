package http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

public class MainVerticle extends AbstractVerticle {

	/**
	 * Ends response with a JSON list of objects
	 * 
	 * @param objects
	 * @return
	 */
	private static Handler<RoutingContext> json(Object... objects) {
		return (RoutingContext rc) -> rc.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encode(objects));
	}

	@Override
	public void start() {

		Router router = Router.router(vertx);

		// return a list of messages
		router.route(HttpMethod.GET, "/api/messages").handler(
				json(new Message(42, "Hello World!"), new Message(42, "Hello World!")));

		// disabled cache for static asset reload
		router.route("/*").handler(
				StaticHandler.create().setCachingEnabled(!Boolean.getBoolean("vertx.disableFileCaching")));

		vertx.createHttpServer().requestHandler(router::accept)
				.listen(Integer.getInteger("server.port", 8080), System.getProperty("server.host", "0.0.0.0"));
	}
}
