package com.example.businesses;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.nutz.dao.entity.Record;

@Component
public class TestBusiness {
	
	@SuppressWarnings("unchecked")
	@Bean
	public Function<Flux<Object>, Flux<Object>> testf1() {
		return flux -> flux.map(value -> {
			
			LinkedList<Object> ll = new LinkedList<Object>();
			if (!(value instanceof List)) {
				ll.add(value);
			} else {
				ll = (LinkedList<Object>)value;
			}
			
			for (int i = 0 ; i< ll.size();i++) {
				org.nutz.dao.entity.Record o = (org.nutz.dao.entity.Record)ll.get(i);
				o.put("a", 1);
				ll.remove(i);
				ll.add(i, o);
			}
			
			return ll;
		});
	}
	
	@Bean
	public Function<Flux<Object>, Flux<Object>> testf2() {
		return flux -> flux.map(value -> {
			LinkedList<Object> ll = new LinkedList<Object>();
			if (!(value instanceof List)) {
				ll.add(value);
			} else {
				ll = (LinkedList<Object>)value;
			}
			
			for (int i = 0 ; i< ll.size();i++) {
				org.nutz.dao.entity.Record o = (org.nutz.dao.entity.Record)ll.get(i);
				o.put("b", 2);
				ll.remove(i);
				ll.add(i, o);
			}
			
			return ll;
		});
	}
	
	@Bean
	public Function<Object, String> testMapper() {
		
		return o -> {
			Record r = (Record)o;
			
			if (r.getInt("id") < 18) {
				return "a";
			} else if (r.getInt("id") < 20) {
				return "b";
			} else if (r.getInt("id") > 20) {
				return "c";
			}
			
			return null;
		};	
	}
	
	@SuppressWarnings("unchecked")
	@Bean
	public Function<Flux<Object>, Mono<Object>> testm1() {
		return flux -> flux.map(value -> {
			
			LinkedList<Object> ll = new LinkedList<Object>();
			if (!(value instanceof List)) {
				ll.add(value);
			} else {
				ll = (LinkedList<Object>)value;
			}
			
			for (int i = 0 ; i< ll.size();i++) {
				org.nutz.dao.entity.Record o = (org.nutz.dao.entity.Record)ll.get(i);
				o.put("a", 1);
				ll.remove(i);
				ll.add(i, o);
			}
			
			return ll;
		}).take(1).single().map(lr -> lr.get(0));
	}
	
	
	@Bean
	public Function<Flux<Object>, Flux<Object>> testadd() {
		
		return flux -> {
			
			Mono<List<Object>> ml = flux.collectList();
			
			List<Object> ll = ml.block();
			
			int counta = 0;
			int countb = 0;
			for (int i = 0 ; i< ll.size();i++) {
				org.nutz.dao.entity.Record o = (org.nutz.dao.entity.Record)ll.get(i);
				if (o.getInt("a") > 0) {
					counta++;
				}
				if (o.getInt("b") > 0) {
					countb++;
				}
			}
			
			
			return Flux.just("counta: " + counta + ", countb: " + countb);
			
		};
		
//		return flux -> flux.map(value -> {
//			LinkedList<Object> ll = new LinkedList<Object>();
//			if (!(value instanceof List)) {
//				ll.add(value);
//			} else {
//				ll = (LinkedList<Object>)value;
//			}
//			
//			int counta = 0;
//			int countb = 0;
//			
//			for (int i = 0 ; i< ll.size();i++) {
//				org.nutz.dao.entity.Record o = (org.nutz.dao.entity.Record)ll.get(i);
//				if (o.getInt("a") > 0) {
//					counta++;
//				}
//				if (o.getInt("b") > 0) {
//					countb++;
//				}
//			}
//			
//			return "counta: " + counta + ", countb: " + countb;
//		});
	}
}
