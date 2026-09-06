package com.example;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

//public class Mytest {
//	public static void main(String[] args) {
//
////		String data = "{\"trc_base_explain\":[{\"id\": \"1\", \"name\" : \"name1\"},{\"id\": \"2\", \"name\" : \"name2\"}]}";
////        JSONObject jsonData = JSONObject.parseObject(data);
////		
////        for (Map.Entry<String, Object> entry : jsonData.entrySet()) {
////            entry.getKey();
////            List<JSONObject> records = (List<JSONObject>)entry.getValue();
////            
////            for (int i = 0; i<records.size(); i++) {
////            	JSONObject r = records.get(i);
////            	System.out.println(r);
////            }
////        }
//		Object b = null;
//		String test = String.valueOf(b);
//		String test1 =test;
//	}
//}
//
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.alibaba.fastjson.JSONObject;
import com.example.businesses.CommonBusiness;
import com.example.commons.InParamsActor;
import com.example.commons.InParamsDb;
import com.example.commons.ResponseInfo;
import com.example.functions.ConvFluxJustToMono;
import com.example.functions.SerialActor;
import com.example.functions.SerialRunner;
import com.example.functions.db.ConditionCRUD;
import com.example.functions.db.STCRUD;
import com.example.functions.db.STFetch;
import com.example.functions.db.STQuery;
import com.example.nutz.DBTools;
import com.google.common.base.CaseFormat;

import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class Mytest {
	

	@Autowired
	static
	DBTools DBTools;
	
	public static void main(String[] args) {
        try {
            InetAddress address = InetAddress.getByName("192.168.1.1");
            byte[] bytes = address.getAddress();
 
            for (int i = 0; i < 5; i++) { // 递增5次
                bytes[2]++; // 改变最低位
                address = InetAddress.getByAddress(bytes);
                System.out.println("Incremented IP: " + address.getHostAddress());
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
	
	//测试
//	public static void main(String[] args){    
//	    try {
//	            //测试.class
//	            Class testTypeClass=STQuery.class;
//	            System.out.println("testTypeClass---"+testTypeClass);
//	            //测试Class.forName()（***代表包路径）
//	            Class testTypeForName=Class.forName("com.example.functions.db.STQuery");
//	            System.out.println("testTypeForName---"+testTypeForName);
//	            //测试Object.getClass()
//	            STQuery testTypeGetClass= new STQuery();
//	            System.out.println("testTypeGetClass---"+testTypeGetClass.getClass());
//	       
//	            
////	            Class[] parameterTypes={String.class}; 
////	            java.lang.reflect.Constructor constructor=testTypeClass.get
//	            
//	            
////	          //根据类名获取Class对象
////	            Class c=Class.forName("java.lang.Integer");  
////	          //参数类型数组
////	            Class[] parameterTypes={String.class}; 
////	          //根据参数类型获取相应的构造函数
////	            java.lang.reflect.Constructor constructor=c.getConstructor(parameterTypes);
////	          //参数数组
////	            Object[] parameters={"1"};
////	          //根据获取的构造函数和参数，创建实例
////	            Object o=constructor.newInstance(parameters);
//	            
//	            try {
//					
//					
//					Condition c1 = Cnd.where("age",">",20);
//					InParamsDb i1 = InParamsDb.builder().table("users").cnd(c1).build();
//					
//					Flux<InParamsDb> input = Flux.just(i1);
//					
//					Object o  = testTypeClass.newInstance();
//					
//					
//					
////					Flux<Object> output =((STCRUD)o).apply(input);
//					Flux<Object> output =(Flux<Object>) ((Function)o).apply(input);
//					output.log().subscribe();
//					List<Object> results = output.collectList().block();
//					
//					System.out.println(results.size());
//					System.out.println(results.get(0));
////					System.out.println(results.get(1));
//				} catch (InstantiationException | IllegalAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//	    } catch (ClassNotFoundException e) {
//	        // TODO Auto-generated catch block
//	            e.printStackTrace();
//	    }
//	}
	
	//直接用inParamsDb,可接受多个
//	public static void main(String[] args) throws InterruptedException {
//		
//		Dao dao = com.example.nutz.DBTools.getDao();
//		
//		Object r =dao.func2("account", "JSON_OBJECT", "'username', username");
//		
////		Record r = new Record();
////		
////		r.put(".table", "orgs");
////		r.put("id=", 32);
////		r.put("orgname", "rrr");
////		
////		int i = dao.update(r);
//		
////		select JSON_OBJECT('id',id, 'username',username, 'password',password,'remarks',remarks) from account
////
////		SELECT JSON_OBJECT('id', menu.id, 'name', menu.name, 'meta', JSON_OBJECT('title',  meta.title)) FROM menu inner join meta ON menu.meta=meta.id
//		
//		System.out.println(r);
//		
////		// TODO Auto-generated method stub
////		Condition c1 = Cnd.where("id","=",2);
////		InParamsDb i1 = InParamsDb.builder().table("account").cnd(c1).build();
////		
////		Condition c2 = Cnd.where("id","=",1);
////		InParamsDb i2 = InParamsDb.builder().table("account").cnd(c2).build();
////		
////		Flux<InParamsDb> input = Flux.just(i1, i2);
////		
//////		Flux<Object> output = new SingleGetOne().apply(input);
////		Flux<Object> output = new STFetch().apply(input);
////		
////		List<Object> results = output.collectList().log().block();
////		
//////		Object obj = results.get(0);
////		
////		System.out.println(results.size());
////		System.out.println(results);
//		
////		Flux<Object> stringFlux1 = Flux.just("a","b","c","d","e","f","g","h","i");
////		Mono<Object> m = stringFlux1.take(1).single().map(lr ->lr);
////		System.out.println(m.block());
////		
////        Flux<Flux<String>> stringFlux2 = stringFlux1.window(2);
////        stringFlux2.concatMap(flux1 ->flux1.map(word ->word.toUpperCase())
////                .delayElements(Duration.ofMillis(200)))
////                .subscribe(x -> System.out.print("->"+x));
////        Thread.sleep(2000);
//		
//		
//		
////		Flux<Integer> f1 = Flux.just(1,2,3,4,5,6,7,8,9);
////		
////		Flux<GroupedFlux<Object, Integer>> f2 = f1.groupBy(v -> {
////			if (v < 4) {
////				return "a";
////			} else if (v < 6) {
////				return "b";
////			} else if (v < 8) {
////				return "c";
////			}
////			return "d";
////		}); 
////		f2.subscribe(groupedFlux -> {
////			System.out.println(groupedFlux.key());
////			groupedFlux.subscribe(x -> System.out.println(x));
////			
////		});
//		
////		HashMap<String, String> map = new HashMap<String, String>();
////		map.put("a", "1");
////		map.put("b", "2");
////		map.put("c", "3");
////		
////		System.out.println(map.toString());
//		
////		String string = "{\"a\":1, \"b\":2, \"c\":3}";
////		JSONObject jo = JSONObject.parseObject(string);
////		System.out.println(jo);
//		
//		
////		Dao dao = DBTools.getDao();
////		
////		List<Record> lr = dao.query("orgs", Cnd.where("id", ">", "20"));
////		
////		
////		
////		Flux.just(lr).map(rst -> {
////			System.out.println(rst);
////			if (rst instanceof java.util.List) {
////				return Flux.fromIterable((List<Record>)rst);
////			}
////			return Flux.just(rst);
////		})
////				.concatMap(fr -> fr)
////				.subscribe(v -> System.out.println((Record)v));
//	
//		
//		
//		
//	}
	
	//map转成inParamsDb,可接受多个
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put(".table", "account");
//		map.put(".type", "getOne");
//		map.put("id", 2);
//		
//		Map<String,Object> map1 = new HashMap<String,Object>();
//		map1.put(".table", "account");
//		map1.put(".type", "getOne");
//		map1.put("id", 1);
//		
//		Flux<Map<String,Object>> input = Flux.just(map, map1);
//		
////		Flux<Object> output = new SingleGetOne().apply(input);
//		Flux<Object> output = new ConditionCRUD<STFetch>(STFetch.class).apply(input);
//		
//		List<Object> results = output.collectList().log().block();
//		
////		Object obj = results.get(0);
//		
//		System.out.println(results.size());
//		System.out.println(results);
//		
//	}

	
	
	
//	public static void main(String[] args) {
////		Flux.just(1,2,3,4,5) //list of ids from database
////
////        .doOnNext(uuid ->{
////            getData(uuid).doOnSuccess((result) -> {
////                System.out.println("query data from database "+uuid);
//////                emitter.next("Data from database.");
////            });
////        })
////        .doOnComplete(()->{
////            System.out.println("Not waiting for all the Nested Mono to complete. ");
////        });
//		
//		Flux.just(1,2,3,4,5,6,7,8)
//        .log()
//        .flatMap(e -> {
//        	
//        	
//        	
//        	if (e == 5) {
//        		try {
//					TimeUnit.SECONDS.sleep(2);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//        	}
//        	System.out.println(e + ": " + new Date());
//        	return Flux.just(e*2).delayElements(Duration.ofSeconds(1));
////        	return e*2;
////            return e==5 ? Flux.just(e*2).delayElements(Duration.ofSeconds(2)) : Flux.just(e*2).delayElements(Duration.ofSeconds(1));
//        })
//        .subscribe(e -> {
//        	System.out.print(e/2 + "-s: " + new Date());
//        	System.out.println("  get: " + e);
//        	});
//		 try {
//			TimeUnit.SECONDS.sleep(10);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		 
////		Flux.just(1,2,3,4)
////        .log()
////        .map(i -> {
////        	System.out.println("i: " + new Date());
////            try {
////                TimeUnit.SECONDS.sleep(1);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////            return i * 2;
////        })
////        .subscribe(e -> 
////        {
////        	System.out.println("get: " + new Date());
////        	System.out.println("get: " + e);
////        });
//		
//	}
//	 public static void main(String[] args) throws Exception {
//		 Flux.range(1, 10)
//	        .parallel(1)
//	        .runOn(Schedulers.parallel())
//	        .map(i -> i + 1)
////	        .map(i -> i * 2)
////	        .map(i -> i + 1)
//	        .sequential()
//	        .subscribe(System.out::println);
//		 
//		 TimeUnit.SECONDS.sleep(20);
//		}
	 
//	 public static void main(String[] args) {
//		 String s= "test";
//		 String[] ss = s.split(",");
//		 
//		 System.out.println(ss);
//	 
//	}
	 

	 
}
