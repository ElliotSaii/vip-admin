package com.techguy.security;
import com.techguy.jwt.JwtFilter;
import com.techguy.service.impl.MemberServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled  = true)
@AllArgsConstructor

public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    private static final String SIGN_UP ="/api/member/register1",SIGN_IN="/api/member/login",CAPTCHA_CODE="/api/sys/randomImage/**",
//            FORGOT_PW="/api/sys/forgetpassword",SEND_MAIL="/api/sys/sendMail";
//    UPLOAD_IMG="/api/sys/upload";

//    private static final String ADMIN_SIGN_UP ="/admin/api/register",ADMIN_SIGN_IN="/admin/api/login";
//
//    private static  final  String HONOR_LIST= "/api/honor/list",BANNER_IMAGE="/api/banner/list";
//
//    private static final String GUIDE_LIST= "/api/guide/list",SUB_GUIDE_LIST="/api/guide/sub/list";
//
//    private static final String APP_VERSION = "/api/app/version";

    private static final String [] noFilterApi= new String[]{"/api/member/register1","/api/member/login","/api/sys/randomImage/**","/api/sys/forgetpassword","/api/sys/sendMail",
    "/admin/api/register","/admin/api/login","/api/honor/list","/api/banner/list","/api/guide/list","/api/guide/sub/list","/api/app/version"};


    private final  MemberServiceImpl memberService;
    private final   PasswordEncoder passwordEncoder;
    private final     JwtFilter jwtFilter;


 /*.antMatchers("/admin/api").authenticated()
                               .antMatchers("/**")
                               .permitAll()*/


    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(noFilterApi)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


    }


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());

    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(memberService);

        return provider;
    }

}
