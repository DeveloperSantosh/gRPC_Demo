package com.treeleaf.service;

import com.google.protobuf.Descriptors;
import com.treeleaf.User;
import com.treeleaf.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserClientService {


    @GrpcClient("grpc-treeleaf-service")
    UserServiceGrpc.UserServiceBlockingStub synchronousClient;

    public Map<Descriptors.FieldDescriptor, Object> getUser(int user_id){
        User userRequest = User.newBuilder().setUserId(user_id).build();
        User userResponse = synchronousClient.getUser(userRequest);
        return userResponse.getAllFields();
    }
}
