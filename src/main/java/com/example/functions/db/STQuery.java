package com.example.functions.db;

import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;

import com.example.commons.InParamsDb;
import com.example.nutz.DBTools;

import reactor.core.publisher.Flux;

public class STQuery extends STCRUD {
	
	@Override
	public Flux<Object> apply(Flux<InParamsDb> qs) {
		// TODO Auto-generated method stub
		Dao dao = DBTools.getDao();
		
//		System.out.println("STQuery0:" + new Date());
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("STQuery1:" + new Date());
		
		return qs.map(q -> {
				if (q.getPageNumber() > 0 && q.getPageSize() > 0) {
					Pager pager = dao.createPager(q.getPageNumber(), q.getPageSize());
//					return dao.query(q.getTable(), q.getC(), pager);
					pager.setRecordCount(dao.count(q.getTable(), q.getCnd()));
				    return new QueryResult(q.getFields() != null ? dao.query(q.getTable(), q.getCnd(), pager, q.getFields()) : dao.query(q.getTable(), q.getCnd(), pager), pager);
				}
				return q.getFields() != null ?  dao.query(q.getTable(), q.getCnd(), null, q.getFields()) : dao.query(q.getTable(), q.getCnd());
			});
	}

}
