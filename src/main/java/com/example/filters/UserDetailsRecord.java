package com.example.filters;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.commons.InParamsDb;
import com.example.functions.db.STSql;
import com.example.functions.db.normal.STSqlNormal;

import reactor.core.publisher.Flux;

public class UserDetailsRecord implements UserDetails {
	
	private Record record;
	
	public UserDetailsRecord() {
    }
	
	public UserDetailsRecord(Record record) {
		this.record = record;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
//		String[] roles = this.record.getString("roles") != null ? this.record.getString("roles").split(",") : new String[0];
//		return Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(toList());
		
		int userId = this.record.getInt("id");
		
		Sql sql = Sqls.create("SELECT role FROM role WHERE id IN (SELECT role FROM account_role WHERE status = 1 and account = $userid)");
        sql.vars().set("userid", userId);
        InParamsDb inParamsDb = InParamsDb.builder().sql(sql).build();
        
        Object jsonArray = new STSqlNormal().apply(inParamsDb);
        List<SimpleGrantedAuthority> result = ((JSONArray)jsonArray).stream().filter(Objects::nonNull).map(json -> ((JSONObject)json).getString("role")).map(SimpleGrantedAuthority::new).collect(toList());
        
        return result;
	}

	@Override
	public String getPassword() {
		
		return this.record.getString("password");
	}

	@Override
	public String getUsername() {
		
		return this.record.getString("username");
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		return true;
	}

}
