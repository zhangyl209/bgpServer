package com.example.routers.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.commons.ResponseInfo;
import com.example.functions.BusinessManager;
import com.example.functions.db.ConditionCRUD;
import com.example.functions.db.STQuery;
import com.example.functions.db.STSql;
import com.example.nutz.DBTools;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CommonHandler {
	
	private final Mono<SecurityContext> context  = ReactiveSecurityContextHolder.getContext();
	
	private Dao dao = DBTools.getDao();
	
	private Mono<User> extractUserSeqIdFromJwtToken(Mono<SecurityContext> context) {
        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
          .map(s -> s.getAuthentication().getPrincipal())
          .cast(User.class);
    }
	
	//获取多个table表的信息，用于获取基表
	public Mono<ServerResponse> getTables(ServerRequest request) {
		
		String tables = request.queryParam("tables").get();
    	String[] tableArray = tables.split(",");
    	
    	List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
    	
    	for (int i = 0 ; i < tableArray.length; i++) {
    		Map<String,Object> map = new HashMap<String,Object>();
    		map.put(".table", tableArray[i]);
    		map.put(".type", "getList");
    		request.queryParam("filter").ifPresent(c->map.put("filter", c));
    		
    		listMap.add(map);
    	}
		
		Flux<Map<String,Object>> input = Flux.fromIterable(listMap);
//		Flux<Object> output = new SingleGetList().apply(input);
		Flux<Object> output = new ConditionCRUD<STQuery>(STQuery.class).apply(input);

		final Mono<List<Object>> responseData = output.collectList();
		
		Mono<Object> body = responseData.map(responses -> {
	          return ResponseInfo.ok(responses);
			});
		
		return ServerResponse.ok().body(body, ResponseInfo.class);
    }
	
//	//自定义sql---前端sql不安全
//	//获取表自增序列:SELECT auto_increment FROM information_schema.`TABLES` WHERE TABLE_SCHEMA='ndtweb' AND TABLE_NAME='user';
//	public Mono<ServerResponse> exeSql(ServerRequest request) {
//		
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put(".type", "sql");
//		request.queryParam("sql").ifPresent(c->map.put("sql", c));
//		request.queryParam("params").ifPresent(c->map.put("params", c));
//		
//		Flux<Map<String,Object>> input = Flux.just(map);
//		Flux<Object> output = new ConditionCRUD<STSql>(STSql.class).apply(input);
//
//		final Mono<List<Object>> responseData = output.collectList();
//		
//		Mono<Object> body = responseData.map(responses -> {
//	          return ResponseInfo.ok(responses);
//			});
//		
//		return ServerResponse.ok().body(body, ResponseInfo.class);
//    }
	
	//自定义sql
	//获取表自增序列:SELECT auto_increment FROM information_schema.`TABLES` WHERE TABLE_SCHEMA='ndtweb' AND TABLE_NAME='user';
	public Mono<ServerResponse> exeSql(ServerRequest request) {
		
		String business =request.queryParam("business").get();
		Record businessRecord = dao.fetch("config_business", Cnd.where("business", "=", business).and("status", "<>", 0));
		
		if (businessRecord == null) {
			return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("无效business"));
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(".type", "sql");
		map.put("sql", businessRecord.getString("sql"));
		request.queryParam("params").ifPresent(c->map.put("params", c));
		
		Flux<Map<String,Object>> input = Flux.just(map);
		Flux<Object> output = new ConditionCRUD<STSql>(STSql.class).apply(input);

		final Mono<List<Object>> responseData = output.collectList();
		
		Mono<Object> body = responseData.map(responses -> {
	          return ResponseInfo.ok(responses.size() == 1 ? responses.get(0) : null);
			});
		
		return ServerResponse.ok().body(body, ResponseInfo.class);
    }
	
	//执行business: get;不带数据data
	public Mono<ServerResponse> excute(ServerRequest request) {
		
		String business =request.queryParam("business").get();
		
		return extractUserSeqIdFromJwtToken(context).flatMap(user -> {
			String username = user.getUsername();
			
			Record userRecord = dao.fetch("account", Cnd.where("username", "=", username).and("status", "<>", 0));
			if (userRecord != null) {
				String userId = userRecord.getString("id");
				
				Map<String,Object> map = new HashMap<String,Object>();
				map.put(".type", "excute");
				map.put("business", business);
				map.put(".auth", userId);
				
				Flux<Object> output = new BusinessManager().apply(map);

				final Mono<List<Object>> responseData = output.collectList();
				
				Mono<Object> body = responseData.map(responses -> {
			          return ResponseInfo.ok(responses);
					});
				
				return ServerResponse.ok().body(body, ResponseInfo.class);
			}
			
			return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("非系统有效用户"));
			
		});
    }
	
	//执行business: get;不带数据data
	public Mono<ServerResponse> excute_noAuth(ServerRequest request) {
		
		String business =request.queryParam("business").get();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(".type", "excute");
		map.put("business", business);
		
		Flux<Object> output = new BusinessManager().apply(map);

		final Mono<List<Object>> responseData = output.collectList();
		
		Mono<Object> body = responseData.map(responses -> {
	          return ResponseInfo.ok(responses);
			});
		
		return ServerResponse.ok().body(body, ResponseInfo.class);
    }
	
	//执行business: post;带数据data（body；带.table 带.cnd）;条件参数优先级record比config_input高
	public Mono<ServerResponse> excutewith(ServerRequest request) {
		
		String business =request.queryParam("business").get();
		
		return extractUserSeqIdFromJwtToken(context).flatMap(user -> {
			String username = user.getUsername();
			
			Record userRecord = dao.fetch("account", Cnd.where("username", "=", username).and("status", "<>", 0));
			if (userRecord != null) {
				String userId = userRecord.getString("id");
				
				return request.bodyToMono(Record.class)
						.flatMap(data -> {
							
							Record record = new Record();
							record.put(".type", "excutewith");
							record.put("business", business);
							record.put("data", data);
							record.put(".auth", userId);
							
							return new BusinessManager().apply(record).collectList();
						})
						.map(responses -> ResponseInfo.ok(responses.size() == 1 ? responses.get(0) : responses) )
						.flatMap(responseInfo -> ServerResponse.ok().body(Mono.just(responseInfo), ResponseInfo.class));
			}
			
			return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("非系统有效用户"));
			
		});	
		
    }
	
	//执行business: post;带数据data（body；带.table 带.cnd）;条件参数优先级record比config_input高
	public Mono<ServerResponse> excutewith_noAuth(ServerRequest request) {
		
		String business =request.queryParam("business").get();
		
		return request.bodyToMono(Record.class)
				.flatMap(data -> {
					
					Record record = new Record();
					record.put(".type", "excutewith");
					record.put("business", business);
					record.put("data", data);
					
					return new BusinessManager().apply(record).collectList();
				})
				.map(responses -> ResponseInfo.ok(responses.size() == 1 ? responses.get(0) : responses) )
				.flatMap(responseInfo -> ServerResponse.ok().body(Mono.just(responseInfo), ResponseInfo.class));
		
    }
	
	//执行business: post;带数据data（body；带.table 带.cnd）;条件参数优先级record比config_input高
		public Mono<ServerResponse> querywith(ServerRequest request) {
			
			String business =request.queryParam("business").get();
			
			return extractUserSeqIdFromJwtToken(context).flatMap(user -> {
				String username = user.getUsername();
				
				Record userRecord = dao.fetch("account", Cnd.where("username", "=", username).and("status", "<>", 0));
				if (userRecord != null) {
					String userId = userRecord.getString("id");
					
					return request.bodyToMono(Record.class)
							.flatMap(data -> {
								
								Record record = new Record();
								record.put(".type", "excutewith");
								record.put("business", business);
								record.put("data", data);
								record.put(".auth", userId);
								
								return new BusinessManager().apply(record).collectList();
							})
							.map(responses -> ResponseInfo.ok(responses) )
							.flatMap(responseInfo -> ServerResponse.ok().body(Mono.just(responseInfo), ResponseInfo.class));
				}
				
				return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("非系统有效用户"));
				
			});	
			
	    }
	
	//执行business: post;带数据data（body；带.table 带.cnd）;条件参数优先级record比config_input高
	public Mono<ServerResponse> excutewithmany(ServerRequest request) {
		
		String business =request.queryParam("business").get();
		
		return extractUserSeqIdFromJwtToken(context).flatMap(user -> {
			String username = user.getUsername();
			
			Record userRecord = dao.fetch("account", Cnd.where("username", "=", username).and("status", "<>", 0));
			if (userRecord != null) {
				String userId = userRecord.getString("id");
				
				return request.bodyToFlux(Record.class)
						.flatMap(data -> {
							
							Record record = new Record();
							record.put(".type", "excutewith");
							record.put("business", business);
							record.put("data", data);
							record.put(".auth", userId);
							
							return new BusinessManager().apply(record).collectList();
						})
						.map(flux -> flux.get(0))
						.collectList()
						.map(responses -> ResponseInfo.ok(responses) )
						.flatMap(responseInfo -> ServerResponse.ok().body(Mono.just(responseInfo), ResponseInfo.class));
			}
			
			return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(ResponseInfo.not("非系统有效用户"));
			
		});	
	
    }
	
	//执行business: post;带数据data（body；带.table 带.cnd）;条件参数优先级record比config_input高
	public Mono<ServerResponse> excutewithmany_noAuth(ServerRequest request) {
		
		String business =request.queryParam("business").get();
		
		return request.bodyToFlux(Record.class)
				.flatMap(data -> {
					
					Record record = new Record();
					record.put(".type", "excutewith");
					record.put("business", business);
					record.put("data", data);
					
					return new BusinessManager().apply(record).collectList();
				})
				.map(flux -> flux.get(0))
				.collectList()
				.map(responses -> ResponseInfo.ok(responses) )
				.flatMap(responseInfo -> ServerResponse.ok().body(Mono.just(responseInfo), ResponseInfo.class));
		
    }
	
	

}
