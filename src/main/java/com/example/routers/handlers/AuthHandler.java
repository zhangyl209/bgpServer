package com.example.routers.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.commons.InParamsDb;
import com.example.commons.ResponseInfo;
import com.example.functions.db.normal.STSqlNormal;
import com.example.jwt.JwtTokenProvider;
import com.example.nutz.DBTools;

import reactor.core.publisher.Mono;

@Component
public class AuthHandler {
	
	@Autowired
    ReactiveAuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    
    private final Mono<SecurityContext> context  = ReactiveSecurityContextHolder.getContext();
    
    private Dao dao = DBTools.getDao();
    
    private Mono<User> extractUserSeqIdFromJwtToken(Mono<SecurityContext> context) {
        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
          .map(s -> s.getAuthentication().getPrincipal())
          .cast(User.class);
      }

	public Mono<ServerResponse> login(ServerRequest request) {
		
		return request.bodyToMono(Record.class).flatMap(userRecord -> {
			String username = userRecord.getString("username");
			String password = userRecord.getString("password");
			
	    	List<Record> list = dao.query("account", Cnd.where("username","=",username).and("password", "=", password).and("status", "=", 1));
	    	
	    	if (list != null && list.size() == 1 ) {
	    		Mono<Authentication> authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	    		
	    		int userId = list.get(0).getInt("id");
	    		//记录登录次数
	    		Record record = dao.fetch("log_account_login", Cnd.where("status", "=", 1).and("account","=",userId));
	    		if (record == null) {
	    			dao.insert("log_account_login", Chain.make("account", userId).add("count", 1));
	    		} else {
	    			long count = record.getLong("count");
	    			dao.update("log_account_login", Chain.make("count",count + 1), Cnd.where("status", "=", 1).and("account","=",userId));
	    		}
	    		
	    		return ServerResponse.ok().body(authentication.map(auth -> ResponseInfo.ok(jwtTokenProvider.createToken(auth))), ResponseInfo.class);
	    	} else {
				return ServerResponse.ok().body(Mono.just(ResponseInfo.not("用户名不存在，或者密码错误")), ResponseInfo.class);
	    	}
		});
    	
    }
	
	public Mono<ServerResponse> logout(ServerRequest request) {
		
		return extractUserSeqIdFromJwtToken(context).flatMap(user -> {
			String username = user.getUsername();
			
			dao.update("account",
                    Chain.make("token",null).add("expire_time", new Date()),
                    Cnd.where("username","=",username).and("status", "=", 1));
			
			return ServerResponse.ok().body(Mono.just(ResponseInfo.ok(username + " logout")), ResponseInfo.class);
		});
    	
    }
	
	public Mono<ServerResponse> getUserInfo(ServerRequest request) {
		
		return extractUserSeqIdFromJwtToken(context).flatMap(user -> {
			String username = user.getUsername();
			
			Record record = dao.fetch("account",
                    Cnd.where("username","=",username).and("status", "=", 1),
                    "id,username,realname,avatar,remarks");
			
			Sql sql = Sqls.create("SELECT t1.id, t1.role, t1.rolename FROM role t1 INNER JOIN account_role t2 " + 
									"WHERE t1.id = t2.role AND t2.account = '$accountid' AND t1.status = 1 AND t2.status = 1");
			sql.vars().set("accountid", record.getInt("id"));
	        InParamsDb inParamsDb = InParamsDb.builder().sql(sql).build();
	        
	        Object result = new STSqlNormal().apply(inParamsDb);
	        
	        JSONArray roleList  = result != null ? (JSONArray)result : null;
	        
	        JSONArray roleArray = new JSONArray();
	        roleList.stream().forEach(roleObj->{
	        	JSONObject jb = new JSONObject();
	        	jb.put("roleName", ((JSONObject)roleObj).get("rolename"));
	            jb.put("value", ((JSONObject)roleObj).get("role"));
	            roleArray.add(jb);
	        });

			JSONObject userInfo = new JSONObject();
			userInfo.put("roles", roleArray);
			userInfo.put("userId", record.getInt("id"));
			userInfo.put("username", record.getString("username"));
			userInfo.put("realName", record.getString("realname"));
			userInfo.put("avatar", record.getString("avatar"));
			userInfo.put("desc", record.getString("remarks"));
			
			return ServerResponse.ok().body(Mono.just(ResponseInfo.ok(userInfo)), ResponseInfo.class);
		});
    	
    }
	
	public Mono<ServerResponse> getPermCode(ServerRequest request) {
		
		return extractUserSeqIdFromJwtToken(context).flatMap(user -> {
			String username = user.getUsername();
			
			Record record = dao.fetch("account",
                    Cnd.where("username","=",username).and("status", "=", 1));
			
			Sql sql = Sqls.create("SELECT t1.permcode FROM role t1 INNER JOIN account_role t2 " + 
					"WHERE t1.id = t2.role AND t2.account = '$accountid' AND t1.status = 1 AND t2.status = 1");
			sql.vars().set("accountid", record.getInt("id"));
	        InParamsDb inParamsDb = InParamsDb.builder().sql(sql).build();
	        
	        Object result = new STSqlNormal().apply(inParamsDb);
	        
	        JSONArray roleList  = result != null ? (JSONArray)result : null;
	        
	        List<String> permcodeList = new ArrayList<String>();
	        roleList.stream().forEach(roleObj->{
	        	String[] permcodes = ((JSONObject)roleObj).get("permcode") != null ? ((JSONObject)roleObj).get("permcode").toString().split(",") : new String[]{};
	        	for (int i = 0; i<permcodes.length; i++) {
	        		if (!permcodeList.contains(permcodes[i])) {
	        			permcodeList.add(permcodes[i]);
	        		}
	        	}
	        });
			
			return ServerResponse.ok().body(Mono.just(ResponseInfo.ok(permcodeList)), ResponseInfo.class);
		});
    	
    }
	

}
