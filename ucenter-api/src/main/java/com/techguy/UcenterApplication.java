package com.techguy;
import com.techguy.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.Entity;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EntityScan(basePackages = "com.techguy.entity")
@EnableJpaRepositories(basePackages = "com.techguy.repository")
@Slf4j
public class UcenterApplication{

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext app = SpringApplication.run(UcenterApplication.class, args);
        Environment env  = app.getEnvironment();
        String port = env.getProperty("server.port");
        String ip = InetAddress.getLocalHost().getHostAddress();

        log.info("App running,Access URL Following," +"\n\t"+
                "------------------------------->>>"+"\n\t"+
                "localhost:\thttp://localhost:"+port+"\n\t"+
                "external:\thttp://"+ip+":"+port
        );
    }

}
