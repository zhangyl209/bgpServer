package com.example.functions;

import java.util.List;
import java.util.function.Function;

import org.nutz.dao.entity.Record;

import reactor.core.publisher.Flux;

/*
 * 将flux中每一个结果（Iterable）做成flux
 * 
 * 例如flux<List<Object>> to flux<flux<Object>>
 * 
 * 可以对于flux中多个结果都采用同样的后续处理
 * */
public class ResultFlatToBigFlux implements Function<Flux<Object>, Flux<Object>> {

	@SuppressWarnings("unchecked")
	@Override
	public Flux<Object> apply(Flux<Object> flux) {
		// TODO Auto-generated method stub
		Flux<Object> ffr = flux.map(rst -> {
					if (rst instanceof java.util.List) {
						return Flux.fromIterable((List<Record>)rst);
					}
					return Flux.just(rst);
				})
				.concatMap(fr -> fr);
		
		return ffr;
	}
	
}
