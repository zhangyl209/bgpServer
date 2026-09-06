package com.example.functions;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.commons.InParamsActor;
import com.example.commons.ResponseInfo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 分配actor
 * */
public class SerialActor0 implements Function<Flux<InParamsActor>, Flux<Object>>{

	@Override
	public Flux<Object> apply(Flux<InParamsActor> jobs) {
		// TODO Auto-generated method stub
		return jobs.map(job -> {
			System.out.println("job " + new Date());
			//通过使用block进行阻塞,不适用于前端调用：block()/blockFirst()/blockLast() are blocking, which is not supported in thread reactor-http-nio-3
			if (job.isBlock()) {
				try {
					Class workerClass = null;
					if (job.getWorkerClass() != null) {
						workerClass = job.getWorkerClass();
					} else if (job.getClassname() != null) {
						workerClass = Class.forName(job.getClassname());
					}
					
					Object o = workerClass.newInstance();
					Flux<Object> output =(Flux<Object>) ((Function)o).apply(job.getInput());
//					output.log().subscribe();
					List<Object> responseData = output.collectList().log().block();
					return responseData;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
			} else {
				try {
					Class workerClass = job.getWorkerClass();
					Object o = workerClass.newInstance();
					Flux<Object> output =(Flux<Object>) ((Function)o).apply(job.getInput());
					return output;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
			}
			return Flux.empty();
		});
	}

}
