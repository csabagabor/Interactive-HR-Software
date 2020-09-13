package com.gabor.hr.config;

import com.gabor.hr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebConfig extends WebSecurityConfigurerAdapter {

    @Resource(name = "userService")
    private UserService userDetailsService;

    @Autowired
    private JwtErrorHandler unauthorizedHandler;

    @Autowired
    private TokenProvider jwtTokenUtil;

    /*
        super method does not create a bean for AuthenticationManager
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.cors();

        http.authorizeRequests()
                .antMatchers("/api/users/**").access("hasRole('ADMIN')")
                .antMatchers("/api/roles/**").access("hasRole('ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/cities/**").access("hasRole('MODERATOR')")
                .antMatchers(HttpMethod.PUT, "/api/cities/**").access("hasRole('MODERATOR')")
                .antMatchers(HttpMethod.DELETE, "/api/cities/**").access("hasRole('MODERATOR')")
                .antMatchers(HttpMethod.POST, "/api/countries/**").access("hasRole('MODERATOR')")
                .antMatchers(HttpMethod.PUT, "/api/countries/**").access("hasRole('MODERATOR')")
                .antMatchers(HttpMethod.DELETE, "/api/countries/**").access("hasRole('MODERATOR')")
                .antMatchers(HttpMethod.POST, "/api/transportation-means/**").access("hasRole('MODERATOR')")
                .antMatchers(HttpMethod.PUT, "/api/transportation-means/**").access("hasRole('MODERATOR')")
                .antMatchers(HttpMethod.DELETE, "/api/transportation-means/**").access("hasRole('MODERATOR')")
                .antMatchers(HttpMethod.GET, "/api/requests/open").access("hasRole('MODERATOR')")
                .antMatchers(HttpMethod.GET, "/api/requests/rejected").access("hasRole('MODERATOR')")
                .antMatchers(HttpMethod.GET, "/api/requests/accepted").access("hasRole('MODERATOR')")
                .antMatchers(HttpMethod.GET, "/api/requests").access("hasRole('MODERATOR')")
                .antMatchers(HttpMethod.GET, "/api/requests/own").access("hasRole('USER')")
                .antMatchers(HttpMethod.GET, "/api/requests/{id[\\d+]}").access("hasRole('MODERATOR')")
                .antMatchers(HttpMethod.PUT, "/api/requests/**").access("hasRole('MODERATOR')")
                .antMatchers("/api/metrics/**").access("hasRole('MODERATOR')")

                .antMatchers(HttpMethod.GET,"/api/tasks/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/tasks/**").access("hasRole('PM')")
                .antMatchers(HttpMethod.PUT,"/api/tasks/**").access("hasRole('PM')")
                .antMatchers(HttpMethod.DELETE,"/api/tasks/**").access("hasRole('PM')")

                .antMatchers(HttpMethod.GET,"/api/projects/").permitAll()
                .antMatchers(HttpMethod.POST,"/api/projects/").access("hasRole('MODERATOR')")
                .antMatchers(HttpMethod.PUT,"/api/projects/").access("hasRole('MODERATOR')")
                .antMatchers(HttpMethod.DELETE,"/api/projects/").access("hasRole('MODERATOR')")

                .antMatchers("/api/timecards/accept/**").access("hasRole('PM')")
                .antMatchers("/api/timecards/reject/**").access("hasRole('PM')")
                .antMatchers("/api/timecards/open").access("hasRole('PM')")
                .antMatchers("/api/timecards/tasks/**").access("hasRole('PM') || hasRole('PAYROLL')")
                .antMatchers("/api/timecards/tasks-payroll/**").access("hasRole('PAYROLL')")
                .antMatchers("/api/timecards/past-month").access("hasRole('PM') || hasRole('PAYROLL')")
                .antMatchers("/api/timecards/accepted").access("hasRole('PM') || hasRole('PAYROLL')")

                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/commands/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        //do not create a bean with this, because it will be automatically added to the Spring filter chain and then
        //paths cannot be excluded
        http.addFilterBefore(new JwtFilter(userDetailsService, jwtTokenUtil),
                UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/auth/**");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
