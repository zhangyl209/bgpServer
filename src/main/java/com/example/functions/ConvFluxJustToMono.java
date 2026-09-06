package com.example.functions;

import java.util.List;
import java.util.function.Function;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*
 * 对于只有一个值的flux，可以转换成mono
 * */
public class ConvFluxJustToMono implements Function<Flux<Object>, Mono<Object>> {

	@SuppressWarnings("unchecked")
	@Override
	public Mono<Object> apply(Flux<Object> flux) {

		return flux.take(1).single().map(lr -> ((List<Object>)lr).get(0));
	}

}
