package com.tivic.grpc_test.controller;

import com.tivic.grpc_test.domain.UserEntity;
import com.tivic.grpc_test.repository.UserRepository;
import com.tivic.proto.grpc_test.user.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
        List<UserEntity> list = userRepository.findAll();
        List<UserRes> userResList1 = list.stream()
                .map(user -> UserRes.newBuilder()
                            .setName(user.getName())
                            .setEmail(user.getEmail())
                            .build()
                ).toList();

        UserResList userResList = UserResList.newBuilder()
                .addAllUsers(userResList1)
                .build();

        responseObserver.onNext(userResList);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllServerStream(EmptyReq request, StreamObserver<UserRes> responseObserver) {
        List<UserEntity> list = userRepository.findAll();
        list.forEach(user -> {
            UserRes userRes = UserRes.newBuilder()
                    .setName(user.getName())
                    .setEmail(user.getEmail())
                    .build();

            responseObserver.onNext(userRes);
        });
        responseObserver.onCompleted();
    }
}
