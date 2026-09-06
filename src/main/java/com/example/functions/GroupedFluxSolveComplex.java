package com.example.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.alibaba.fastjson.JSONObject;
import com.example.commons.InParamsActor;
import com.example.utils.RegexMatchUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;

/*
 * 对于Flux<GroupedFlux<Object, Object>>进行处理
 * 区别于GroupedFluxSolve，可以继续用复杂的函数（config_funcflow中reducer为{key:id,...}）处理
 * 
 * --key后面的值--
 * 目前支持：    1. 一个id   2. 一个或多个函数名
 * 目前禁止：    1. 多个id
 * 目前不支持：  1. 混合id和函数名
 * */
public class GroupedFluxSolveComplex implements Function<InParamsActor, Flux<Object>> {

	@Override
	public Flux<Object> apply(InParamsActor actor) {

		Flux<GroupedFlux<Object, Object>> fGf = actor.getInput();
		
		JSONObject solve = JSONObject.parseObject(actor.getSolve());
		
		Flux<Object> fo = fGf.flatMap(groupedFlux -> {
			
			String k = groupedFlux.key().toString();
			
			if (solve.get(k) == null) {
				return groupedFlux;
			}
			
			String fs = solve.get(k).toString();
			
			List<String> workerL = Arrays.asList(fs.split(","));
			
			InParamsActor inParamsActor = InParamsActor.builder().input(groupedFlux).build();
			
			if (workerL == null || workerL.size() < 1) {
				
				return Flux.just("确少对于groupedFlux：" + k + " 的执行业务逻辑配置");
				
			} else if (workerL.size() == 1) {
				
				if (RegexMatchUtils.isPureNumber(workerL.get(0))) {  //business id
					
					inParamsActor.setClassname(workerL.get(0));
					//FuncManager返回结果已经flat了，这里就不用再变成mono了
					return new FuncManager().apply(inParamsActor);
					
				} else {											 //func name
					
					inParamsActor.setClassnameList(workerL);
					
				}
				
			} else {
				
				if (RegexMatchUtils.hasPureNumber(fs)) {
					
					return Flux.just("不支持在solve的一个key中配置多个业务或者业务与函数组合，可以使用包含多个子业务或函数的一个业务");
					
				}
				
				inParamsActor.setClassnameList(workerL);
				
			}
			
//			InParamsActor inParamsActor = InParamsActor.builder().classname(workerL.get(0)).input(groupedFlux).build();
		    
			return new ConvFluxJustToMono().apply(new FuncFlow().apply(inParamsActor));
			
		});
		
		return fo;
		
	}
	
}
