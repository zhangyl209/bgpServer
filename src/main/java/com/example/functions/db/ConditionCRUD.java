package com.example.functions.db;

import java.util.Map;
import java.util.function.Function;

import reactor.core.publisher.Flux;

public class ConditionCRUD<T extends STCRUD> implements Function<Flux<Map<String, Object>>, Flux<Object>> {

	public Class <T> type;
	
	public ConditionCRUD() {
	}
	
	public ConditionCRUD(Class<T> type) {
        this.type = type;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Flux<Object> apply(Flux<Map<String, Object>> t) {
		// TODO Auto-generated method stub
		
		STFactory factory = new STFactory<STCRUD>();
		
		STCRUD stcrud = factory.getInstance(type);
		
		return new FieldFilterMaker().andThen(new ConditionMaker()).andThen(stcrud).apply(t);
	}

}
