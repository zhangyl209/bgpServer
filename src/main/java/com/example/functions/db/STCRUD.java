package com.example.functions.db;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.example.commons.InParamsDb;

import reactor.core.publisher.Flux;

public class STCRUD implements Function<Flux<InParamsDb>, Flux<Object>> {

	@Override
	public Flux<Object> apply(Flux<InParamsDb> flux) {
		// TODO Auto-generated method stub
		return null;
	}

}
