package com.techguy.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techguy.constant.CommonConstant;
import com.techguy.entity.Member;
import com.techguy.entity.admin.Admin;
import com.techguy.service.AdminService;
import com.techguy.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Objects;

@Component
@Slf4j

public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private MemberService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder  encoder;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String  secrectkey= "ima34rw3r3wrsdfsefesfd324324324*&^$%w#";
    private static final String adminKey="kzieksikeiskkeiskeiksieiskeisiekiskeksie";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/admin/api/login", request.getServletPath());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authorization = httpServletRequest.getHeader("Authorization");
        String admin = httpServletRequest.getHeader("Admin");

        String token = null;
        String userName = null;
        if(null != authorization && !authorization.isEmpty()) {
            token = authorization.replace("Bearer ","");
            try {
                userName = jwtUtility.getUsernameFromToken(token);
                if(null != userName && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if(admin!=null && admin.equals(adminKey)){
                        Admin sysAdmin = adminService.findByEmail(userName);
                        if(encoder.matches(secrectkey,sysAdmin.getSecrectKey())){

                            UserDetails userDetails
                                    = adminService.loadUserByUsername(userName);

                            if(jwtUtility.validateToken(token,userDetails)) {
                                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                                        = new UsernamePasswordAuthenticationToken(userDetails,
                                        null, userDetails.getAuthorities());

                                usernamePasswordAuthenticationToken.setDetails(
                                        new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
                                );

                                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                            }
                        }
                    }
                    else {
                        Member user = userService.findByEmail(userName);
                        if(token.equals(user.getToken())) {
                            UserDetails userDetails
                                    = userService.loadUserByUsername(userName);
                            if (jwtUtility.validateToken(token, userDetails)) {
                                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                                        = new UsernamePasswordAuthenticationToken(userDetails,
                                        null, userDetails.getAuthorities());

                                usernamePasswordAuthenticationToken.setDetails(
                                        new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
                                );
                                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                            }
                        }
                        else {
                            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            return;
                        }
                    }
                }
            } catch (JwtException e){
                httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
