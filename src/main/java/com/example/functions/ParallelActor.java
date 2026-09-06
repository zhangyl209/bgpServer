package com.example.functions;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

import com.example.commons.InParamsActor;

import reactor.core.publisher.Flux;

public class ParallelActor implements Function<InParamsActor, Flux<Object>>{

	@Override
	public Flux<Object> apply(InParamsActor worker) {
		// TODO Auto-generated method stub
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return Flux.empty();
	}

}
