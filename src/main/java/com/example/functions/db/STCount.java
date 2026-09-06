package com.example.functions.db;

import org.nutz.dao.Dao;

import com.example.commons.InParamsDb;
import com.example.nutz.DBTools;

//import io.seata.spring.annotation.GlobalTransactional;
import reactor.core.publisher.Flux;

public class STCount extends STCRUD {

	@Override
//	@GlobalTransactional
	public Flux<Object> apply(Flux<InParamsDb> cs) {
		// TODO Auto-generated method stub
		
		Dao dao = DBTools.getDao();
		
		return cs.map(c -> dao.count(c.getTable(), c.getCnd()));
	}

}
