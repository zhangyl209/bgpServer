package com.example.utils;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.commons.InParamsDb;
import com.example.functions.db.STSql;
import com.example.functions.db.normal.STSqlNormal;

import reactor.core.publisher.Flux;

@Component
public class SpringUtils implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		SpringUtils.applicationContext = applicationContext;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName){
		if(applicationContext.containsBean(beanName)){
			return (T)applicationContext.getBean(beanName);
		} else{ 
			return null;
		}
	}

	public static <T> Map<String, T> getBeans0fType(Class<T> baseType){
		return applicationContext.getBeansOfType(baseType);
	}
	
	/**
	 * 根据类名或者bean，生产function
	 * @param key
	 * @return
	 */
	public static Function<?, ?> createFunctionByClassnameOrBean(String worker) {
		
		Function<?, ?> f = null;
		
		if (StringUtils.hasText(worker)) {
			
			try {
				Class<?> workerClass = Class.forName(worker);
				Object o = workerClass.getDeclaredConstructor().newInstance();
				
				f = (Function<?, ?>) o;
			
			} catch (ClassNotFoundException e) { //如果没有找到，再从bean里找
				
				f = SpringUtils.getBean(worker);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();	
			}
		}
		
		return f;
		
	}
	
	/**
	 * 根据类名或者bean的list，生产function流
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Function<?, ?> createFuncFlowByListOfClassnameOrBean(List<String> workerL) {
		
		Function<?, ?> f = null;
		
		if (workerL != null) {
			
			for (int i = 0; i < workerL.size(); i++) {
				String worker = workerL.get(i);
				try {
					Class<?> workerClass = Class.forName(worker);
					Object o = workerClass.getDeclaredConstructor().newInstance();
					
					if (f == null) {
						f = (Function) o;
					} else {
						f = f.andThen(((Function) o));
					}
				
				} catch (ClassNotFoundException e) { //如果没有找到，再从bean里找
					Function fb = SpringUtils.getBean(worker);
					
					if (f == null) {
						f = fb;
					} else {
						f = f.andThen((fb));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();	
				}
			}
		}
		
		return f;
		
	}
	
	/**
	 * 将config_input中sort字段（json字符串）追加到cnd中
	 */
	public static Condition addOrderByToCnd(String config_sort, Cnd cnd) {
		
		Condition c = null;
		
		JSONObject jo = JSONObject.parseObject(config_sort);
    	if (cnd == null){
            cnd = Cnd.NEW();
        }
    	
    	for (String key : jo.keySet()) {
    		List<String> sortfields = Arrays.asList(jo.get(key).toString().split(",")).stream().toList();
    		if (key.equals("asc")) {
    			for (String sort:sortfields) {
        			c = cnd.asc(sort);
        		}
    		} else {
    			for (String sort:sortfields) {
        			c = cnd.desc(sort);
        		}
    		}
    	}
    	
		return c;
	}
	
	/**
	 * 将config_input中sort字段（json字符串）追加到cnd中
	 */
	public static Condition addOrderByToSql(String config_sort, String sql) {
		
		JSONObject jo = JSONObject.parseObject(config_sort);
    	if (!StringUtils.hasText(sql)){
            sql = "";
        }
    	sql = sql + " ORDER BY ";
    	
    	for (String key : jo.keySet()) {
    		List<String> sortfields = Arrays.asList(jo.get(key).toString().split(",")).stream().toList();
    		for (String sort:sortfields) {
    			sql = sql + sort + " " + key + ",";
    		}
    	}
    	
    	sql = sql.substring(0, sql.length() -1 );
    	
		return Cnd.wrap(sql);
	}
	
	/**
	 * JSONObject to Record
	 */
	public static Record JSONObject2Record(JSONObject jsonobject) {
		
		Record record = new Record();
        
        for (Map.Entry<String, Object> entry: jsonobject.entrySet()) {
        	record.put(entry.getKey(), entry.getValue());
        }
        
        return record;
	}
	
	/**
	 * 关联查询
	 */
	public static List<Record> selectFromRelations(String field_target, String relation_table, String relation_field, String cnd) {
		
		Sql sql = Sqls.create("SELECT * FROM $relation_field WHERE id IN (SELECT $relation_field FROM $relation_table $cnd)");
        sql.vars().set("field_target", field_target);
        sql.vars().set("relation_table", relation_table);
        sql.vars().set("cnd", cnd);
        sql.vars().set("relation_field", relation_field);
        InParamsDb inParamsDb = InParamsDb.builder().sql(sql).build();
        
         Object jsonArray = new STSqlNormal().apply(inParamsDb);
         List<Record> result = ((JSONArray)jsonArray).stream().filter(Objects::nonNull).map(json -> JSONObject2Record((JSONObject)json)).collect(toList());
         
        return result;
	}
	
	/**
	 * 查询自增id
	 */
	public static int getAutoIncrementID(String databaseName, String tableName) {
		
		Sql sql = Sqls.create("SELECT auto_increment FROM information_schema.`TABLES` WHERE TABLE_SCHEMA='$databaseName' AND TABLE_NAME='$tableName'");
        sql.vars().set("databaseName", databaseName);
        sql.vars().set("tableName", tableName);
        InParamsDb inParamsDb = InParamsDb.builder().sql(sql).build();
        //auto_increment
        Object jsonArray = new STSqlNormal().apply(inParamsDb);
        List<Integer> result = ((JSONArray)jsonArray).stream().filter(Objects::nonNull).map(json -> ((JSONObject)json).getInteger("auto_increment")).collect(toList());
        
        if (result !=null && result.size() > 0) {
        	return result.get(0);
        }
        
        return -1;
        
	}
	
	/**
	 * 查询表的字段
	 */
	public static List<String> getTableColumns(String databaseName, String tableName) {
		
		Sql sql = Sqls.create("SELECT column_name FROM information_schema.`COLUMNS` WHERE TABLE_SCHEMA='$databaseName' AND TABLE_NAME='$tableName'");
        sql.vars().set("databaseName", databaseName);
        sql.vars().set("tableName", tableName);
        InParamsDb inParamsDb = InParamsDb.builder().sql(sql).build();
        
        Object jsonArray = new STSqlNormal().apply(inParamsDb);
        List<String> result = ((JSONArray)jsonArray).stream().filter(Objects::nonNull).map(json -> ((JSONObject)json).getString("column_name")).collect(toList());
        
        if (result !=null && result.size() > 0) {
        	return result;
        }
        
        return null;
        
	}
	
	public static void main(String[] args) {
		System.out.println(getTableColumns("test", "account"));
	}
}
