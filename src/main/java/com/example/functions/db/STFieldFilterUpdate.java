package com.example.functions.db;

import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Dao;
import org.nutz.trans.Molecule;
import org.nutz.trans.Trans;

import com.example.commons.InParamsDb;
import com.example.nutz.DBTools;

//import io.seata.spring.annotation.GlobalTransactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class STFieldFilterUpdate extends STCRUD {

	@Override
//	@GlobalTransactional
	public Flux<Object> apply(Flux<InParamsDb> us) {
		// TODO Auto-generated method stub
		Dao dao = DBTools.getDao();
		
		Mono<Object> atomResults = us.collectList().map(uList -> {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<Object> responses = (List<Object>) Trans.exec(new Molecule(){
				
			    public void run() {
			    	List<Object> responses = new ArrayList<Object>();
			    	uList.forEach(u -> {
			    		Object result = dao.update(u.getM(), u.getActived(), u.getLocked(), u.isIgnoreNull());
						responses.add(result);
			    	});
			    	
			    	setObj(responses);
			    }
			});
			
			return responses;
			
		});
		
		return atomResults.flux();
	}

}
