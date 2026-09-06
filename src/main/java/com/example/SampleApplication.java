/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableAsync
//@ComponentScan(value = "com.example.functions.db")
public class SampleApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SampleApplication.class, args);
	}
	
	
//	@Bean
//	public Function<Flux<Map<String, Object>>, Flux<Record>> test() {
//		return new ConditionMake().andThen(new SingleFetch());
//	}

//	@Bean
//	public Function<String, String> uppercase() {
//		return value -> value.toUpperCase();
//	}
//
//	@Bean
//	public Function<Flux<String>, Flux<String>> lowercase() {
//		return flux -> flux.map(value -> value.toLowerCase());
//	}
//
//	@Bean
//	public Supplier<String> hello() {
//		return () -> "hello";
//	}
//
//	@Bean
//	public Supplier<Flux<String>> words() {
//		return () -> Flux.fromArray(new String[] {"foo", "bar"});
//	}
//
//	@Bean
//	public Function<String, String> compiledUppercase(
//		FunctionCompiler<String, String> compiler) {
//		String lambda = "s -> s.toUpperCase()";
//		LambdaCompilingFunction<String, String> function = new LambdaCompilingFunction<>(
//			new ByteArrayResource(lambda.getBytes()), compiler);
//		function.setTypeParameterizations("String", "String");
//		return function;
//	}
//
//	@Bean
//	public Function<Flux<String>, Flux<String>> compiledLowercase(
//		FunctionCompiler<Flux<String>, Flux<String>> compiler) {
//		String lambda = "f->f.map(o->o.toString().toLowerCase())";
//		return new LambdaCompilingFunction<>(new ByteArrayResource(lambda.getBytes()),
//			compiler);
//	}
//
//	@Bean
//	public <T, R> FunctionCompiler<T, R> compiler() {
//		return new FunctionCompiler<>();
//	}

//	private BiFunction<Object, String, Object> inside_get_value = (r, field_get) -> {
//		Record record = (Record)r;
//		return record.getString(field_get) != null ? record.getString(field_get) : "";
//	};
	
}
