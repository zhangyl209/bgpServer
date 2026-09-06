package com.example.functions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.springframework.util.StringUtils;

import com.example.commons.InParamsActor;
import com.example.nutz.DBTools;
import com.google.common.primitives.Ints;

import reactor.core.publisher.Flux;

/*
 * 1. excute: 从config_input获取输入，从数据库获取business配置并执行
 * 2. excutewith: 调用接口传入数据，从数据库获取business配置并执行
 * */
public class BusinessManager implements Function<Map<String,Object>, Flux<Object>> {

	@Override
	public Flux<Object> apply(Map<String,Object> map) {
		
		Dao dao = DBTools.getDao();
		
		Flux<Object> output = null;
		InParamsActor inParamsActor = null;
		
		if (map.get("business") == null) {
			return Flux.just("缺少参数business");
		}
		String business = map.get("business").toString();
		
		Record config_business = dao.fetch("config_business", Cnd.where("business", "=", business).and("status", "=", 1));
		
		if (config_business == null) {
			return Flux.just("未找到config_business, business：" + business);
		}
		
		String inputs = config_business.getString("inputs");
		String funcflow = config_business.getString("funcflow");
		
		switch (map.get(".type").toString()) {
		case "excute":
			
			if (!StringUtils.hasText(inputs) || !StringUtils.hasText(funcflow)) {
				return Flux.just("config_business配置不完整, business：" + business);
			}
			
			List<Integer> inputL = Arrays.asList(inputs.split(",")).stream().map(Ints::tryParse).toList();
			
			inParamsActor = new InputCreater().apply(inputL, Integer.parseInt(funcflow));
			
			output = new FuncManager().apply(inParamsActor);
			
			break;

		case "excutewith":
			
			if ( !StringUtils.hasText(funcflow) ) {
				return Flux.just("config_business配置不完整, business：" + business);
			}
			
			inParamsActor = InParamsActor.builder().classname(funcflow.toString()).input(Flux.just(map.get("data"))).authUser(map.get(".auth") != null ? map.get(".auth").toString() : null).build();
			
			output = new FuncManager().apply(inParamsActor);
			
			break;

		default:
			
			break;
		}
		
		return output;
	}

}
