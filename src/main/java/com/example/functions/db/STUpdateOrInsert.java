package com.example.functions.db;

import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.trans.Molecule;
import org.nutz.trans.Trans;
import org.springframework.util.StringUtils;

import com.example.commons.InParamsDb;
import com.example.nutz.DBTools;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class STUpdateOrInsert extends STCRUD {

	@Override
	public Flux<Object> apply(Flux<InParamsDb> us) {
		// TODO Auto-generated method stub
		Dao dao = DBTools.getDao();
		

		Mono<Object> atomResults = us.collectList().map(uList -> {

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
			    		
			    		if ((Integer)result == 0) {
			    			result = dao.insert(m);
			    		}
						responses.add(result);
			    	});
			    	
			    	setObj(responses);
			    }
			});
			
			return responses;
			
		});
		
		return atomResults.flux();
	}
	
	public static void main(String[] args) {
		Condition c = Cnd.where("orgname","=","updateOrInsert2");
		Record r = new Record();
		r.put("orgname", "updateOrInsert2");
		InParamsDb i = InParamsDb.builder().table("orgs").m(r).cnd(c).build();
		
		Flux<InParamsDb> input = Flux.just(i);
		
		Flux<Object> output = new STUpdateOrInsert().apply(input);
		
		System.out.println(output.collectList().block().get(0));
	}

}
