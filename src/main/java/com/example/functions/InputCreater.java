package com.example.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.OrderBy;
import org.nutz.dao.util.cri.Static;
import org.springframework.util.StringUtils;

import com.example.commons.InParamsActor;
import com.example.commons.InParamsDb;
import com.example.nutz.DBTools;
import com.example.utils.SpringUtils;

import reactor.core.publisher.Flux;

/*
 * 从config_input生成business的输入
 * */
public class InputCreater implements BiFunction<List<Integer>, Integer, InParamsActor> {

	@Override
	public InParamsActor apply(List<Integer> inputL, Integer funcflow) {
		
		Dao dao = DBTools.getDao();
		
		List<InParamsDb> inParamsDbL = new ArrayList<InParamsDb>();
		
		for (int i = 0 ; i < inputL.size() ; i++) {
			
			int input = inputL.get(i);
			
			Record config_input = dao.fetch("config_input", Cnd.where("id", "=", input));
			
			if (config_input == null) {
				return null;
			}
			
			String table = config_input.getString("table");
			if (!StringUtils.hasText(table)) {
				return null;
			}
			
			Cnd cnd = Cnd.NEW();
			Condition c = null;
	        
			String wrap = config_input.getString("cndwrap");
			if (StringUtils.hasText(wrap)) {
				cnd = cnd.and(new Static(wrap));
			}
			c = cnd;
			
			String sort = config_input.getString("sort");
			if (StringUtils.hasText(sort)) {
            	c = SpringUtils.addOrderByToCnd(sort, cnd);
            }
			
			String fields = config_input.getString("fields");
			
			InParamsDb inParamsDb = InParamsDb.builder()
					.table(table)
					.cnd(c)
					.fields(fields)
//					.pageNumber(config_input.getInt("pagenumber"))
//					.pageSize(config_input.getInt("pagesize"))
					.build();
			
			inParamsDbL.add(inParamsDb);
			
		}
	
		Flux<InParamsDb> finput = Flux.fromIterable(inParamsDbL);
		
		InParamsActor inParamsActor = InParamsActor.builder().classname(funcflow.toString()).input(finput).build();
		
		return inParamsActor;
	}

}
