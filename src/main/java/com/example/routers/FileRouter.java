package com.example.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.routers.handlers.FileHandler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FileRouter extends CorsRouter {
  // works for a single bean
  @Bean
  public RouterFunction<ServerResponse> fileRoutes(FileHandler fileHandler) {
    return RouterFunctions.route(GET("/f/download/{uuid}"), fileHandler::download)
    		.andRoute(POST("/f/upload"), fileHandler::upload)
    		.andRoute(POST("/f/parserib"), fileHandler::parserib)
    		.andRoute(POST("/f/parseroute"), fileHandler::parseroute)
    		.andRoute(POST("/f/parseirr"), fileHandler::parseirr)
    		.andRoute(POST("/f/parseroa"), fileHandler::parseroa);
  }
  
}
