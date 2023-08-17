package com.nixagh.authservice.service;

import com.nixagh.auth.AuthRequest;
import com.nixagh.auth.AuthResponse;
import com.nixagh.auth.AuthServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {
    @Override
    public void auth(AuthRequest request, StreamObserver<AuthResponse> responseObserver) {
        String username = request.getUsername();
        String password = request.getPassword();
        if("admin".equals(username) && "admin".equals(password)) {
            AuthResponse response = AuthResponse.newBuilder()
                    .setToken("token")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onNext(null);
            responseObserver.onCompleted();
        }
    }
}
