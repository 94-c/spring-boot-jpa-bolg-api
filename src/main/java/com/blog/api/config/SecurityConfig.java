package com.blog.api.config;

import com.blog.api.filter.JwtFilter;
import com.blog.api.jwt.JwtAccessDeniedHandler;
import com.blog.api.jwt.JwtAuthenticationEntryPoint;
import com.blog.api.jwt.provider.TokenProvider;
import com.blog.api.jwt.provider.UserAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtFilter customFilter = new JwtFilter(tokenProvider);
        http
                .authorizeRequests()
                .antMatchers("/login")
                .permitAll()
                .antMatchers("/signup")
                .permitAll()
                .antMatchers("/confirm-email")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .disable()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder managerBuilder) throws Exception {
        managerBuilder.authenticationProvider(userAuthenticationProvider);
    }
}