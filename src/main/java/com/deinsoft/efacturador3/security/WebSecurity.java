package com.deinsoft.efacturador3.security;

import javax.servlet.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
            .antMatchers("/empresa/save").permitAll() //permitimos el acceso a /login a cualquiera
            .antMatchers("/login").permitAll() //permitimos el acceso a /login a cualquiera
            .anyRequest().authenticated() //cualquier otra peticion requiere autenticacion
            .and()
            // Las peticiones /login pasaran previamente por este filtro
//            .addFilterBefore(new LoginFilter("/empresa/save", authenticationManager()),
//                    UsernamePasswordAuthenticationFilter.class)
//
//            // Las demás peticiones pasarán por este filtro para validar el token
//            .addFilterBefore(new JwtFilter(),
//                    UsernamePasswordAuthenticationFilter.class);
            .addFilter((Filter) new JWTAuthenticationFilter(authenticationManager()))
	    .addFilter(new JWTAuthorizationFilter(authenticationManager()));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Creamos una cuenta de usuario por default
        auth.inMemoryAuthentication()
                .withUser("ask")
                .password("123")
                .roles("ADMIN");
    }
}