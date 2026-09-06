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

import com.example.routers.handlers.MTHandler;

@Configuration
public class MTRouter extends CorsRouter{
  // works for a single bean
  @Bean
  public RouterFunction<ServerResponse> mtRoutes(MTHandler multiHandler) {
    return RouterFunctions.route(GET("/m/insert"), multiHandler::insertDemo)
    		.andRoute(GET("/m/insertAndFetch"), multiHandler::insertAndFetchFunction)
    		.andRoute(GET("/m/s1"), multiHandler::serial1)
    		.andRoute(GET("/m/p1"), multiHandler::parallel1)
    		.andRoute(GET("/m/f1"), multiHandler::funcFlow1)
    		.andRoute(GET("/m/f2"), multiHandler::funcFlow2)
    		.andRoute(GET("/m/f3"), multiHandler::funcFlow3)
    		.andRoute(GET("/m/f4"), multiHandler::funcFlow4);
  }
  
}


