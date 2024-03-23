package com.deinsoft.efacturador3.security;

import com.deinsoft.efacturador3.repository.SecUserRepository;
import com.deinsoft.efacturador3.security.web.JpaUserDetailsService;
import com.deinsoft.efacturador3.security.web.LoginSuccessHandler;
import java.util.Arrays;
import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    @Order(1)
    @Configuration
    public static class WebSecurityRest extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/api/**").csrf().disable().authorizeRequests()
                    .antMatchers("/api/v1/empresa/**").permitAll() //permitimos el acceso a /login a cualquiera
                    .antMatchers("/api/v1/local/**").permitAll() //permitimos el acceso a /login a cualquiera
                    .anyRequest().authenticated() //cualquier otra peticion requiere autenticacion
                    .and()
                    .addFilter(new JWTAuthorizationFilter(authenticationManager()));
        }

    }
    @Order(2)
    @Configuration
    public static class WebSecurityRestWeb extends WebSecurityConfigurerAdapter {

        @Autowired
	private LoginSuccessHandler successHandler;
	
	@Autowired
	private JpaUserDetailsService userDetailsService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
        @Autowired
        private SecUserRepository secUserRepository;
        
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                        //.csrf().disable().authorizeRequests()
//			.cors().configurationSource(corsConfigurationSource()).and()
			.cors().and()
			.csrf().disable()
                        .authorizeRequests().antMatchers(HttpMethod.POST,"/login","/backend").permitAll()
		/*.antMatchers("/ver/**").hasAnyRole("USER")*/
		/*.antMatchers("/uploads/**").hasAnyRole("USER")*/
		/*.antMatchers("/form/**").hasAnyRole("ADMIN")*/
		/*.antMatchers("/eliminar/**").hasAnyRole("ADMIN")*/
		/*.antMatchers("/factura/**").hasAnyRole("ADMIN")*/
		.anyRequest().authenticated()
//		.and()
//		    .formLogin()
//		        .successHandler(successHandler)
//		        .loginPage("/login")
//		    .permitAll()
		.and()
                
		.logout().permitAll()
                .and().addFilter((Filter) new JWTAuthenticationFilterWeb(authenticationManager(),secUserRepository))
					.addFilter(new JWTAuthorizationFilter(authenticationManager()))
		.exceptionHandling().accessDeniedPage("/error_403");

	}

	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception
	{
		build.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder);

	}
        @Bean
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList("http://deinsoft-la.com/","http://66.29.149.124:8080/deinsoft-cloud/",
                        "http://localhost:4200/","http://localhost:57784/","http://127.0.0.1:5500/"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
				"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}
    }
}
