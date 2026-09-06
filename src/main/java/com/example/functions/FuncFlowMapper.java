package com.example.functions;

import java.util.function.Function;

import org.springframework.util.StringUtils;

import com.example.commons.InParamsActor;
import com.example.utils.SpringUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;

/*
 * 对于flux中一系列结果进行mapper
 * */
public class FuncFlowMapper implements Function<InParamsActor, Flux<GroupedFlux<Object, Object>>> {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Flux<GroupedFlux<Object, Object>> apply(InParamsActor actor) {
		// TODO Auto-generated method stub
		
//		Function f = SpringUtils.createFunctionByClassnameOrBean(actor.getClassname());
		
		Flux<Object> finput = actor.getInput();
		Flux<GroupedFlux<Object, Object>> fGf = null ;
		Function f = SpringUtils.createFunctionByClassnameOrBean(actor.getClassname());  //变成final
		
		if (finput != null) {
			fGf = finput.groupBy(v -> f.apply(v));
		}
		
		return fGf;
	}
	

	

}
