package com.gabor.hr.service.grpc;

import com.gabor.LoginRequest;
import com.gabor.LoginResponse;
import com.gabor.LoginServiceGrpc;
import com.gabor.hr.config.TokenProvider;
import com.gabor.model.dto.Invalid;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
public class LoginImpl extends LoginServiceGrpc.LoginServiceImplBase {

    @Autowired
    private TokenProvider tokenProvider;

    @Override
    public void login(final LoginRequest loginUser, final StreamObserver<LoginResponse> responseObserver) {
        String roleFromToken, email;

        try {
            roleFromToken = tokenProvider.getRoleFromToken(loginUser.getToken());
            email = tokenProvider.getEmailFromToken(loginUser.getToken());
        } catch (Exception e) {
            roleFromToken = Invalid.INVALID.name();
            email = "";
            log.warn("grpc call {}", e.getMessage());
        }

        final LoginResponse loginResponse = LoginResponse
                .newBuilder()
                .setRole(roleFromToken)
                .setEmail(email)
                .build();
        responseObserver.onNext(loginResponse);
        responseObserver.onCompleted();
    }
}
