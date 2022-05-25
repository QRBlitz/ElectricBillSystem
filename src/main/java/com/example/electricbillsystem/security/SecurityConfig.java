package com.example.electricbillsystem.security;

import com.example.electricbillsystem.security.filter.CustomAuthenticationFilter;
import com.example.electricbillsystem.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/chat").permitAll();
        http.authorizeRequests().antMatchers("/customer/delete").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/address/delete").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/billing/delete").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/billing/add").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/credit/delete").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/debit/delete").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/electric/**").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/employee/**").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/invoice/**").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/paypal/delete").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/tariff/**").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/user").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.cors();
//        http.headers().xssProtection().and().contentSecurityPolicy("script-src 'self'");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
