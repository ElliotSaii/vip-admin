package com.techguy.aspect;

import com.techguy.config.LocaleMessageSourceService;
import com.techguy.constant.SysConstant;
import com.techguy.entity.LoginAttempt;
import com.techguy.service.LoginAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class AntiAttackAspect {
    @Autowired
   private   RedisTemplate redisTemplate;
    @Resource
   private LocaleMessageSourceService messageSourceService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    private ThreadLocal<Long> startTime= new ThreadLocal<>();

    @Pointcut("execution(public * com.techguy.admin.controller.AdminLoginController.login(..))")
    public void antiAttack(){

    }
    @Before("antiAttack()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {

        log.info("❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");
        check(joinPoint);
    }
    public void check(JoinPoint joinPoint) throws Exception {

        startTime.set(System.currentTimeMillis());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        ValueOperations valueOperations = redisTemplate.opsForValue();
        String key = SysConstant.ANTI_ATTACK_ + request.getSession().getId();


        Object code = valueOperations.get(key);
        if (code != null) {
            String ipAddress = request.getRemoteAddr();
            LoginAttempt attempt = loginAttemptService.findByIpAddress(ipAddress);
            attempt.setAttemptTime(0);
            loginAttemptService.update(attempt);

            throw new IllegalArgumentException(messageSourceService.getMessage("FREQUENTLY_REQUEST"));
        }
    }

    @AfterReturning(pointcut = "antiAttack()")
    public void doAfterReturning() throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String key = SysConstant.ANTI_ATTACK_ + request.getSession().getId();
        ValueOperations valueOperations = redisTemplate.opsForValue();

        String ipAddress = request.getRemoteAddr();
        LoginAttempt attempt= loginAttemptService.findByIpAddress(ipAddress);

        log.info("ip : {} ",ipAddress);

        if( attempt.getAttemptTime()>=5){
            valueOperations.set(key,"Frequently Request" , 10, TimeUnit.MINUTES);
        }
        attempt.setAttemptTime(attempt.getAttemptTime()+1);
        loginAttemptService.update(attempt);

        log.info("processing time：" + (System.currentTimeMillis() - startTime.get()) + "ms");
        log.info("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
        startTime.remove();
    }
}
