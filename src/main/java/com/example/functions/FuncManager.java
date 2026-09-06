package com.example.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.VarIndex;
import org.nutz.dao.sql.VarSet;
import org.nutz.dao.util.cri.Static;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.example.commons.BusinessConfigType;
import com.example.commons.FiledRelationType;
import com.example.commons.InParamsActor;
import com.example.commons.InParamsDb;
import com.example.nutz.DBTools;
import com.example.utils.RegexMatchUtils;
import com.example.utils.SpringUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

/*
 * 从数据库中读取函数组合配置
 * */
public class FuncManager implements Function<InParamsActor, Flux<Object>> {

	private Dao dao = DBTools.getDao();
	
	public String makeInStrings(String field_target_string) {
		
		//["20", "31"]
		field_target_string = field_target_string.replace("[", "");
		field_target_string = field_target_string.replace("]", "");
		
		List<String> field_target_array = Arrays.asList(field_target_string.split(",")).stream().toList();
		field_target_string = "";
		for (String str:field_target_array) {
			field_target_string = field_target_string + "'" + removeDoubleQuotation(str.trim()) + "',";
		}
		field_target_string = field_target_string.substring(0, field_target_string.length() -1 );
		
		return field_target_string;
	}
	
	public String removeDoubleQuotation(String input) {
        String patternStr = "^\"(.*?)\"$";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(input);
        String output = matcher.replaceAll("$1");
        return output;
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public Flux<Object> apply(InParamsActor actor) {
		
		Flux<GroupedFlux<Object, Object>> fgf = null;
		Flux<Object> f = null;
		
		Record config_funcflow = dao.fetch("config_funcflow", Cnd.where("id","=",actor.getClassname()).and("status", "=", 1));
		
		if (config_funcflow == null) {
			return Flux.error(new Exception("config_funcflow中没有找到该配置，id：" + actor.getClassname()));
		}
		
		String funcs = config_funcflow.getString("funcs");
		
		switch(config_funcflow.getInt("type")) {
		
		case BusinessConfigType.mapper:
			
			//转成大flux
			Flux<Object> ffr  = new ResultFlatToBigFlux().apply(actor.getInput());
//			actor.setHasFlated(true);
			
			//mapper
			actor.setClassname(funcs);
			actor.setInput(ffr);
			fgf = new FuncFlowMapper().apply(actor);
			
			//处理GroupedFlux
			String solve = config_funcflow.getString("reducer");
			actor.setSolve(solve);
			actor.setInput(fgf);
			f = new GroupedFluxSolveComplex().apply(actor);
			
			break;
		case BusinessConfigType.inside_id_to_value:
			String field_target = config_funcflow.getString("field_target");
			if (!StringUtils.hasText(field_target)) {
				return Flux.just("缺少field_target配置, config_funcflow id is ：" + config_funcflow.getString("id"));
			}
			String field_relation = config_funcflow.getString("field_relation");
			if (!StringUtils.hasText(field_relation)) {
				return Flux.just("缺少field_relation配置, config_funcflow id is ：" + config_funcflow.getString("id"));
			}
			
			String field_cnd = config_funcflow.getString("field_cnd");
			
			//转成大flux
			Flux<Object> ffr_inside_id_to_value  = new ResultFlatToBigFlux().apply(actor.getInput());
			f = ffr_inside_id_to_value.flatMap(r -> {
				
				Record record = (Record)r;
				
				String field_target_string = record.getString(field_target);
				
				//如果是空，就不用关联查询了
				if (!StringUtils.hasText(field_target_string)) {
					return Mono.just(r);
				}
				
				String field_table = config_funcflow.getString("field_table");
				//如果没有配置从哪个表里查，就从目标字段同名的表里查
				if (!StringUtils.hasText(field_table)) {
					field_table = field_target;
				}
				
				String field_source = config_funcflow.getString("field_source");
			  	//如果没有子表的哪个字段查，就用id
				if (!StringUtils.hasText(field_source)) {
					field_source = "id";
				}
				
				int field_relation_int = Integer.parseInt(field_relation);
				String field_relation_type = "=";
				
				switch (field_relation_int) {
				case FiledRelationType.equal:
					break;
				case FiledRelationType.in:
					field_relation_type = "in";
					break;
				case FiledRelationType.not_in:
					field_relation_type = "not in";
					break;
				default:
					break;
				}
				
				if ( field_relation_type != "=") {
					field_target_string = makeInStrings(field_target_string);
				}
				
				Cnd cnd = StringUtils.hasText(field_cnd) ? 
						Cnd.where(field_source, field_relation_type, field_target_string)
							.and("status", "=", "1")
							.and(new Static(field_cnd)) :
						Cnd.where(field_source, field_relation_type, field_target_string)
							.and("status", "=", "1");
				
				List<Record> children = dao.query(field_table, cnd);
			  	
				//如果没有配置结果填到哪个字段，就填到目标字段
				String field_result = StringUtils.hasText(config_funcflow.getString("field_result")) ? config_funcflow.getString("field_result") : field_target;
				
				//如果没有children可以直接返回
				if ( children == null || children.size() == 0 ) {
					return Mono.just(record);
				}
			    //对children继续迭代处理
				if (StringUtils.hasText(funcs) && children != null && children.size() > 0 ) {
					
					Flux<Object> childrenAsInput = Flux.fromIterable(children);
					
					InParamsActor inParamsActor = InParamsActor.builder().classname(funcs).input(childrenAsInput).authUser(actor.getAuthUser()).build();
					
					return new FuncManager().apply(inParamsActor).collectList().map(list -> {
						record.put(field_result, field_relation_int == FiledRelationType.equal ? list.get(0) : list);	
						return record;
					});
					
						
				} else {
					
					record.put(field_result, field_relation_int == FiledRelationType.equal ? children.get(0) : children);
					return Mono.just(record);
					
				}
				
			});
			
			break;
		case BusinessConfigType.inside_replace_id_with_value:
			String field_replaced = config_funcflow.getString("field_target");
			if (!StringUtils.hasText(field_replaced)) {
				return Flux.just("缺少field_target配置, config_funcflow id is ：" + config_funcflow.getString("id"));
			}
			String field_relation11 = config_funcflow.getString("field_relation");
			if (!StringUtils.hasText(field_relation11)) {
				return Flux.just("缺少field_relation配置, config_funcflow id is ：" + config_funcflow.getString("id"));
			}
			
			String field_cnd11 = config_funcflow.getString("field_cnd");
			
			//转成大flux
			Flux<Object> ffr_inside_replace_id_with_value  = new ResultFlatToBigFlux().apply(actor.getInput());
			f = ffr_inside_replace_id_with_value.flatMap(r -> {
				
				Record record = (Record)r;
				
				String field_target_string = record.getString(field_replaced);
				
				//如果是空，就不用关联查询了
				if (!StringUtils.hasText(field_target_string)) {
					return Mono.just(r);
				}
				
				String field_table = config_funcflow.getString("field_table");
				//如果没有配置从哪个表里查，就从目标字段同名的表里查
				if (!StringUtils.hasText(field_table)) {
					field_table = field_replaced;
				}
				
				String field_source = config_funcflow.getString("field_source");
			  	//如果没有子表的哪个字段查，就用id
				if (!StringUtils.hasText(field_source)) {
					field_source = "id";
				}
				
				int field_relation_int = Integer.parseInt(field_relation11);
				String field_relation_type = "=";
				
				switch (field_relation_int) {
				case FiledRelationType.equal:
					break;
				case FiledRelationType.in:
					field_relation_type = "in";
					break;
				case FiledRelationType.not_in:
					field_relation_type = "not in";
					break;
				default:
					break;
				}
				
				if ( field_relation_type != "=") {
					field_target_string = makeInStrings(field_target_string);
				}
				
				Cnd cnd = StringUtils.hasText(field_cnd11) ? 
						Cnd.where(field_source, field_relation_type, field_target_string)
							.and("status", "=", "1")
							.and(new Static(field_cnd11)) :
						Cnd.where(field_source, field_relation_type, field_target_string)
							.and("status", "=", "1");
				
				List<Record> children = dao.query(field_table, cnd);
			  	
				//从replace里获取哪个字段
				String field_result = StringUtils.hasText(config_funcflow.getString("field_result")) ? config_funcflow.getString("field_result") : field_replaced;
				
				//如果没有children可以直接返回
				if ( children == null || children.size() == 0 ) {
					return Mono.just(record);
				}
			    //对children继续迭代处理
				if (StringUtils.hasText(funcs) && children != null && children.size() > 0 ) {
					
					Flux<Object> childrenAsInput = Flux.fromIterable(children);
					
					InParamsActor inParamsActor = InParamsActor.builder().classname(funcs).input(childrenAsInput).authUser(actor.getAuthUser()).build();
					
					return new FuncManager().apply(inParamsActor).collectList().map(list -> {
						record.put(field_result, field_relation_int == FiledRelationType.equal ? list.get(0) : list);	
						return record;
					});
					
						
				} else {
					List<Object> list = new ArrayList<Object>();
					for (int i = 0; i < children.size(); i++) {
						Record rr = children.get(i);
						list.add(rr.get(field_result));
					}
					
					Object listStirng = list.stream().map(String::valueOf).collect(Collectors.joining(", "));
					
					record.put(field_replaced, listStirng);
					return Mono.just(record);
					
				}
				
			});
			
			break;
		case BusinessConfigType.inside_get_value:
			
			String field_get = config_funcflow.getString("field_target");
			if (!StringUtils.hasText(field_get)) {
				return Flux.just("缺少field_target配置, config_funcflow id is ：" + config_funcflow.getString("id"));
			}
			
			//转成大flux
			Flux<Object> ffr_inside_get_value  = new ResultFlatToBigFlux().apply(actor.getInput());
			f = ffr_inside_get_value.map(r -> {
				Record record = (Record)r;
				//如果field_target配置了多个字段
				if (field_get.contains(",")) {
					String[] field_gets = field_get.split(",");
					Record fieldRecord = new Record();
					for (int i = 0; i < field_gets.length; i++) {
						fieldRecord.put(field_gets[i], record.get(field_gets[i]));
					}
					return fieldRecord;
					
				} else { //如果field_target配置了1个字段
					return record.get(field_get) != null ? record.get(field_get) : "";
				}
			});
			
			break;
		case BusinessConfigType.inside_remove_value:
			String field_remove = config_funcflow.getString("field_target");
			if (!StringUtils.hasText(field_remove)) {
				return Flux.just("缺少field_target配置, config_funcflow id is ：" + config_funcflow.getString("id"));
			}
			
			//转成大flux
			Flux<Object> ffr_inside_remove_value  = new ResultFlatToBigFlux().apply(actor.getInput());
			f = ffr_inside_remove_value.map(r -> {
				Record record = (Record)r;
				//如果field_target配置了多个字段
				if (field_remove.contains(",")) {
					String[] field_removes = field_remove.split(",");
					for (int i = 0; i < field_removes.length; i++) {
						record.remove(field_removes[i]);
					}
					return record;
					
				} else { //如果field_target配置了1个字段
					record.remove(field_remove);
					return record;
				}
			});
			
			break;
		case BusinessConfigType.inside_id_to_value_from_relation:
			String field_target_r = config_funcflow.getString("field_target");
			if (!StringUtils.hasText(field_target_r)) {
				return Flux.just("缺少field_target配置, config_funcflow id is ：" + config_funcflow.getString("id"));
			}
			String field_relation_r = config_funcflow.getString("field_relation");
			if (!StringUtils.hasText(field_relation_r)) {
				return Flux.just("缺少field_relation配置, config_funcflow id is ：" + config_funcflow.getString("id"));
			}
			String field_table_r = config_funcflow.getString("field_table");
			if (!StringUtils.hasText(field_table_r)) {
				return Flux.just("缺少field_table配置, config_funcflow id is ：" + config_funcflow.getString("id"));
			}
			String field_source_r = config_funcflow.getString("field_source");
			if (!StringUtils.hasText(field_source_r)) {
				return Flux.just("缺少field_source配置, config_funcflow id is ：" + config_funcflow.getString("id"));
			}
			String field_cnd_r = config_funcflow.getString("field_cnd");
			
			//转成大flux
			Flux<Object> ffr_inside_id_to_value_relation  = new ResultFlatToBigFlux().apply(actor.getInput());
			f = ffr_inside_id_to_value_relation.flatMap(r -> {
				
				Record record = (Record)r;
				
				String field_target_string = record.getString(field_target_r);
				
				//如果是空，就不用关联查询了
				if (!StringUtils.hasText(field_target_string)) {
					return Mono.just(r);
				}
				
				int field_relation_int = Integer.parseInt(field_relation_r);
				String field_relation_type = "=";
				
				
				
				switch (field_relation_int) {
				case FiledRelationType.equal:
					break;
				case FiledRelationType.in:
					field_relation_type = "in";
					break;
				case FiledRelationType.not_in:
					field_relation_type = "not in";
					break;
				default:
					break;
				}
				
				if ( field_relation_type != "=") {
					field_target_string = makeInStrings(field_target_string);
				}
				
				Cnd cnd = StringUtils.hasText(field_cnd_r) ? 
						Cnd.where(field_source_r, field_relation_type, field_target_string)
							.and("status", "=", "1")
							.and(new Static(field_cnd_r)) :
						Cnd.where(field_source_r, field_relation_type, field_target_string)
							.and("status", "=", "1");
				String cndString = cnd.getCri().toString(); //WHERE account IN ('1') AND status='1'
				
				//关联字段直接用关联表名处理
				String relation_field = field_table_r.replace(field_source_r, "").replace("_", "");
				
				List<Record> children = SpringUtils.selectFromRelations(field_target_r, field_table_r, relation_field, cndString);
				
				//如果没有配置结果填到哪个字段，就填到目标字段
				String field_result_r = StringUtils.hasText(config_funcflow.getString("field_result")) ? config_funcflow.getString("field_result") : field_target_r;
				
				//如果没有children可以直接返回
				if ( children == null || children.size() == 0 ) {
					return Mono.just(record);
				}
				
				if (StringUtils.hasText(funcs) && children != null && children.size() > 0 ) {    //对children继续迭代处理
					
					Flux<Object> childrenAsInput = Flux.fromIterable(children);
					
					InParamsActor inParamsActor = InParamsActor.builder().classname(funcs).input(childrenAsInput).authUser(actor.getAuthUser()).build();
					
					return new FuncManager().apply(inParamsActor).collectList().map(list -> {
						record.put(field_result_r, field_relation_int == FiledRelationType.equal ? list.get(0) : list);
						return record;
					});
						
				} else {
					record.put(field_result_r, field_relation_int == FiledRelationType.equal ? children.get(0) : children);
					return Mono.just(record);
					
				}
				
			});
			
			break;
		case BusinessConfigType.inside_add_auto_increment_id:
			String databaseName = config_funcflow.getString("field_target");
			if (!StringUtils.hasText(databaseName)) {
				return Flux.just("缺少field_target配置, config_funcflow id is ：" + config_funcflow.getString("id"));
			}
			
			String tableName = config_funcflow.getString("field_table");
			if (!StringUtils.hasText(tableName)) {
				return Flux.just("缺少field_table配置, config_funcflow id is ：" + config_funcflow.getString("id"));
			}
			
			//转成大flux
			Flux<Object> ffr_inside_add_id  = new ResultFlatToBigFlux().apply(actor.getInput());
			
			int autoIncrementID = SpringUtils.getAutoIncrementID(databaseName, tableName);
//			AtomicInteger count = new AtomicInteger(autoIncrementID);
			f = ffr_inside_add_id.map(r -> {
				Record record = (Record)r;
//				record.put("id", count.getAndIncrement());
				record.put("id", autoIncrementID);
				return record;
			});
			
			break;
		case BusinessConfigType.put_record_into_inParamsDb:
			
			if ( StringUtils.hasText(funcs) ) { //如果没设置就不需要其他条件，有的话，从config_input中获取
				Record config_input = dao.fetch("config_input", Cnd.where("id","=",funcs).and("status", "=", 1));
				
				if ( config_input == null ) {
					return Flux.just("缺少config_input配置(该配置在funcs字段里), config_funcflow id is ：" + config_funcflow.getString("id"));
				}

				f = actor.getInput().map(r -> {
					Record record = (Record)r;
					
					//如果record中有.table设置，以此为准，InParamsDb中table设置无效，下面这段没有意义
//					if ( ! StringUtils.hasText( record.getString(".table") ) ) {
//						record.put(".table", config_input.getString("table"));
//					}
					
					String cndString = null;
					if ( StringUtils.hasText( record.getString(".cnd") ) ) {
						cndString = record.getString(".cnd");
					} else if ( StringUtils.hasText( config_input.getString("cndwrap") ) ) {
						cndString = config_input.getString("cndwrap");
					}
					
					//如果有设置条件
					if (StringUtils.hasText(cndString)) {
						
						
						List<String> placeHolders = RegexMatchUtils.getPlaceholder(cndString, "$");
						
						//判断如果cnd中需要的值没有，替换一下1=1,需要配置cndwrapignore
						if (StringUtils.hasText(config_input.getString("cndwrapignore"))) {
							for (int i = 0 ; i<placeHolders.size(); i++) {
								String ph = placeHolders.get(i);
								if (ph.equals("auth")) {
									continue;
								} else {
									if (!StringUtils.hasText(record.getString(ph))) {
										String phxxxph = RegexMatchUtils.getConditionWith(cndString, "$", ph);
										if (phxxxph != null) {
											if (phxxxph.trim().endsWith(")") && !phxxxph.contains("(")) {
												phxxxph = phxxxph.substring(0, phxxxph.lastIndexOf(")"));
											}
											cndString = cndString.replace(phxxxph, "1=1 ");
										}
									}	
								}
							}
						}
						
						Sql sql = Sqls.create(cndString);
						
						for (int i = 0 ; i<placeHolders.size(); i++) {
							String ph = placeHolders.get(i);
							if (ph.equals("auth")) {
								sql.vars().set(ph, actor.getAuthUser());
							} else {
								sql.vars().set(ph, record.getString(ph));	
							}
						}
						
						//如果blocked有值，需要去掉
						String locked = config_input.getString("locked");
						if (StringUtils.hasText(locked)) {
							String[] field_removes = locked.split(",");
							for (int i = 0; i < field_removes.length; i++) {
								record.remove(field_removes[i]);
							}
						}
						
						String cndWrap = sql.toString();
						Condition c = Cnd.wrap(cndWrap);
						
						//如果sort有值，需要进行排序
						JSONObject sortJson = new JSONObject();
			            if (record.get("sort") != null && record.get("order") != null){
			                String sort = record.get("sort").toString().trim().toLowerCase();
			                String order = record.get("order").toString().trim().toLowerCase();
			                String key = order.equals("asc") || order.equals("ascend") ? "asc" : "desc";
			                
			                sortJson.put(key, sort);
			            }
			            //追加config_input内的配置
						String configSort = config_input.getString("sort");
						if (StringUtils.hasText(configSort)) {
							JSONObject configSortJson = JSONObject.parseObject(configSort);
							
							if (configSortJson.get("asc") != null) {
								String newSorts = sortJson.get("asc") != null ? sortJson.getString("asc").concat("," + configSortJson.getString("asc")) : configSortJson.getString("asc");
								sortJson.put("asc", newSorts);
							}
							if (configSortJson.get("desc") != null) {
								String newSorts = sortJson.get("desc") != null ? sortJson.getString("desc").concat("," + configSortJson.getString("desc")) : configSortJson.getString("desc");
								sortJson.put("desc", newSorts);
							}
						}
						
						if (sortJson.get("asc") != null || sortJson.get("desc") != null) {
							c = SpringUtils.addOrderByToSql(sortJson.toString(), cndWrap);
						}
						
						InParamsDb inParamsDb = InParamsDb.builder()
								.table(config_input.getString("table"))
								.fields(config_input.getString("fields"))
								.m(record)
								.cnd( c )
								.build();
						
						if (StringUtils.hasText(record.getString("pagenum")) && StringUtils.hasText(record.getString("pagesize"))) {
							inParamsDb.setPageNumber(record.getInt("pagenum"));
							inParamsDb.setPageSize(record.getInt("pagesize"));
						}
						 
						 return inParamsDb;
					} else {
						
						//如果blocked有值，需要去掉
						String locked = config_input.getString("locked");
						if (StringUtils.hasText(locked)) {
							if (locked.contains(",")) {
								String[] field_removes = locked.split(",");
								for (int i = 0; i < field_removes.length; i++) {
									record.remove(field_removes[i]);
								}
							} else { //如果blocked配置了1个字段
								record.remove(locked);
							}
						}
						
						return InParamsDb.builder()
								.table(config_input.getString("table"))
								.m(record)
								.build();
					}
					
				});

			} else {
				f = actor.getInput().map(r -> {
					Record record = (Record)r;
					return InParamsDb.builder().m(record).cnd(Cnd.wrap(record.getString(".cnd"))).build();
				});
			}

			break;
		case BusinessConfigType.pagination:
			
			f = actor.getInput().flatMap(pagination ->  {
				
				QueryResult qr = (QueryResult)pagination;
				
				if (StringUtils.hasText(funcs)) {
					InParamsActor inParamsActor = InParamsActor.builder().classname(funcs).input(Flux.fromIterable(qr.getList())).authUser(actor.getAuthUser()).build();
					
					return new FuncManager().apply(inParamsActor).collectList().map(list->qr.setList(list));
					
				}			
				
				return Mono.just(qr);
				
			});
			
			break;
		case BusinessConfigType.auth_user:
			List<String> workerL_withAuthUser = Arrays.asList(funcs.split(","));
			
			actor.setClassnameList(workerL_withAuthUser);
			
			f = new FuncFlowWithAuthUser().apply(actor);
			
			break;
		
		case BusinessConfigType.normal:
		default:
			
			List<String> workerL = Arrays.asList(funcs.split(","));
			
			actor.setClassnameList(workerL);
			
			f = new FuncFlow().apply(actor);
			
			break;
		
		}
		
		
		
		if (StringUtils.hasText(config_funcflow.getString("next"))) { //有后续business
			
//			if (StringUtils.hasText(business.getString("reducer")) && actor.getSolve() != null && business.getString("reducer").equals(actor.getSolve())) {   //如果reducer与actor.slove一样，说明是mapper
//				return Flux.merge(f);
//			}
			
			Record businessAfter = dao.fetch("config_funcflow", Cnd.where("id","=",config_funcflow.getString("next")).and("status", "=", 1));
			
			if (businessAfter != null) { //说明实际也找到后续的配置
				String idNext = config_funcflow.getString("next");
				
				actor.setClassname(idNext);
				actor.setInput(f);
				return this.apply(actor);
				
			} else {  //实际没有找到后续配置
				return Flux.just("没有找到后续配置：" + config_funcflow.getString("next"));
			}
			
		} else {   //没有后续business
			if (f != null) {
				//对于mapper处理后，继续接函数的情况，需要把FluxFlatMap处理成concatMap
				return new ResultFlatToBigFlux().apply(f);
//				return actor.isHasFlated() ? f : new ResultFlatToBigFlux().apply(f);
			} else if ( fgf != null) {
				return Flux.just("FuncManager执行遇到mapper缺少reducer参数");
			} else {
				return Flux.just("FuncManager执行出错啦");
			}
		}
		
	}
	
	
	

	

}
