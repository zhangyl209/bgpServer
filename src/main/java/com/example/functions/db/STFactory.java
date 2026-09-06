package com.example.functions.db;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;

import com.example.commons.InParamsDb;

import reactor.core.publisher.Flux;

public class STFactory<T extends STCRUD>{

	public T getInstance(Class<T> generic) {
		T t = null;		
		try {			
			t = generic.getDeclaredConstructor().newInstance();
		} catch (Exception e) {	
			e.printStackTrace();		
		}		
		return t;	
	} 	
	
	public static void main(String[] args) throws ClassNotFoundException {	
		STFactory factory = new STFactory<STCRUD>();
		
		STCRUD stfetch = factory.getInstance(STFetch.class);
		
		Condition c = Cnd.NEW();
        c = Cnd.where("id", "=", 1);
		
		InParamsDb inParamsDb = InParamsDb.builder().table("users").cnd(c).build();
		
		Flux<InParamsDb> input = Flux.just(inParamsDb);
		
		Flux<Object> output = stfetch.apply(input);	
		
		List<Object> results = output.collectList().block();
		
		System.out.println(results.size());
		System.out.println(results.get(0));
	}
	
}
