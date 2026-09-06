package com.example.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.alibaba.fastjson.JSONObject;
import com.example.commons.InParamsActor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;

/*
 * 对于Flux<GroupedFlux<Object, Object>>进行处理
 * */
public class GroupedFluxSolve implements Function<InParamsActor, Flux<Object>> {

	@Override
	public Flux<Object> apply(InParamsActor actor) {

		Flux<GroupedFlux<Object, Object>> fGf = actor.getInput();
		
		JSONObject solve = JSONObject.parseObject(actor.getSolve());
		
		Flux<Object> fo = fGf.flatMap(groupedFlux -> {
			
			String k = groupedFlux.key().toString();
			
			String fs = solve.get(k).toString();
			
			List<String> workerL = Arrays.asList(fs.split(","));
			
			InParamsActor inParamsActor = InParamsActor.builder().classnameList(workerL).input(groupedFlux).build();
			
			return new ConvFluxJustToMono().apply(new FuncFlow().apply(inParamsActor));
//			return new FuncFlow().apply(inParamsActor);
			 
		});
		
		return fo;
		
	}
	
}
