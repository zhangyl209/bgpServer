package com.example.routers.handlers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.trans.Molecule;
import org.nutz.trans.Trans;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.alibaba.fastjson.JSONObject;
import com.example.commons.InParamsActor;
import com.example.commons.InParamsDb;
import com.example.commons.ResponseInfo;
import com.example.functions.ConvFluxJustToMono;
import com.example.functions.FuncFlow;
import com.example.functions.FuncFlowMapper;
import com.example.functions.FuncManager;
import com.example.functions.GroupedFluxSolve;
import com.example.functions.ParallelActor;
import com.example.functions.ParallelRunner;
import com.example.functions.ResultFlatToBigFlux;
import com.example.functions.SerialActor;
import com.example.functions.SerialRunner;
import com.example.functions.db.ConditionCRUD;
import com.example.functions.db.STClear;
import com.example.functions.db.STFetch;
import com.example.functions.db.STInsert;
import com.example.functions.db.STQuery;
import com.example.nutz.DBTools;

//import io.seata.spring.annotation.GlobalTransactional;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

@Component
public class MTHandler {
	
	Mono<Object> responseBody = Mono.empty();
	
	Mono<List<Object>> mlo;
	
	List<Object> workersResponses = new ArrayList<Object>();
	
	private Mono<List<Object>> thisWorkersResponses;
	
	

//	@GlobalTransactional
	//单个方法事务测试
//	@Transactional
//	public Mono<ServerResponse> insertDemo(ServerRequest request) {
//
//		//新增机构
//		Map<String,Object> orgMap = new HashMap<String,Object>();
//		orgMap.put(".table", "orgs");
//		orgMap.put("orgname", "testorg1");
//		
//		InParamsDb inParamsDb0 = InParamsDb.builder().m(orgMap).build();
//		
//		//新增人员
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put(".table", "users1");
//		map.put("name", "test1");
//		map.put("pwd", "123");
//		
//		InParamsDb inParamsDb1 = InParamsDb.builder().m(map).build();
//		
//		Flux<InParamsDb> input = Flux.just(inParamsDb0, inParamsDb1);
//		
//		Flux<Object> output = new STInsert().apply(input);
//
//		//flux具有懒惰性，不执行订阅，就不会执行。需要调用block、subscribe等方法，否则不会实际发出请求。
//		//new STInsert().apply(input1).subscribe();
//		final Mono<List<Object>> responseData = output.collectList();
//		
//		Mono<Object> body = responseData.map(responses -> {
//	          return ResponseInfo.ok(responses.get(0));
//			});
//		
//		return ServerResponse.ok().body(body, ResponseInfo.class);
//    }
	
	//多个方法事务
	public Mono<ServerResponse> insertDemo(ServerRequest request) {

		//新增机构
//		Map<String,Object> orgMap = new HashMap<String,Object>();
		Record orgMap = new Record();
		orgMap.put(".table", "orgs");
		orgMap.put("orgname", "20200916");
		
		InParamsDb inParamsDb0 = InParamsDb.builder().m(orgMap).build();
		
		Cnd cnd = Cnd.where("id", "in", 149);
		
		InParamsDb inParamsDb1 = InParamsDb.builder().table("orgs1").cnd(cnd).build();
//		//新增人员
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put(".table", "users1");
//		map.put("name", "20200916");
//		map.put("pwd", "123");
//		
//		InParamsDb inParamsDb1 = InParamsDb.builder().m(map).build();
		
		Flux<InParamsDb> input0 = Flux.just(inParamsDb0);
		
		Flux<InParamsDb> input1 = Flux.just(inParamsDb1);
		
		Flux<Object> output = (Flux<Object>) Trans.exec(new Molecule(){
			
		    public void run() {
		    
		    	Flux<Object> output0 = new STInsert().apply(input0);
		    	Flux<Object> output1 = new STClear().apply(input1);
		    	
		    	Flux<Object> output = output1.mergeWith(output0);
		    	
		    	setObj(output);
		    }
		});
		
//		Flux<Object> output = new STInsert().apply(input1);
//		new STInsert().apply(input).subscribe();

		//flux具有懒惰性，不执行订阅，就不会执行。需要调用block、subscribe等方法，否则不会实际发出请求。
		//new STInsert().apply(input1).subscribe();
		final Mono<List<Object>> responseData = output.collectList();
		
		Mono<Object> body = responseData.map(responses -> {
	          return ResponseInfo.ok(responses.get(0));
			});
		
		return ServerResponse.ok().body(body, ResponseInfo.class);
    }
	
	public Mono<ServerResponse> insertAndFetchFunction(ServerRequest request) {

		//新增机构
//		Map<String,Object> orgMap = new HashMap<String,Object>();
		Record orgMap = new Record();
		orgMap.put(".table", "orgs");
		orgMap.put("orgname", "testorg2");
		
		InParamsDb inParamsDb0 = InParamsDb.builder().m(orgMap).build();
		
		Cnd cnd = Cnd.where("id", "in", 149);
		
		InParamsDb inParamsDb1 = InParamsDb.builder().table("orgs1").cnd(cnd).build();
				
		//查询机构
//		Condition c1 = Cnd.where("orgname","=","testorg2");
//		InParamsDb inParamsDb1 = InParamsDb.builder().table("orgs").cnd(c1).build();
		
		Flux<InParamsDb> input0 = Flux.just(inParamsDb0);
		
		Flux<InParamsDb> input1 = Flux.just(inParamsDb1);
		
		new STInsert().apply(input0).subscribe();
		
		Flux<Object> output1 = new STQuery().apply(input1);

		final Mono<List<Object>> responseData = output1.collectList();
		
		Mono<Object> body = responseData.map(responses -> {
	          return ResponseInfo.ok(responses);
			});
		
		return ServerResponse.ok().body(body, ResponseInfo.class);
    }
	

//	public Mono<ServerResponse> serial1(ServerRequest request) {
//
////		//1
////		Condition c1 = Cnd.where("age",">",20);
////		InParamsDb i1 = InParamsDb.builder().table("users").cnd(c1).build();
////		
////		Condition c2 = Cnd.where("sex","=",1);
////		InParamsDb i2 = InParamsDb.builder().table("users").cnd(c2).build();
////		
////		Flux<InParamsDb> input = Flux.just(i1, i2);
////		InParamsActor inParamsActor = InParamsActor.builder().workerClass(STQuery.class).input(input).build();
//		
//		//1
//		Map<String,Object> orgMap = new HashMap<String,Object>();
//		orgMap.put(".table", "orgs");
//		orgMap.put("orgname", "testorg0908");
//		InParamsDb inParamsDb = InParamsDb.builder().m(orgMap).build();
//		Flux<InParamsDb> input = Flux.just(inParamsDb);
//		InParamsActor inParamsActor = InParamsActor.builder().classname("com.example.functions.db.STInsert").input(input).build();
//		//workerClass(STInsert.class)
//		//2
//		Map<String,Object> orgMap0 = new HashMap<String,Object>();
//		orgMap0.put(".table", "orgs");
//		orgMap0.put("orgname", "testorg0831");
//		
//		InParamsDb inParamsDb0 = InParamsDb.builder().m(orgMap0).build();
//		Flux<InParamsDb> input0 = Flux.just(inParamsDb0);
//		InParamsActor inParamsActor0 = InParamsActor.builder().classname("com.example.functions.db.STInsert").input(input0).build();
//		
////		SerialRunner serialRunner = new SerialRunner();
////		Mono<Object> body = serialRunner.actorRun(Flux.just(inParamsActor, inParamsActor0)).map(responses -> {
////	          return ResponseInfo.ok(responses);
////			});;
//			
//		Mono<Object> body1 = (Mono<Object>) Trans.exec(new Molecule(){			
//		    public void run() {
//		    	SerialRunner serialRunner = new SerialRunner();
//				Mono<Object> body = serialRunner.actorRun(Flux.just(inParamsActor, inParamsActor0)).map(responses -> {
//			          return ResponseInfo.ok(responses);
//					});;	 
//		    	setObj(body);
//		    }
//		});
//		
//		return ServerResponse.ok().body(body1, ResponseInfo.class);
//    }
	
	//串行
	public Mono<ServerResponse> serial1(ServerRequest request) {
		
		//1
//		Map<String,Object> orgMap = new HashMap<String,Object>();
		Record orgMap = new Record();
		orgMap.put(".table", "account");
		orgMap.put("id", "3");
		orgMap.put("username", "3");
		InParamsDb inParamsDb = InParamsDb.builder().m(orgMap).build();
		Flux<InParamsDb> input = Flux.just(inParamsDb);
		InParamsActor inParamsActor = InParamsActor.builder().classname("com.example.functions.db.STInsert").input(input).build();
		//workerClass(STInsert.class)
		//2
		Cnd cnd = Cnd.where("id", "in", "3");
		
		InParamsDb inParamsDb0 = InParamsDb.builder().table("account").cnd(cnd).build();
		Flux<InParamsDb> input0 = Flux.just(inParamsDb0);
		InParamsActor inParamsActor0 = InParamsActor.builder().classname("com.example.functions.db.STQuery").input(input0).build();
		
		SerialRunner serialRunner = new SerialRunner();
		Mono<Object> body = serialRunner.actorRun(Flux.just(inParamsActor, inParamsActor0)).map(responses -> {
	          return ResponseInfo.ok(responses);
			});;
		
		return ServerResponse.ok().body(body, ResponseInfo.class);
    }
	
//	//事务
//	public Mono<ServerResponse> serial1(ServerRequest request) {
//		
//		//1
//		Map<String,Object> orgMap = new HashMap<String,Object>();
//		orgMap.put(".table", "orgs");
//		orgMap.put("orgname", "20200916-1");
//		InParamsDb inParamsDb = InParamsDb.builder().m(orgMap).build();
//		Flux<InParamsDb> input = Flux.just(inParamsDb);
//		InParamsActor inParamsActor = InParamsActor.builder().classname("com.example.functions.db.STInsert").input(input).build();
//		//workerClass(STInsert.class)
//		//2
//		
//		Cnd cnd = Cnd.where("id", "in", 143);
//		
//		InParamsDb inParamsDb0 = InParamsDb.builder().table("orgs1").cnd(cnd).build();
//		Flux<InParamsDb> input0 = Flux.just(inParamsDb0);
//		InParamsActor inParamsActor0 = InParamsActor.builder().classname("com.example.functions.db.STClear").input(input0).build();
//		
////		SerialRunner serialRunner = new SerialRunner();
////		Mono<Object> body = serialRunner.actorRun(Flux.just(inParamsActor, inParamsActor0)).map(responses -> {
////	          return ResponseInfo.ok(responses);
////			});;
//		
//		Flux<Object> output = (Flux<Object>) Trans.exec(new Molecule(){
//			
//		    public void run() {
//		    	
//		    	try {
//		    		InParamsActor worker = inParamsActor;
//		    		
//					Class workerClass = null;
//					if (worker.getWorkerClass() != null) {
//						workerClass = worker.getWorkerClass();
//					} else if (worker.getClassname() != null) {
//						workerClass = Class.forName(worker.getClassname());
//					}
//					Object o = workerClass.newInstance();
//					Flux<Object> output =(Flux<Object>) ((Function)o).apply(worker.getInput());
//					
//					InParamsActor worker0 = inParamsActor0;
//		    		
//					Class workerClass0 = null;
//					if (worker0.getWorkerClass() != null) {
//						workerClass0 = worker0.getWorkerClass();
//					} else if (worker0.getClassname() != null) {
//						workerClass0 = Class.forName(worker0.getClassname());
//					}
//					Object o0 = workerClass0.newInstance();
//					Flux<Object> output0 =(Flux<Object>) ((Function)o0).apply(worker0.getInput());
//					
//					Flux<Object> out = output.mergeWith(output0);
//			    	
//			    	setObj(out);
//					
//				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					setObj(null);
//				}
//		    
////		    	Flux<Object> output0 = new STInsert().apply(input0);
////		    	Flux<Object> output1 = new STInsert().apply(input1);
////		    	
////		    	Flux<Object> output = output1.mergeWith(output0);
////		    	
//		    	
//		    }
//		});
//		
//		final Mono<List<Object>> responseData = output.collectList();
//		
//		Mono<Object> body = responseData.map(responses -> {
//	          return ResponseInfo.ok(responses.get(0));
//			});
//		
//		return ServerResponse.ok().body(body, ResponseInfo.class);
//    }
	
	public Mono<ServerResponse> parallel1(ServerRequest request) {

		//1
		Condition c1 = Cnd.where("id",">",20);
		InParamsDb i1 = InParamsDb.builder().table("orgs").cnd(c1).build();
		
		Condition c2 = Cnd.where("id","<",19);
		InParamsDb i2 = InParamsDb.builder().table("orgs").cnd(c2).build();
		
		Flux<InParamsDb> input = Flux.just(i1, i2);
		InParamsActor inParamsActor = InParamsActor.builder().workerClass(STQuery.class).input(input).build();
		
		//2
//		Map<String,Object> orgMap = new HashMap<String,Object>();
		Record orgMap = new Record();
		orgMap.put(".table", "orgs");
		orgMap.put("orgname", "testorg0831");
		
		InParamsDb inParamsDb0 = InParamsDb.builder().m(orgMap).build();
		Flux<InParamsDb> input0 = Flux.just(inParamsDb0);
		InParamsActor inParamsActor0 = InParamsActor.builder().workerClass(STInsert.class).input(input0).build();
		
		ParallelRunner parallelRunner = new ParallelRunner();
		Mono<Object> body = parallelRunner.actorRun(Flux.just(inParamsActor, inParamsActor0), 5).map(responses -> {
	          return ResponseInfo.ok(responses);
			});;
		
		
		return ServerResponse.ok().body(body, ResponseInfo.class);
    }
	
	
	
//	//单个test
//	public Mono<ServerResponse> serial1(ServerRequest request) {
//
//		Condition c1 = Cnd.where("age",">",20);
//		InParamsDb i1 = InParamsDb.builder().table("users").cnd(c1).build();
//		
//		Condition c2 = Cnd.where("sex","=",1);
//		InParamsDb i2 = InParamsDb.builder().table("users").cnd(c2).build();
//		
//		Flux<InParamsDb> input = Flux.just(i1, i2);
//		
//		InParamsActor inParamsActor = InParamsActor.builder().workerClass(STQuery.class).input(input).build();
//		Flux<Object> output = new SerialActor().apply(Flux.just(inParamsActor));
//		
//		final Mono<List<Object>> responseData = output.log().collectList();//Object是Flux<Object>
//		
//		Mono<Flux<Object>> body = responseData.map(responses -> {
//			
//			Flux<Object> actorOutput = ((Flux<Object>) responses.get(0));
//
//			return actorOutput;
//		});
//		
//		body.log().subscribe(actorOutput -> {
//			responseBody = actorOutput.log().collectList().map(responses -> {
//				return responses;
//			});
//		});
//		
//		return ServerResponse.ok().body(responseBody, ResponseInfo.class);
//    }
	
	
	//组合函数（类）
	public Mono<ServerResponse> funcFlow1(ServerRequest request) {
		
		//新增机构
//		Map<String,Object> orgMap = new HashMap<String,Object>();
		Record orgMap = new Record();
		orgMap.put(".table", "orgs");
		orgMap.put("orgname", "testorg0915");
		InParamsDb inParamsDb = InParamsDb.builder().m(orgMap).build();
		
//		Map<String,Object> orgMap2 = new HashMap<String,Object>();
		Record orgMap2 = new Record();
		orgMap2.put(".table", "orgs");
		orgMap2.put("orgname", "testorg0223");
		InParamsDb inParamsDb2 = InParamsDb.builder().m(orgMap2).build();
		
		Flux<InParamsDb> input = Flux.just(inParamsDb, inParamsDb2);
				
		List<String> workerL = new ArrayList<String>();
		workerL.add("com.example.functions.db.STInsert");
		workerL.add("com.example.businesses.QueryId");
		
		InParamsActor inParamsActor = InParamsActor.builder().classnameList(workerL).input(input).build();
		 
		Mono<List<Object>> responseData = new FuncFlow().apply(inParamsActor).collectList();
		
		Mono<Object> body = responseData.map(responses -> {
	          return ResponseInfo.ok(((List<Object>)responses).get(0));
			});
		
		return ServerResponse.ok().body(body, ResponseInfo.class);
		
    }
	
	//组合函数（类和function）
	public Mono<ServerResponse> funcFlow2(ServerRequest request) {
		
		//查机构
		Cnd cnd1 = Cnd.where("id", ">", "20");	
		InParamsDb inParamsDb1 = InParamsDb.builder().table("orgs").cnd(cnd1).build();
		
		Cnd cnd2 = Cnd.where("id", "<", "18");	
		InParamsDb inParamsDb2 = InParamsDb.builder().table("orgs").cnd(cnd2).build();
		
		Cnd cnd3 = Cnd.where("id", "=", "18");	
		InParamsDb inParamsDb3 = InParamsDb.builder().table("orgs").cnd(cnd3).build();
	
		//改机构
	
		
		
		Flux<InParamsDb> input = Flux.just(inParamsDb1, inParamsDb2);
				
		List<String> workerL = new ArrayList<String>();
//		workerL.add("com.example.functions.db.STFetch");
		workerL.add("com.example.functions.db.STQuery");
//		workerL.add("com.example.businesses.Test");
		workerL.add("testf1");
		workerL.add("testf2");
		
		InParamsActor inParamsActor = InParamsActor.builder().classnameList(workerL).input(input).build();
		
		Flux<Object> f = new FuncFlow().apply(inParamsActor);
		
		//转成大flux
		Flux<Object> ffr  = new ResultFlatToBigFlux().apply(f);
		
		Mono<List<Object>> responseData = ffr.collectList();
		
		//分别输出
//		Mono<List<Object>> responseData = new FuncFlow().apply(inParamsActor).collectList();
		
		Mono<Object> body = responseData.map(responses -> {
//	          return ResponseInfo.ok(((List<Object>)responses));
				return ResponseInfo.ok(responses);
			});
		
		return ServerResponse.ok().body(body, ResponseInfo.class);
		
    }
	
	//组合函数（类和function）
	public Mono<ServerResponse> funcFlow3(ServerRequest request) {
		
		//查机构
		Cnd cnd1 = Cnd.where("id", ">", "20");	
		InParamsDb inParamsDb1 = InParamsDb.builder().table("orgs").cnd(cnd1).build();
		
		Cnd cnd2 = Cnd.where("id", "<", "18");	
		InParamsDb inParamsDb2 = InParamsDb.builder().table("orgs").cnd(cnd2).build();
		
		Cnd cnd3 = Cnd.where("id", ">=", "18").and("id", "<=", "20");	
		InParamsDb inParamsDb3 = InParamsDb.builder().table("orgs").cnd(cnd3).build();
	
		
		Flux<InParamsDb> input = Flux.just(inParamsDb1,inParamsDb2,inParamsDb3);
				
		List<String> workerL = new ArrayList<String>();
		workerL.add("com.example.functions.db.STQuery");
//			workerL.add("com.example.businesses.Test");
		workerL.add("testf1");
//		workerL.add("testf2");
		
		InParamsActor inParamsActor = InParamsActor.builder().classnameList(workerL).input(input).build();
		
		Flux<Object> f = new FuncFlow().apply(inParamsActor);
		
		//转成大flux
		Flux<Object> ffr  = new ResultFlatToBigFlux().apply(f);
		
		//mapper
		InParamsActor inParamsActorMapper = InParamsActor.builder().classname("testMapper").input(ffr).build();
		Flux<GroupedFlux<Object, Object>> fGf = new FuncFlowMapper().apply(inParamsActorMapper);
		
		//处理GroupedFlux
		InParamsActor inParamsActorSolve = InParamsActor.builder().solve("{\"a\":\"testf1\", \"b\":\"testf2\", \"c\":\"testf1\"}").input(fGf).build();
		Flux<Object> gfs = new GroupedFluxSolve().apply(inParamsActorSolve);
		 
		Mono<List<Object>> responseData = gfs.collectList();
		
		Mono<Object> body = responseData.map(responses -> {
	          return ResponseInfo.ok(responses);
			});
		
		return ServerResponse.ok().body(body, ResponseInfo.class);
		
    }
	
	//组合函数（类和function）
	public Mono<ServerResponse> funcFlow4(ServerRequest request) {
		
		//查机构
		Cnd cnd1 = Cnd.where("id", ">", "20");	
		InParamsDb inParamsDb1 = InParamsDb.builder().table("orgs").cnd(cnd1).build();
		
		Cnd cnd2 = Cnd.where("id", "<", "18");	
		InParamsDb inParamsDb2 = InParamsDb.builder().table("orgs").cnd(cnd2).build();
		
		Cnd cnd3 = Cnd.where("id", ">=", "18").and("id", "<=", "20");	
		InParamsDb inParamsDb3 = InParamsDb.builder().table("orgs").cnd(cnd3).build();
	
		
		Flux<InParamsDb> input = Flux.just(inParamsDb1,inParamsDb2,inParamsDb3);
		
		InParamsActor inParamsActor = InParamsActor.builder().classname("5").input(input).build();
		
		Flux<Object> gfs = new FuncManager().apply(inParamsActor);
		
		Mono<List<Object>> responseData = gfs.collectList();
		
		Mono<Object> body = responseData.map(responses -> {
	          return ResponseInfo.ok(responses);
			});
		
		return ServerResponse.ok().body(body, ResponseInfo.class);
		
    }
	
}
