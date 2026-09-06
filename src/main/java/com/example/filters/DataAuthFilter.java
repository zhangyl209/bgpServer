package com.example.filters;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.springframework.http.server.PathContainer.Element;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.example.SampleApplication;
import com.example.commons.InParamsDb;
import com.example.functions.db.STQuery;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DataAuthFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		
		System.out.println(exchange.getRequest().getPath());
		System.out.println(exchange.getRequest().getQueryParams());
		System.out.println(exchange.getRequest().getURI());
		
//		ServerWebExchange newExchange = exchange;
		
//		org.nutz.dao.entity.Record userNow=(org.nutz.dao.entity.Record) SecurityUtils.getSubject().getPrincipal();
//		System.out.println(userNow);
		
		
//		RequestPath path = exchange.getRequest().getPath();
//		List<Element> elements = path.elements();
//		String type = elements.get(1).value();
//		boolean test = elements.contains("s");
//		
//		if (type.equals("s")) { //单表操作
//			String table = elements.get(3).toString();
//			//获取CRUD
//			//判断表权限
//			//获取字段权限
//			//设置字段
//			ServerHttpRequest newRequest = exchange.getRequest();
//			MultiValueMap<String, String> params = newRequest.getQueryParams();
//			params.set("fields", "id,name");
//			
////			ServerHttpResponse newResponse = exchange.getResponse();
//			newExchange = exchange.mutate().request(newRequest).build();
//			
//		}
		
		
		return chain.filter(exchange);
	}

}
