package com.amazon.demo;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.security.NoSuchAlgorithmException;

@Configuration
public class AppConfig {
    File driverConfig = new File(System.getProperty("user.dir")+"/application.conf");

    public @Bean
    CqlSession session() throws NoSuchAlgorithmException {
        return CqlSession.builder().
                withConfigLoader(DriverConfigLoader.fromFile(driverConfig)).
                withSslContext(SSLContext.getDefault()).
                withKeyspace("keyspace_name").
                build();
    }
}
