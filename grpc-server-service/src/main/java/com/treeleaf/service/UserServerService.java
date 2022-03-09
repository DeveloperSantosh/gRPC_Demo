package com.treeleaf.service;

import com.treeleaf.User;
import com.treeleaf.UserServiceGrpc;
import com.treeleaf.model.UserEntity;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import com.treeleaf.repository.UserRepository;

@GrpcService
public class UserServerService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    UserRepository userRepository;

    @Override
    public void getUser(User request, StreamObserver<User> responseObserver) {
        UserEntity user = userRepository.findById(request.getUserId()).orElseThrow();
        User userResponse = User.newBuilder()
                .setUserId(user.getUserId())
                .setPassword(user.getPassword())
                .setUserId(user.getUserId())
                .build();
        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }
}
