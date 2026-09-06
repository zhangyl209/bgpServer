package com.example.functions.db;

import java.util.Map;
import java.util.function.Function;

import com.example.commons.InParamsDb;

import reactor.core.publisher.Flux;

public class FieldFilterMaker implements Function<Flux<Map<String, Object>>, Flux<Map<String, Object>>> {

	@Override
	public Flux<Map<String, Object>> apply(Flux<Map<String, Object>> maps) {
		// TODO Auto-generated method stub
		return maps.map(m -> {
//			String table = m.get(".table").toString();
//			String type = m.get(".type").toString();
			//判断表权限
			//获取字段权限
			//设置字段
//			m.put("fields", "id,name");//id,name
//			m.put("actived", "^(id|name)$");//^(id|name)$
			return m;
		});
	}

}
