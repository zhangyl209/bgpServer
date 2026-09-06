package com.example.functions.db;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SimpleExpression;
import org.nutz.dao.util.cri.Static;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.commons.InParamsDb;
import com.example.nutz.DBTools;
import com.example.utils.RegexMatchUtils;
import com.example.utils.SpringUtils;

import reactor.core.publisher.Flux;

public class ConditionMaker implements Function<Flux<Map<String, Object>>, Flux<InParamsDb>>{

	@Override
	public Flux<InParamsDb> apply(Flux<Map<String, Object>> maps) {
		// TODO Auto-generated method stub
		Dao dao = DBTools.getDao();
		
		return maps.map(m -> {
			InParamsDb inParamsDb = m.get(".table") != null ? InParamsDb.builder().table(m.get(".table").toString().trim()).build() : InParamsDb.builder().build();	
			String type = m.get(".type").toString();
			
			Condition c = null;
            Cnd cnd = null;
//            Chain chain = null;
            Record dataMap = null;
            String fields = null;
            String actived = null;
            Sql sql = null;
            
            //从config_input获取其他配置
            String config_cndwrap = null;
            String config_sort = null;
            String config_cndwrapignore = null;
            if (m.get(".input") != null) {
            	Record config_input = dao.fetch("config_input", Cnd.where("id", "=", m.get(".input")));
            	
            	if (config_input != null) {
            		
            		if ( StringUtils.hasText( config_input.getString("cndwrap") ) ) {
            			config_cndwrap = config_input.getString("cndwrap");
            		}
            		if ( StringUtils.hasText( config_input.getString("sort") ) ) {
            			config_sort = config_input.getString("sort");
            		}
            		if ( StringUtils.hasText( config_input.getString("cndwrapignore") ) ) {
            			config_cndwrapignore = config_input.getString("cndwrapignore");
            		}
            	}
            }
            
			switch(type) {
			case "getOne":
				c = Cnd.where("id", "=", m.get("id").toString());
				fields = m.get("fields") != null ? m.get("fields").toString() : null;
				break;
			case "getList":
				//filter
	            if (m.get("filter") != null){
	            	JSONObject filterMap = JSON.parseObject((String) m.get("filter"));
	                for(Object key : filterMap.keySet()){
	                    // =
	                    String field = key.toString();
	                    String op = "=";
	                    Object value = filterMap.get(key);
	                    
	                    if (value == null && !key.toString().contains("_")) {
	                    	continue;
	                    }
	                    
	                    //> >= < <=
	                    if (key.toString().contains("_") && !key.toString().endsWith("_id") && !key.toString().endsWith("_time")){
	                        field = field.split("_")[0];
	                        if (key.toString().endsWith("_q")) { //q
	                            op = "LIKE";
	                            value = "%" + value + "%";
	                        } else if (key.toString().endsWith("_gte")){
	                            op = ">=";
	                        } else if (key.toString().endsWith("_lte")) { //lte
	                            op = "<=";
	                        } else if (key.toString().endsWith("_in")) {
	                        	op = "IN";
//	                            value = "(" + value + ")";
	                        } else if (key.toString().endsWith("_s")) { //search用于多字段or检索，配合config_input使用，配置cndwrap
	                        	op = "SEARCH";
	                        } else if (key.toString().endsWith("_not")) {
	                        	op = "!=";
	                        } else if (key.toString().endsWith("_is")) {
	                        	op = "is";
	                        } else if (key.toString().endsWith("_isnot")) {
	                        	op = "is not";
	                        }
	                    }
	                    if (op.equals("SEARCH")) {
	                    	
	                    } else if (!op.equals("IN")  ) {
	                    	SimpleExpression se = new SimpleExpression(field, op, value);
	                    	if (cnd == null) {
		                        cnd = Cnd.where(se);
		                    } else {
		                        cnd = cnd.and(se);
		                    }
	                    	
	                    } else {
	                    	if (cnd == null) {
	                    		cnd = Cnd.where(field, "in", value.toString().trim());
		                    } else {
		                    	cnd = cnd.and(field, "in", value.toString().trim());
		                    }
	                    	
	                    }    
	                }
	                c = cnd;
	            }
	            //追加config_input内的配置
                if (StringUtils.hasText(config_cndwrap)) {
                	
                	List<String> placeHolders = RegexMatchUtils.getPlaceholder(config_cndwrap, "$");
                	
                	JSONObject filterMap = m.get("filter") != null ? JSON.parseObject((String) m.get("filter")) : new JSONObject();
					
					//判断如果cnd中需要的值没有，替换一下1=1,需要配置cndwrapignore
					if (config_cndwrapignore != null) {
						for (int i = 0 ; i<placeHolders.size(); i++) {
							String ph = placeHolders.get(i);
							if (ph.equals("auth")) {
								continue;
							} else {
								if (!StringUtils.hasText(filterMap.getString(ph+"_s"))) {
									String phxxxph = RegexMatchUtils.getConditionWith(config_cndwrap, "$", ph);
									if (phxxxph != null) {
										if (phxxxph.trim().endsWith(")")) {
											phxxxph = phxxxph.substring(0, phxxxph.lastIndexOf(")"));
										}
										config_cndwrap = config_cndwrap.replace(phxxxph, "1=1 ");
									}
								}	
							}
						}
					}
                	
                	//如果条件是带变参的，$
                	Sql mySql = Sqls.create(config_cndwrap);
					
                	for (int i = 0 ; i<placeHolders.size(); i++) {
						String ph = placeHolders.get(i);
						if (ph.equals("auth")) {
							mySql.vars().set(ph, m.get(".auth"));
						} else {
							mySql.vars().set(ph, filterMap.get(ph+"_s"));//暂时只支持string型
						}
					}
					
					//这里需要注意，如果filter什么也不传，就是获取所有的
					String cndWrap = mySql.toString();
                	
                	if (cnd == null) {
                		cnd = Cnd.where(new Static(cndWrap));
                    } else {
                    	cnd = cnd.and(new Static(cndWrap));
                    }
                	c = cnd;
                }
	            //sort
	            if (m.get("sort") != null && m.get("order") != null){
	                String sort = m.get("sort").toString().trim().toLowerCase();
	                String order = m.get("order").toString().trim().toLowerCase();

	                if (cnd == null){
                        cnd = Cnd.NEW();
                    }
                    if (order.equals("asc") || order.equals("ascend")) {
                    	c = cnd.asc(sort);
                    } else { //desc
                        c = cnd.desc(sort);
                    }
	            }
	            //追加config_input内的配置
                if (StringUtils.hasText(config_sort)) {
                	c = SpringUtils.addOrderByToCnd(config_sort, cnd);
                }
	            //分页
				int pageNum = m.get("pageNum") != null ? Integer.parseInt(m.get("pageNum").toString()) : 0;
				int pageSize = m.get("pageSize") != null ? Integer.parseInt(m.get("pageSize").toString()) : 0;
				inParamsDb.setPageNumber(pageNum);
				inParamsDb.setPageSize(pageSize);
				fields = m.get("fields") != null ? m.get("fields").toString() : null;
				break;
			case "create":
				if (m.get("data") != null ) {
					dataMap = (Record) m.get("data");
					dataMap.put(".table", m.get(".table").toString().trim());
				}
				actived = m.get("actived") != null ? m.get("actived").toString() : null;
				break;
			case "delete":
				c = Cnd.where("id", "in", m.get("ids").toString().trim());
				break;
			case "update":
				c = Cnd.where("id", "in", m.get("ids").toString().trim());
				dataMap = (Record) m.get("data");
				dataMap.put(".table", m.get(".table").toString().trim());
//				chain = Chain.from(dataMap);
				break;
			case "sql":
				String s = m.get("sql") != null ? m.get("sql").toString() : null;
				if (s != null) {
					sql = Sqls.create(s);
					if (m.get("params") != null) {
						Map<String, Object> params = JSON.parseObject((String) m.get("params"));
						for(String key : params.keySet()){
							sql.vars().set(key, params.get(key));
						}
					}
					
				}
				
				break;
			default: 
					
			}
				
			inParamsDb.setCnd(c);
			inParamsDb.setM(dataMap);
//			inParamsDb.setChain(chain);
			inParamsDb.setFields(fields);
			inParamsDb.setActived(actived);
			inParamsDb.setSql(sql);
			return inParamsDb;
		});
	}


	
}
