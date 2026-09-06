package com.example.functions.db;

import org.nutz.dao.Dao;

import com.example.commons.InParamsDb;
import com.example.nutz.DBTools;

//import io.seata.spring.annotation.GlobalTransactional;
import reactor.core.publisher.Flux;

public class STClear extends STCRUD {

	@Override
//	@GlobalTransactional
	public Flux<Object> apply(Flux<InParamsDb> ds) {
		// TODO Auto-generated method stub
		
		Dao dao = DBTools.getDao();
		
		return ds.map(d -> dao.clear(d.getTable(), d.getCnd()));
	}

}
