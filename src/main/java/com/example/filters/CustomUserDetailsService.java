package com.example.filters;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.nutz.DBTools;

import reactor.core.publisher.Mono;

@Component
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    public CustomUserDetailsService() {
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
    	  	
    	Dao dao = DBTools.getDao();
    	
    	List<Record> list = dao.query("account", Cnd.where("username","=",username).and("status", "=", 1));
    	
    	if (list != null && list.size() == 1 ) {
    		UserDetailsRecord userDetailsRecord = new UserDetailsRecord(list.get(0));
    		return Mono.just(userDetailsRecord);
    	} else {
    		throw new UsernameNotFoundException("Username: " + username + " not found");
    	}
    }
}