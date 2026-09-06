package com.example.functions;

import java.util.Date;
import java.util.function.Function;

import com.example.commons.InParamsActor;

import reactor.core.publisher.Flux;

public class SerialActor implements Function<Flux<InParamsActor>, Flux<Object>>{

	@Override
	public Flux<Object> apply(Flux<InParamsActor> workers) {
		// TODO Auto-generated method stub
		//如果使用map，返回的是对原flux的转换，对上层flux而言，该flux没变，得到该flux就是结果，不会等该flux出结果。所以要写代码处理。
		//如果使用flatMap，返回的是新的flux，对上层flux而言，新的flux替换了旧flux，并且是带着结果的新flux（runner.java里面return actor.log().collectList();）
		return workers.flatMap(worker -> {
			try {
				Class workerClass = null;
				if (worker.getWorkerClass() != null) {
					workerClass = worker.getWorkerClass();
				} else if (worker.getClassname() != null) {
					workerClass = Class.forName(worker.getClassname());
				}
				Object o = workerClass.newInstance();
				Flux<Object> output =(Flux<Object>) ((Function)o).apply(worker.getInput());
				return output;
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return Flux.empty();
		});
//		workers.collectList().map(workerL -> {
//			
//			for (int i = 0; i < workerL.size(); i++) {
//				InParamsActor worker = workerL.get(i);
//				
//				try {
//					Class workerClass = null;
//					if (worker.getWorkerClass() != null) {
//						workerClass = worker.getWorkerClass();
//					} else if (worker.getClassname() != null) {
//						workerClass = Class.forName(worker.getClassname());
//					}
//					Object o = workerClass.newInstance();
//					Flux<Object> output =(Flux<Object>) ((Function)o).apply(worker.getInput());
//					return output;
//				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//			
//			
//			return Flux.empty();
//		});
	}

}
