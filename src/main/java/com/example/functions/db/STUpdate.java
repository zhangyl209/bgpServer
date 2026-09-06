package com.example.functions.db;

import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.trans.Molecule;
import org.nutz.trans.Trans;
import org.springframework.util.StringUtils;

import com.example.commons.InParamsDb;
import com.example.nutz.DBTools;

//import io.seata.spring.annotation.GlobalTransactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class STUpdate extends STCRUD {

	@Override
//	@GlobalTransactional
	public Flux<Object> apply(Flux<InParamsDb> us) {
		// TODO Auto-generated method stub
		Dao dao = DBTools.getDao();
		
//		return us.map(u -> dao.update(u.getTable(), u.getChain(), u.getCnd()));
//		return us.map(u -> dao.update(u.getM(), u.getCnd()));
		
		Mono<Object> atomResults = us.collectList().map(uList -> {
//			System.out.println("STInsert0:" + new Date());
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			@SuppressWarnings("unchecked")
			List<Object> responses = (List<Object>) Trans.exec(new Molecule(){
				
			    public void run() {
			    	List<Object> responses = new ArrayList<Object>();
			    	uList.forEach(u -> {
			    		
			    		//InParamsDb中table与getM中的.table优先级设置，以.table为准
			    		Record m = u.getM();
			    		if ( !StringUtils.hasText(m.getString(".table")) && StringUtils.hasText(u.getTable()) ) {
			    			m.put(".table", u.getTable());
			    		}
			    		
			    		Object result = dao.update(m, u.getCnd());
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
