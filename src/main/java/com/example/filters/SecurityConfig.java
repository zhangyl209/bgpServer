package com.example.filters;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.alibaba.fastjson.JSONObject;
import com.example.commons.ResponseInfo;
import com.example.jwt.JwtTokenAuthenticationFilter;
import com.example.jwt.JwtTokenProvider;

import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                JwtTokenProvider tokenProvider,
                                                ReactiveAuthenticationManager reactiveAuthenticationManager) {

        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(reactiveAuthenticationManager)
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> {
		            swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		            swe.getResponse().getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		            byte[] bytes = JSONObject.toJSONString(ResponseInfo.not("UNAUTHORIZED")).getBytes(StandardCharsets.UTF_8);
		            DataBuffer buffer = swe.getResponse().bufferFactory().wrap(bytes);
		            return swe.getResponse().writeWith(Mono.just(buffer));
		//            return swe.getResponse().writeWith(Mono.just(new DefaultDataBufferFactory().wrap("UNAUTHORIZED".getBytes())));
			    })
                .accessDeniedHandler((swe, e) -> {
		            swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
		            swe.getResponse().getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		            byte[] bytes = JSONObject.toJSONString(ResponseInfo.not("FORBIDDEN")).getBytes(StandardCharsets.UTF_8);
		            DataBuffer buffer = swe.getResponse().bufferFactory().wrap(bytes);
		            return swe.getResponse().writeWith(Mono.just(buffer));
		//            return swe.getResponse().writeWith(Mono.just(new DefaultDataBufferFactory().wrap("FORBIDDEN".getBytes())));
		        })
                .and()
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(it -> it
                        .pathMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/open/b/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/auth/logout").authenticated()
//                        .pathMatchers(HttpMethod.GET, "/search/s/collection").authenticated()
                        .pathMatchers(HttpMethod.GET, "/admin").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/vben/user").hasRole("USER")
                        .pathMatchers("/**").permitAll()
                        .anyExchange().permitAll()
                )
                .addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
                .build();
    }
    
	@Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
//        return new Pbkdf2PasswordEncoder();
    }


    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(CustomUserDetailsService userDetailsService,
                                                                       PasswordEncoder passwordEncoder) {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }
}

