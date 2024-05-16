package com.tivic.grpc_test.controller;

import com.tivic.grpc_test.domain.UserEntity;
import com.tivic.grpc_test.repository.UserRepository;
import com.tivic.proto.grpc_test.user.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class UserController extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void create(UserReq request, StreamObserver<UserRes> responseObserver) {
        var user = new UserEntity(request.getName(),request.getEmail());
        var savedUser = userRepository.save(user);
        responseObserver.onNext(
                UserRes.newBuilder()
                        .setName(savedUser.getName())
                        .setEmail(savedUser.getEmail())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getAll(EmptyReq request, StreamObserver<UserResList> responseObserver) {
        super.getAll(request, responseObserver);
    }

    @Override
    public void getAllServerStream(EmptyReq request, StreamObserver<UserRes> responseObserver) {
        super.getAllServerStream(request, responseObserver);
    }
}
