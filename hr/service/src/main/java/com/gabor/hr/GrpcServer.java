package com.gabor.hr;

import com.gabor.hr.service.grpc.LoginImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Configuration
@Profile("!test")
public class GrpcServer {
    @Value("${grpc.enable.ssl}")
    private boolean enableSsl;

    @Value("${grpc.port.number}")
    private int grpcPort;

    @Value("${grpc.cert.path}")
    private String certPath;

    @Value("${grpc.private.path}")
    private String privatePath;

    @Bean
    public LoginImpl login() {
        return new LoginImpl();
    }

    @PostConstruct
    public void startGrpcServer() throws Exception {

        Server server;

        if (enableSsl) {
            server = ServerBuilder.forPort(grpcPort)
                    .addService(login())
                    //enable TLS
                    .useTransportSecurity(
                            new ClassPathResource(certPath).getInputStream(),
                            new ClassPathResource(privatePath).getInputStream())
                    .build();
        } else {
            server = ServerBuilder.forPort(grpcPort)
                    .addService(login())
                    .build();
        }

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
    }
}


