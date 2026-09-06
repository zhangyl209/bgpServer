package com.example.functions;

import java.util.List;
import java.util.function.Function;

import com.example.commons.InParamsActor;
import com.example.utils.SpringUtils;

import reactor.core.publisher.Flux;

/*
 * 可以将函数组合成流执行
 * */
public class FuncFlow implements Function<InParamsActor, Flux<Object>> {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Flux<Object> apply(InParamsActor actor) {
		// TODO Auto-generated method stub
		
		List<String> workerL = actor.getClassnameList();
		
		Function f = SpringUtils.createFuncFlowByListOfClassnameOrBean(workerL);
		
		return (Flux<Object>) f.apply(actor.getInput());
	}
	

	

}
