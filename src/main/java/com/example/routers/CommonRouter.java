package com.example.routers;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.routers.handlers.CommonHandler;

@Configuration
public class CommonRouter extends CorsRouter {

	@Bean
	  public RouterFunction<ServerResponse> businessRoutes(CommonHandler commonHandler) {
	    return RouterFunctions.route(GET("/b/getTables"), commonHandler::getTables)
	    		.andRoute(GET("/b/exeSql"), commonHandler::exeSql)
	    		.andRoute(GET("/b/excute"), commonHandler::excute)
	    		.andRoute(GET("/open/b/excute"), commonHandler::excute_noAuth)
	    		.andRoute(POST("/b/excutewith"), commonHandler::excutewith)
	    		.andRoute(POST("/b/querywith"), commonHandler::querywith)
	    		.andRoute(POST("/open/b/excutewith"), commonHandler::excutewith_noAuth)
	    		.andRoute(POST("/b/excutewithmany"), commonHandler::excutewithmany)
	    		.andRoute(POST("/open/b/excutewithmany"), commonHandler::excutewithmany_noAuth);
	  }
}
