package com.example.businesses;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.lang.util.NutMap;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.commons.InParamsActor;
import com.example.commons.InParamsDb;
import com.example.nutz.DBTools;
import com.google.common.base.CaseFormat;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CommonBusiness {
	
	private Dao dao = com.example.nutz.DBTools.getDao();
	
	@Bean
	public Function<String, String> convertHumpToLine() {
		
		return str -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str);
		
	}
	
	@Bean
	public Function<String, String> convertLineToHump() {
		
		return str -> CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
		
	}
	
	@Bean
	public Function<Flux<Record>, Flux<NutMap>> convertRecordToNutMap() {
		
		return flux -> flux.map(record -> {
			
			NutMap nutMap = new NutMap();
			record.keySet().forEach(key -> {
				nutMap.put(this.convertLineToHump().apply(key), record.get(key));
			});
			
			return nutMap;
			
		});
		
	}
	
	@Bean
	public Function<Flux<NutMap>, Flux<Record>> convertNutMapToRecord() {
		
		return flux -> flux.map( nutMap -> {
			
			Record record = new Record();
			nutMap.keySet().forEach(key -> {
				record.put(this.convertHumpToLine().apply(key), nutMap.get(key));
			});
			
			return record;
			
		});
		
	}
	
	/*
	 * 最简单的情况，直接转，然后执行，不带条件
	 * 带条件的通过FuncManager实现
	 * */
	@Bean
	public Function<Flux<Record>, Flux<InParamsDb>> convertRecordToInParamsDb() {
		
		return flux -> flux.map( record -> {			
			return InParamsDb.builder().m(record).build();
		} );
		
	}
	
	/*
	 * 增加create_time
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> addCreateTime() {
		return flux -> flux.map(record -> {
			
			record.put("create_time", new Date());
			
			return record;
		});
	}
	
	/*
	 * 增加create_id
	 * */
	@Bean
	public Function<InParamsActor, Flux<Record>> addCreateId() {
		
		return actor -> {
			String accountId = actor.getAuthUser();
			Flux<Record> fr = actor.getInput();
			
			if ( !StringUtils.hasText(accountId) ) { //缺少信息，失效，直接返回上一环节数据
				return fr;
			}
			
			return fr.map(record -> {
				
				record.put("create_id", accountId);
				
				return record;
			});
		};
	}
	
	/*
	 * 增加update_time
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> addUpdateTime() {
		return flux -> flux.map(record -> {
			
			record.put("update_time", new Date());
			
			return record;
		});
	}
	
	/*
	 * 增加update_id
	 * */
	@Bean
	public Function<InParamsActor, Flux<Record>> addUpdateId() {
		
		return actor -> {
			String accountId = actor.getAuthUser();
			Flux<Record> fr = actor.getInput();
			
			if ( !StringUtils.hasText(accountId) ) { //缺少信息，失效，直接返回上一环节数据
				return fr;
			}
			
			return fr.map(record -> {
				
				record.put("update_id", accountId);
				
				return record;
			});
		};
	}
	
	/*
	 * 增加account
	 * */
	@Bean
	public Function<InParamsActor, Flux<Record>> addAccount() {
		
		return actor -> {
			String accountId = actor.getAuthUser();
			Flux<Record> fr = actor.getInput();
			
			if ( !StringUtils.hasText(accountId) ) { //缺少信息，失效，直接返回上一环节数据
				return fr;
			}
			
			return fr.map(record -> {
				
				record.put("account", accountId);
				
				return record;
			});
		};
	}
	
	
	/*
	 * 求和
	 * */
	@Bean
	public Function<Flux<Number>, Flux<Number>> sumLong() {
		
		return flux -> flux.collectList().map(list -> {
				Number count = 0;
				for (int i = 0; i < list.size(); i++) {
					Number number = list.get(i);
					count = count.longValue() +  number.longValue();
				}
				return count;
			}).flux();
				
	}
	
	
	/*
	 * 获取基表
	 * */
	@Bean
	public Function<Flux<Record>, Flux<List<Record>>> getBaseList() {
		return flux -> flux.map(record -> {
			List<Record> list = dao.query("base_" + record.getString("base"), Cnd.where("status", "<>", 0).orderBy("sort", "asc"));
			
			return list;
		});
	}
	
	/*
	 * 新增基表
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> createBase() {
		return flux -> flux.map(record -> {
			
			String table = "base_" + record.getString("base");
			record.remove("base");
			try {
				dao.insert(table, Chain.from(record));
				record.put("result", 1);
			}catch(Exception e) {
				record.put("result", e.getMessage());
			}
			return record;
		});
	}
	
	/*
	 * 更新基表
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> updateBase() {
		return flux -> flux.map(record -> {
			String table = "base_" + record.getString("base");
			record.remove("base");
			try {
				int i = dao.update(table, Chain.from(record), Cnd.where("id", "=", record.getString("id")));
				record.put("result", i);
			}catch(Exception e) {
				record.put("result", e.getMessage());
			}
			return record;
		});
	}

	/*
	 * 获取最新记录
	 * */
	@Bean
	public Function<Flux<Record>, Flux<Record>> getLastestById() {
		return flux -> flux.map(record -> {
			Record r = dao.fetch(record.getString("table"), Cnd.where("status", "=", 1).orderBy("id", "desc"));
			
			return r;
		});
	}
}
