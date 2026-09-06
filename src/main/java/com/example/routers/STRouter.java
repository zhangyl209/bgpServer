package com.example.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.routers.handlers.MTHandler;
import com.example.routers.handlers.STHandler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class STRouter extends CorsRouter {
  // works for a single bean
  @Bean
  public RouterFunction<ServerResponse> stRoutes(STHandler singleHandler) {
    return RouterFunctions.route(GET("/s/{table}/{id}"), singleHandler::getOne)
    		.andRoute(GET("/count/s/{table}"), singleHandler::count)
    		.andRoute(GET("/s/{table}"), singleHandler::getList)
    		.andRoute(GET("/open/s/{table}"), singleHandler::getList_noAuth)
    		.andRoute(POST("/s/{table}"), singleHandler::create)
    		.andRoute(PUT("/s/{table}/{ids}"), singleHandler::update)
    		.andRoute(DELETE("/s/{table}/{ids}"), singleHandler::delete)
			.andRoute(POST("/s/{table}/{sceneid}"), singleHandler::updateAll)
			.andRoute(POST("/loadScene/{sceneid}"), singleHandler::LoadScene)
			.andRoute(GET("/getNodesWithASN/{sceneid}"), singleHandler::getNodesWithoutASN)
			.andRoute(GET("/getNodesWithASN/{sceneid}/{asn}"), singleHandler::getNodesWithASN)
			.andRoute(POST("/updataTier/{sceneid}"), singleHandler::updataTier)
			.andRoute(GET("/getNodesWithTier/{sceneid}"), singleHandler::getNodesWithTier)
			.andRoute(POST("/insertOperation/{sceneid}"), singleHandler::insertOperation)
			.andRoute(GET("/getOperations/{sceneid}"), singleHandler::getOperations)
			.andRoute(GET("/searchNodes/{sceneid}/{asn}"), singleHandler::searchNodes)
			.andRoute(POST("/interrupt/{sceneid}/{sessionid}"), singleHandler::interrupt)
			.andRoute(GET("/interruptCancel/{sceneid}/{sessionid}"), singleHandler::interruptCancel)
			.andRoute(POST("/IntraNetRerouting/{sceneid}/{sessionid}"), singleHandler::IntraNetWorkRerouting)
			.andRoute(GET("/InterNetRerouting/{sceneid}/{sessionid}"), singleHandler::InterNetWorkRerouting)
			.andRoute(POST("/searchLinkByPrefix/{sceneid}/{sessionid}"), singleHandler::searchLinkByPrefix)
			.andRoute(POST("/hijack/{sceneid}/{sessionid}"), singleHandler::hijack)
			.andRoute(POST("/resHijack/{sceneid}/{sessionid}"), singleHandler::resHijack)
			.andRoute(GET("/searchPrefixByAsn/{sceneid}/{sessionid}/{nodeid}"), singleHandler::searchPrefixByAsn)
			.andRoute(POST("/getCntNetWork/{sceneid}/{sessionid}"), singleHandler::getCntNetWork);
  }
  
}
