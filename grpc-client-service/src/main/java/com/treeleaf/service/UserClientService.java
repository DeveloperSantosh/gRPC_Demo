package com.treeleaf.service;

import com.google.protobuf.Descriptors;
import com.treeleaf.User;
import com.treeleaf.UserDetails;
import com.treeleaf.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class UserClientService {

    @GrpcClient("grpc-treeleaf-service")
    UserServiceGrpc.UserServiceBlockingStub synchronousClient;

    @GrpcClient("grpc-treeleaf-service")
    UserServiceGrpc.UserServiceStub asynchronousClient;

    public Map<Descriptors.FieldDescriptor, Object> getUser(int user_id){
        User userRequest = User.newBuilder().setUserId(user_id).build();
        User userResponse = synchronousClient.getUser(userRequest);
        return userResponse.getAllFields();
    }

    public List<Map<Descriptors.FieldDescriptor, Object>> getAllUsersByGender(String gender) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        List<Map<Descriptors.FieldDescriptor, Object>> userResponseList = new ArrayList<>();
        UserDetails request = UserDetails.newBuilder().setGender(gender).build();
        asynchronousClient.getAllUsersByGender(request, new StreamObserver<>() {
            @Override
            public void onNext(User user) {
                userResponseList.add(user.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });
        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? userResponseList : Collections.emptyList();
    }

    public Map<String,Map<Descriptors.FieldDescriptor, Object>> findFirstAdminUserRole(List<Integer> userIdList) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Map<String, Map<Descriptors.FieldDescriptor, Object>> response = new HashMap<>();
        StreamObserver<User> responseObserver = asynchronousClient.findFirstAdminUserRole(new StreamObserver<>() {
            @Override
            public void onNext(User user) {
                response.put("ADMIN USER", user.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });
        userIdList.forEach(id -> {
            User user = User.newBuilder()
                    .setUserId(id)
                    .build();
            responseObserver.onNext(user);
        });
        responseObserver.onCompleted();
        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyMap();
    }

    public List<Map<Descriptors.FieldDescriptor, Object>> getUserByAge(int []ages) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();
        StreamObserver<UserDetails> responseObserver = asynchronousClient.getUsersByAges(new StreamObserver<>() {
            @Override
            public void onNext(User user) {
                response.add(user.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });
        for(int age: ages){
            UserDetails details = UserDetails.newBuilder().setAge(age).build();
            responseObserver.onNext(details);
        }
        responseObserver.onCompleted();
        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyList();
    }

}
