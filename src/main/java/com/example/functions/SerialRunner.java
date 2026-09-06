package com.example.functions;

import java.util.List;

import org.nutz.trans.Molecule;
import org.nutz.trans.Trans;

import com.example.commons.InParamsActor;
import com.example.functions.db.STInsert;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class SerialRunner {

	public Mono<List<Object>> actorRun(Flux<InParamsActor> inParamsActors) {
		
		Flux<Object> actor = new SerialActor().apply(inParamsActors);

		return actor.log().collectList();
	}
}
