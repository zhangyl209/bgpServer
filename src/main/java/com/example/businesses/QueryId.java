package com.example.businesses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.entity.Record;
import org.springframework.stereotype.Component;

import com.example.commons.InParamsActor;
import com.example.commons.InParamsDb;
import com.example.functions.db.STInsert;
import com.example.functions.db.STQuery;

import reactor.core.publisher.Flux;

public class QueryId implements Function<Flux<Object>, Flux<Object>>{

	@Override
	public Flux<Object> apply(Flux<Object> ts) {
		// TODO Auto-generated method stub
		System.out.println(ts);
		Flux<Object> response = ts.flatMap(t -> {
			Map<String,Object> result = (HashMap<String,Object>)((ArrayList<Object>) t).get(0);
//			Record result = (Record) ((ArrayList<Object>) t).get(0);
			Condition c1 = Cnd.where("orgname","=", result.get("orgname") );
			InParamsDb inParamsDb1 = InParamsDb.builder().table(result.get(".table").toString()).cnd(c1).build();
			Flux<InParamsDb> input1 = Flux.just(inParamsDb1);
			Flux<Object> output = new STQuery().apply(input1);
			
			return output;
		}).doOnError(System.out::println);
		
		return response;
	}
	
	
	

}
