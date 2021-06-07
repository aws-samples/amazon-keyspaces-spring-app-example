package com.aws.mcs.springsample;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.security.NoSuchAlgorithmException;

@Configuration
public class AppConfig {
    private final String username = System.getenv("AWS_MCS_SPRING_APP_USERNAME");
    private final String password = System.getenv("AWS_MCS_SPRING_APP_PASSWORD");
    File driverConfig = new File(System.getProperty("user.dir")+"/application.conf");

    @Primary
    public @Bean
    CqlSession session() throws NoSuchAlgorithmException {
        return CqlSession.builder().
                withConfigLoader(DriverConfigLoader.fromFile(driverConfig)).
                withAuthCredentials(username, password).
                withSslContext(SSLContext.getDefault()).
                withKeyspace("keyspace_name").
                build();
    }
}
