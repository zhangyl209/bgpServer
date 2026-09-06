package com.example.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import reactor.core.publisher.Flux;

@Builder(toBuilder = true)
@Data
public class InParamsActor {
	
	//用于Serial和Parallel单个运行; FuncFlowMapper; FuncManager从数据库中根据业务id查询
	private String classname;
	
	//用于Serial和Parallel 单个运行
	@SuppressWarnings("rawtypes")
	private Class workerClass;
	
	@SuppressWarnings("rawtypes")
	private Flux input;
	
	//用于funflow
	private List<String> classnameList;
	
//	//用于FuncFlowMapper
//	private String mapper;
	
	//用于GroupedFluxSolve
	private String solve;
	
//	//用于标记是否ResultFlatToBigFlux
//	private boolean hasFlated = false;
	
	private boolean isBlock = false;
	
	//用于存放当前用户信息（用户id）
	private String authUser;
}
