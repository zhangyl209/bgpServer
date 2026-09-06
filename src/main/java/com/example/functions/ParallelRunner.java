package com.example.functions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import com.example.commons.InParamsActor;

//import net.sf.cglib.proxy.Callback;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

public class ParallelRunner {
	
	public Mono<List<Object>> actorRun(Flux<InParamsActor> inParamsActors, int parallelNum) {
		
		ParallelFlux<InParamsActor> parallelFlux = inParamsActors.parallel(parallelNum);
		
		Flux<Object> parallel =
		parallelFlux
			.runOn(Schedulers.boundedElastic())
			.flatMap(inParams -> {
				try {
					
					System.out.println(((InParamsActor) inParams).getClass() + " start " + new Date());
					System.out.println(String.format("Executing on thread %s", Thread.currentThread().getName()));

					Flux<Object> output = new ParallelActor().apply(inParams);
					
					System.out.println(((InParamsActor) inParams).getClass() + " end " + new Date());
					return output;
				} catch (Exception e) {
					return Flux.empty();
				}
			}).sequential()
			.publishOn(Schedulers.single());
		
		return parallel.log().collectList();
//		return parallel.log().collectList();
	}
}
