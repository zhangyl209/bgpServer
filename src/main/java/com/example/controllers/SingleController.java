//package com.example.controllers;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.nutz.dao.Chain;
//import org.nutz.dao.Cnd;
//import org.nutz.dao.Condition;
//import org.nutz.dao.Dao;
//import org.nutz.dao.entity.Record;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.alibaba.fastjson.JSON;
//import com.example.SampleApplication;
//import com.example.commons.InParamsDb;
//import com.example.commons.ResponseInfo;
//import com.example.functions.db.ConditionCRUD;
//import com.example.functions.db.ConditionMake;
//import com.example.functions.db.STCRUD;
//import com.example.functions.db.STClear;
//import com.example.functions.db.STFactory;
//import com.example.functions.db.STFetch;
//import com.example.functions.db.STInsert;
//import com.example.functions.db.STQuery;
//import com.example.functions.db.STUpdate;
//import com.example.nutz.DBTools;
//
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@CrossOrigin(maxAge = 3600)
//@RestController
//@RequestMapping(value = "/sss")//produces = MediaType.TEXT_EVENT_STREAM_VALUE
//public class SingleController {
//
//	@GetMapping(path="/{table}/{id}")
//	public Mono<Object> getOne(@PathVariable String table, @PathVariable String id, String fields) throws InstantiationException, IllegalAccessException {
//		
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put(".table", table);
//		map.put(".type", "getOne");
//		map.put("id", id);
//		map.put("fields", fields);
//		
//		Flux<Map<String,Object>> input = Flux.just(map);
//		
////		Flux<Object> output = new SingleGetOne().apply(input);
//		Flux<Object> output = new ConditionCRUD<STFetch>(STFetch.class).apply(input);
//
//		final Mono<List<Object>> responseData = output.collectList();
//		return responseData.map(responses -> {
//          return ResponseInfo.ok(responses);
//		});
//    }
//	
//	@GetMapping(path="/{table}")
//    public Mono<Object> getList(@PathVariable String table, @RequestParam(name = "pageNum", defaultValue = "1") int pageNum, @RequestParam(name = "pageSize", defaultValue = "50") int pageSize,String order,String sort, @RequestParam String filter) {
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put(".table", table);
//		map.put(".type", "getList");
//		map.put("filter", filter);
//		map.put("pageNum", pageNum);
//		map.put("pageSize", pageSize);
//		map.put("sort", sort);
//		map.put("order", order);
//		
//		Flux<Map<String,Object>> input = Flux.just(map);
////		Flux<Object> output = new SingleGetList().apply(input);
//		Flux<Object> output = new ConditionCRUD<STQuery>(STQuery.class).apply(input);
//
//		final Mono<List<Object>> responseData = output.collectList();
//		return responseData.map(responses -> {
//          return ResponseInfo.ok(responses);
//		});
//    }
//	
//	@PostMapping(path="/{table}")
//	public Mono<Object> create(@PathVariable String table, String data){
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put(".table", table);
//		map.put(".type", "create");
//		map.put("data", data);
//		
//		Flux<Map<String,Object>> input = Flux.just(map);
////		Flux<Object> output = new SingleCreate().apply(input);
//		Flux<Object> output = new ConditionCRUD<STInsert>(STInsert.class).apply(input);
//		
//		final Mono<List<Object>> responseData = output.collectList();
//		return responseData.map(responses -> {
//          return ResponseInfo.ok(responses);
//		});
//	}
//	
//	@DeleteMapping(path="/{table}/{ids}")
//    public Mono<Object> delete(@PathVariable String table, @PathVariable String ids) {
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put(".table", table);
//		map.put(".type", "delete");
//		map.put("ids", ids);
//		
//		Flux<Map<String,Object>> input = Flux.just(map);
////		Flux<Object> output = new SingleDelete().apply(input);
//		Flux<Object> output = new ConditionCRUD<STClear>(STClear.class).apply(input);
//
//		final Mono<List<Object>> responseData = output.collectList();
//		return responseData.map(responses -> {
//          return ResponseInfo.ok(responses);
//		});
//    }
//	
//	@PutMapping(path="/{table}/{ids}")
//	public Mono<Object> update(@PathVariable String table, @PathVariable String ids, String data) {
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put(".table", table);
//		map.put(".type", "update");
//		map.put("ids", ids);
//		map.put("data", data);
//		
//		Flux<Map<String,Object>> input = Flux.just(map);
////		Flux<Object> output = new SingleDelete().apply(input);
//		Flux<Object> output = new ConditionCRUD<STUpdate>(STUpdate.class).apply(input);
//
//		final Mono<List<Object>> responseData = output.collectList();
//		return responseData.map(responses -> {
//          return ResponseInfo.ok(responses);
//		});
//    }
//	
//}
