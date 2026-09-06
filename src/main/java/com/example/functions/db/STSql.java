package com.example.functions.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;

import com.alibaba.fastjson.JSONArray;
import com.example.commons.InParamsDb;
import com.example.nutz.DBTools;
import com.example.utils.Utils;

import reactor.core.publisher.Flux;

public class STSql extends STCRUD {
	
	@Override
	public Flux<Object> apply(Flux<InParamsDb> ss) {
		// TODO Auto-generated method stub
//		Dao dao = new NutDao(dataSource);
		Dao dao = DBTools.getDao();
		
		return ss.map(s -> {
			if (s.getSql() != null) {
				Sql sql = s.getSql();
				sql.setCallback(new SqlCallback() {
			        public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
			        	return Utils.resultSetToJsonArry(rs);
			        }
			    });
				dao.execute(sql);
				
				System.out.println(sql);
				
//				return sql.getList(Record.class);
			    return sql.getObject(JSONArray.class);
			}
			return null;
		});			
	}
	
}
