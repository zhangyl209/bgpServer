package com.example.functions.db;

import org.nutz.dao.Dao;
import org.nutz.lang.util.NutMap;

import com.example.commons.InParamsDb;
import com.example.nutz.DBTools;

//import io.seata.spring.annotation.GlobalTransactional;
import reactor.core.publisher.Flux;

public class STFetch extends STCRUD {

	@Override
	public Flux<Object> apply(Flux<InParamsDb> is) {
		// TODO Auto-generated method stub
//		Dao dao = new NutDao(dataSource);
		Dao dao = DBTools.getDao();
		
		return is.flatMap(i -> {
			 Object res = i.getFields() != null ? dao.fetch(i.getTable(), i.getCnd(), i.getFields()): dao.fetch(i.getTable(), i.getCnd());
			 
			 return res != null ? Flux.just(res) : Flux.empty();
		});			
	}
	
}
