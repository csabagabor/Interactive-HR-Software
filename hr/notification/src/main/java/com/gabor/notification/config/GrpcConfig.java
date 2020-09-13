package com.gabor.notification.config;

import com.gabor.LoginServiceGrpc;
import io.grpc.Channel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.SSLException;
import java.io.File;

@Configuration
public class GrpcConfig {

    @Value("${grpc.enable.ssl}")
    private boolean enableSsl;

    @Value("${grpc.port.number}")
    private int grpcPort;

    @Value("${grpc.host.name}")
    private String host;

    @Value("${grpc.cert.path}")
    private String certPath;

    @Bean
    public LoginServiceGrpc.LoginServiceBlockingStub loginServiceBlockingStub() throws Exception {
        return LoginServiceGrpc.newBlockingStub(channel());
    }

    @Bean
    public Channel channel() throws Exception {
        if (enableSsl) {
            return NettyChannelBuilder.forAddress(host, grpcPort)
                    .sslContext(GrpcSslContexts
                            .forClient()
                            .trustManager(new ClassPathResource(certPath).getInputStream())
                            .build())
                    .build();
        } else {
            return NettyChannelBuilder.forAddress(host, grpcPort)
                    .usePlaintext()
                    .build();
        }
    }
}
