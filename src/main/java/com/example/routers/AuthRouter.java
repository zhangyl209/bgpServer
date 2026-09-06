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

import com.example.routers.handlers.AuthHandler;

@Configuration
public class AuthRouter extends CorsRouter {

	@Bean
	  public RouterFunction<ServerResponse> openRoutes(AuthHandler authHandler) {
	    return RouterFunctions.route(POST("/auth/login"), authHandler::login)
	    		.andRoute(GET("/auth/logout"), authHandler::logout)
	    		.andRoute(GET("/auth/getUserInfo"), authHandler::getUserInfo)
	    		.andRoute(GET("/auth/getPermCode"), authHandler::getPermCode);
	  }
}
