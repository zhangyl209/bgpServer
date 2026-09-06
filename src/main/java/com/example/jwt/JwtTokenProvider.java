package com.example.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Flux;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.commons.InParamsDb;
import com.example.functions.db.STSql;
import com.example.functions.db.normal.STSqlNormal;

import static java.util.stream.Collectors.joining;

@Component
public class JwtTokenProvider {
	
	@Value("${funcions.jwt.saveToken}")
	public boolean saveToken;
	
    private static final String AUTHORITIES_KEY = "roles";

    private final JwtProperties jwtProperties;

    private SecretKey secretKey;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes()); //Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
    }

    public Record createToken(Authentication authentication) {
    	
    	Dao dao = com.example.nutz.DBTools.getDao();

        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Claims claims = Jwts.claims().setSubject(username);
        if (!authorities.isEmpty()) {
            claims.put(AUTHORITIES_KEY, authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(",")));
        }

        Date now = new Date();
        Date validity = new Date(now.getTime() + this.jwtProperties.getValidityInMs());
        
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(this.secretKey, SignatureAlgorithm.HS256)
                .compact();
        
        Cnd cnd = Cnd.where("username","=",username).and("status", "=", 1);
        
        if (saveToken) {
        	
        	dao.update("account",
                    Chain.make("token",token).add("create_time", now).add("expire_time", validity),
                    cnd);
        }
        
        Record userRecord = dao.fetch("account", cnd, "id,username,token");
        
        List<Record> roleRecords = dao.query("account_role", Cnd.where("account","=",userRecord.getInt("id")).and("status", "=", 1));
        
        String roles = null;
        if (roleRecords != null && roleRecords.size() > 0) {
        	roles = roleRecords.toString();
        }
        userRecord.put("roles", roles);
        
        return userRecord;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token).getBody();

        Object authoritiesClaim = claims.get(AUTHORITIES_KEY);

        Collection<? extends GrantedAuthority> authorities = authoritiesClaim == null ? AuthorityUtils.NO_AUTHORITIES
                : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims =  Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            //与数据库的token做比较，可以实现退出、重新登录等让token过期，增加了数据库压力，后续可放在redis里
            String username = claims.getBody().getSubject();
            if (!StringUtils.hasText(username)) {
            	return false;
            }
            
            Dao dao = com.example.nutz.DBTools.getDao();
            Record userRecord = dao.fetch("account", Cnd.where("username","=",username).and("status", "=", 1));
            if (!token.equals(userRecord.getString("token"))) {
            	return false;
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}