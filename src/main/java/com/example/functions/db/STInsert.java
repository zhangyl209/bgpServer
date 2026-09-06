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

public class STInsert extends STCRUD {

	@Override
//	@GlobalTransactional
//	@Transactional
	public Flux<Object> apply(Flux<InParamsDb> is) {
		// TODO Auto-generated method stub
		Dao dao = DBTools.getDao();
		

//		System.out.println("STInsert1:" + new Date());
		
//		//map是同步的，所以flux传过来以后同步执行
//		return is.map(i -> {
//			return dao.insert(i.getM(), i.getActived());
//			
//			});
		
		
		Mono<Object> atomResults = is.collectList().map(iList -> {
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
			    	iList.forEach(i -> {
			    		
			    		//InParamsDb中table与getM中的.table优先级设置，以.table为准
			    		Record m = i.getM();
			    		if ( !StringUtils.hasText(m.getString(".table")) && StringUtils.hasText(i.getTable()) ) {
			    			m.put(".table", i.getTable());
			    		}
			    		
			    		Object result = i.getActived() != null ? dao.insert(m, i.getActived()) : dao.insert(m);
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
