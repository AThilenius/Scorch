//
//  main.cpp
//  Anvil
//
//  Created by Alec Thilenius on 11/13/14.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#include "PrefixHeader.h"

#include <iostream>
#include "BFEProtos.pb.h"

#include "TcpSocket.h"
#include "TcpMessage.h"

using namespace Anvil;
using namespace Anvil::Socket;
using Thilenius::BFEProtos::BFEMessage;
using Thilenius::BFEProtos::BFEAuthRequest;
using Thilenius::BFEProtos::BFEAuthResponse;
using Thilenius::BFEProtos::BFELoadLevelRequest;
using Thilenius::BFEProtos::BFELoadLevelResponse;
using Thilenius::BFEProtos::BFESparkCommand;
using Thilenius::BFEProtos::BFESparkResponse;
using Thilenius::BFEProtos::BFESparkCommand_CommandType;

int main(int argc, const char * argv[]) {
    
    std::string authToken;

    TcpSocket socket;
    if (!socket.Connect("127.0.0.0", 5529)) {
        std::cout << "Failed to connect to localhost on port 5529." << std::endl;
        return 0;
    }
    
    // Write out
    {
        BFEMessage message;
        BFEAuthRequest* request = message.MutableExtension(BFEAuthRequest::BFEAuthRequest_ext);
        request->set_username("athilenius");
        request->set_password("1234");
        message.PrintDebugString();
        request->PrintDebugString();
        
        int size = message.ByteSize();
        void *buffer = malloc(size);
        message.SerializeToArray(buffer, size);
    
        // Send test request
        if (!socket.Write(buffer, size)) {
            std::cout << "Failed to send test data" << std::endl;
            return 0;
        }
    }
    
    // Read back
    {
        TcpMessagePtr response = socket.Read();
        if (response == nullptr) {
            std::cout << "Failed to recieve response" << std::endl;
            return 0;
        }
        
        BFEMessage responseMessage;
        responseMessage.ParseFromArray(response->Data, response->Count);

        if (responseMessage.HasExtension(BFEAuthResponse::BFEAuthResponse_ext)) {
            BFEAuthResponse response = responseMessage.GetExtension(BFEAuthResponse::BFEAuthResponse_ext);
            std::cout << "Got AUTH response: " << response.DebugString() << std::endl;
            authToken = response.auth_token();
        }
    }
    
    // Test Assignment
    {
        BFEMessage message;
        BFELoadLevelRequest* request = message.MutableExtension(BFELoadLevelRequest::BFELoadLevelRequest_ext);
        request->set_auth_token(authToken);
        request->set_assignmentname("com.thilenius.blaze.assignment.demo.DemoAssignment");
        request->set_levelnumber(0);
        request->set_seed(1234);
        
        int size = message.ByteSize();
        void* buffer = malloc(size);
        message.SerializeToArray(buffer, size);
        
        if (!socket.Write(buffer, size)) {
            std::cout << "Failed to send load level request" << std::endl;
            return 0;
        }
    }
    
    // Get Level response
    {
        TcpMessagePtr response = socket.Read();
        if (response == nullptr) {
            std::cout << "Failed to get response from server for LoadLevelRequest" << std::endl;
            return 0;
        }
        
        BFEMessage message;
        message.ParseFromArray(response->Data, response->Count);
        
        if (message.HasExtension(BFELoadLevelResponse::BFELoadLevelResponse_ext)) {
            BFELoadLevelResponse response = message.GetExtension(BFELoadLevelResponse::BFELoadLevelResponse_ext);
            std::cout << "Got LOAD response: " << response.DebugString() << std::endl;
        }
    }
    
    while (true) {
        std::string input;
        getline(std::cin, input);
        
        BFESparkCommand_CommandType command;
        
        if (input == "f") {
            command = BFESparkCommand_CommandType::BFESparkCommand_CommandType_MOVE_FORWARD;
        } else if (input == "b") {
            command = BFESparkCommand_CommandType::BFESparkCommand_CommandType_MOVE_BACKWARD;
        } else if (input == "u") {
            command = BFESparkCommand_CommandType::BFESparkCommand_CommandType_MOVE_UP;
        } else if (input == "d") {
            command = BFESparkCommand_CommandType::BFESparkCommand_CommandType_MOVE_DOWN;
        } else if (input == "tl") {
            command = BFESparkCommand_CommandType::BFESparkCommand_CommandType_TURN_LEFT;
        } else if (input == "tr") {
            command = BFESparkCommand_CommandType::BFESparkCommand_CommandType_TURN_RIGHT;
        } else {
            std::cout << "Invalid command." << std::endl;
            continue;
        }
        
        // dispatch
        {
            BFEMessage message;
            BFESparkCommand* request = message.MutableExtension(BFESparkCommand::BFESparkCommand_ext);
            request->set_command(command);
            request->set_spark_id(0);
            request->set_auth_token(authToken);
            
            int size = message.ByteSize();
            void* buffer = malloc(size);
            message.SerializeToArray(buffer, size);
            
            if (!socket.Write(buffer, size)) {
                std::cout << "Failed to send spark command request" << std::endl;
                return 0;
            }
        }
        
        // Pull response
        {
            TcpMessagePtr response = socket.Read();
            if (response == nullptr) {
                std::cout << "Failed to get response from server for Spark Command" << std::endl;
                return 0;
            }
            
            BFEMessage message;
            message.ParseFromArray(response->Data, response->Count);
            
            if (message.HasExtension(BFESparkResponse::BFESparkResponse_ext)) {
                BFESparkResponse response = message.GetExtension(BFESparkResponse::BFESparkResponse_ext);
                std::cout << "Got SPARK response: " << response.DebugString() << std::endl;
            }
        }
    }
    
    
}
