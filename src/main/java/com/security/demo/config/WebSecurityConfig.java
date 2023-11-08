package com.security.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



/*
 * Exemplo de autenticação na memória (In Memory Authentication)
 * Não está usando a herança do "WebSecurityConfigurerAdapter por que ele está obsoleto,
 * então os métodos abaixo são Beans que devem suprir o comportamento esperado dessa
 * classe de configuração de autenticação
 * */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig  {
	
	@Autowired
	private SecurityDatabaseService securityService;
	
	
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityService).passwordEncoder(passwordEncoder());
    }
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers(HttpMethod.POST, "/login").permitAll()
        .antMatchers("/managers").hasAnyRole("MANAGERS")
        .antMatchers("/users").hasAnyRole("USERS","MANAGERS")
        .anyRequest().authenticated().and().httpBasic();
        //.anyRequest().authenticated().and().formLogin();        
        
        return http.build();
    }
	
	/*
	 * 
	 @Bean
    public InMemoryUserDetailsManager userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
	    manager.createUser(User.withUsername("user")
	      .password(passwordEncoder().encode("user123"))
	      .roles("USERS")
	      .build());
	    manager.createUser(User.withUsername("admin")
	      .password(passwordEncoder().encode("master123"))
	      .roles("MANAGERS")
	      .build());
	    return manager;
    }
	
	 * */
	
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }	

}