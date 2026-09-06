package com.example.commons;

import java.util.Map;

import org.nutz.dao.Condition;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class InParamsDb {
	
	private String table;
	
	private Condition cnd;
	//用于更新，弃用，直接使用map更新
//	private Chain chain;
	
	//插入
	private Record m;
	
	private int pageNumber;
	
	private int pageSize;
	//fetch query过滤字段
	private String fields;
	//白名单
	private String actived;
	//黑名单
	private String locked;
	//忽略空值
	private boolean ignoreNull;
	//数据库函数
	private boolean funcName;
	//列名
	private boolean colname;
		
	private Sql sql;
}
