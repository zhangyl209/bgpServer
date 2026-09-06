package com.example.functions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.commons.InParamsActor;
import com.example.commons.InParamsDb;
import com.example.commons.ResponseInfo;
import com.example.functions.db.STQuery;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 执行工作
 * */
public class SerialRunner0 {
	
	private Mono<List<Object>> workersResponses;
	
	public Mono<List<Object>> actorRun(Flux<InParamsActor> inParamsActors) {
		
		Flux<Object> serialActor = new SerialActor().apply(inParamsActors);
		
		final Mono<List<Object>> serialActorResponseData = serialActor.log().collectList();//Object是Flux<Object>
		
		Mono<List<Flux<Object>>> serialActorOutput = serialActorResponseData.map(responses -> { //responses是List<Flux<Object>>
			
			List<Flux<Object>> workersOutput = new ArrayList<Flux<Object>>();
			Iterator<Object> iter = responses.iterator();
		    while (iter.hasNext()) {
		    	Flux<Object> workerOutput = (Flux<Object>) iter.next(); 
		    	workersOutput.add(workerOutput);
		    }
		    
			return workersOutput;
		});
		
		serialActorOutput.log().subscribe(workersOutput -> {
			
			List<Object> thisWorkersResponses = new ArrayList<Object>();
			Iterator<Flux<Object>> iter = workersOutput.iterator();
		    while (iter.hasNext()) {
		    	Flux<Object> workerOutput = iter.next();
		    	workerOutput.log().subscribe(responses -> {
		    		thisWorkersResponses.add(responses);
		    	});		    		
		    }	
		    
		    Flux<Object> thisWorkersResponsesFlux = Flux.fromIterable(thisWorkersResponses);
		    
		    workersResponses = thisWorkersResponsesFlux.collectList();   
		});
		
		return workersResponses;
		
	}
	
	
	
}
