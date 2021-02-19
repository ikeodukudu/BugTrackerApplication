package com.stss.backend.Bugtracker.config;

import com.stss.backend.Bugtracker.controllers.CustomLoginFailureHandler;
import com.stss.backend.Bugtracker.controllers.CustomLoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Order(90)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomLoginFailureHandler loginFailureHandler;

    @Autowired
    private CustomLoginSuccessHandler loginSuccessHandler;
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/**").permitAll()
                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .usernameParameter("email")
//                .failureHandler(loginFailureHandler)
//                .successHandler(loginSuccessHandler)
//                .permitAll()
                .and()
                .httpBasic();
        /*http
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests();*/

        /*http.httpBasic().disable();
        http.cors().and().csrf().disable().authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin().disable(); // <-- this will disable the login route*/
    }
}
