package com.treeleaf.service;

import com.treeleaf.User;
import com.treeleaf.UserDetails;
import com.treeleaf.UserServiceGrpc;
import com.treeleaf.model.UserEntity;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import com.treeleaf.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

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
                .setUsername(user.getUsername())
                .build();
        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllUsersByGender(UserDetails request, StreamObserver<User> responseObserver) {
        List<UserEntity> users = userRepository.findAll();
        users.forEach(user -> {
            if (user.getDetails().getGender().equalsIgnoreCase(request.getGender())){
                User response = User.newBuilder()
                        .setUserId(user.getUserId())
                        .setUsername(user.getUsername())
                        .setPassword((user.getPassword()))
                        .build();
                responseObserver.onNext(response);
            }
        });
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<User> findFirstAdminUserRole(StreamObserver<User> responseObserver) {
        return new StreamObserver<>() {
            User adminUser = null;
            @Override
            public void onNext(User user) {
                UserEntity userEntity = userRepository.findById(user.getUserId()).orElseThrow();
                userEntity.getRole().forEach(role -> {
                    if(role.getRole_type().equalsIgnoreCase("Admin")){
                        adminUser = User.newBuilder()
                                .setUserId(userEntity.getUserId())
                                .setUsername(userEntity.getUsername())
                                .setPassword(userEntity.getPassword())
                                .build();
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(adminUser);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<UserDetails> getUsersByAges(StreamObserver<User> responseObserver) {
        return new StreamObserver<>() {
            final List<User> users = new ArrayList<>();
            @Override
            public void onNext(UserDetails userDetails) {
                userRepository.findAll().forEach(user -> {
                    if(user.getDetails().getAge() == userDetails.getAge()){
                        users.add(User.newBuilder()
                                .setUserId(user.getUserId())
                                .setUsername(user.getUsername())
                                .setPassword(user.getPassword())
                                .build());
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                users.forEach(responseObserver::onNext);
                responseObserver.onCompleted();
            }
        };
    }
}
