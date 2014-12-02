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
#include <SFML/Network.hpp>

#include "TcpSocket.h"
#include "TcpMessage.h"

using namespace Anvil;
using namespace Anvil::Socket;
using Thilenius::BFEProtos::BFEMessage;
using Thilenius::BFEProtos::BFELoadLevelRequest;
using Thilenius::BFEProtos::BFELoadLevelResponse;
using Thilenius::BFEProtos::BFESparkCommand;
using Thilenius::BFEProtos::BFESparkResponse;
using Thilenius::BFEProtos::BFESparkCommand_CommandType;

void doMinecraftThingy() {
    
    
    // Client Code Sample:
    // Level level = Anvil.getLevel( levelNumber )
    
    
    // Dictionary<UserUUID, User>
    
    // class User:
    //   FirstName, LastName, StudentID, ...
    //   Dictionary<AssignmentDescription, AssignmentStatus>
    //   Dictionary<AssignmentDescription, RecordedAssignemtRuns>
    
    
    // class AssignmentDescription:
    //   Name, Description, Due Date, ...
    //   Download ZIP path (OSX and WIN)
    //   JarPath
    //   Dictionary<LevelID, Level>
    
    // class Level:
    //   Name, Desciption, ...
    //   Points Possible
    
    
    // class AssignmentStatus:
    //   AuthToken
    //   Dictionary<LevelID, LevelStatus>
    
    // class RecordedAssignemtRuns:
    //   Dictionary<LevelID, RecordedLevelRuns>
    
    // class RecordedLevelRuns:
    //   List<RunList>
    
    
    
    //=User
    // user_meta:all
    // user:<CU ID>:FirstName
    // user:<CU ID>:LastName
    // user:<CU ID>:StudentID
    // user:<CU ID>:Permissions
    
    //=Minecraft User [1:1 mapping with User, in Minecraft envinroment]
    // minecraft_user:<CU ID>:Allocated MC Account Username
    // minecraft_user:<CU ID>:Spawn Location
    
    //=Minecraft Account
    // minecraft_account_meta:all
    // minecraft_account:<account UUID>:login
    // minecraft_account:<account UUID>:password
    // minecraft_account:<account UUID>:client_token
    // minecraft_account:<account UUID>:state [allocated/free/suspended/private]
    
    //=AssignmentDescription
    // assignment_description_meta:all
    // assignment_description:<assignment name>:Descritpion Markdown
    // assignment_description:<assignment name>:Due Date
    // assignment_description:<assignment name>:Download ZIP Path
    // assignment_description:<assignment name>:Jar Path
    // assignment_description:<assignment name>:Levels [SET]
    //=LevelDescription
    // level_description:<assignment name>_<level name>:Description
    // level_description:<assignment name>_<level name>:Possible Points
    
    
    //=UserAssignment
    // UserAssignment:<CU ID>_<assignment name>:auth_token
    //=UserLevel
    // userLevel:<CU ID>_<assignment name>_<level name>:IsFinished
    
    
    
    std::string authToken = "c0dba92c-6449-40a6-95da-11d2b7d28ad6";
    
    // Anvil.Authenticate( "authToken" [from Forge] );
    // BlazeLevel level = Anvil.GetLevel( level = 2, submit = false );
    TcpSocket socket;
    std::cout << "Trying to connect" << std::endl;
    if (!socket.Connect("127.0.0.0", 5529)) {
        std::cout << "Failed to connect to localhost on port 5529." << std::endl;
        return;
    }
    
//    // Write out [NOT USED]
//    {
//        BFEMessage message;
//        BFEAuthRequest* request = message.MutableExtension(BFEAuthRequest::BFEAuthRequest_ext);
//        request->set_username("athilenius");
//        request->set_password("qwe123");
//        message.PrintDebugString();
//        request->PrintDebugString();
//        
//        int size = message.ByteSize();
//        void *buffer = malloc(size);
//        message.SerializeToArray(buffer, size);
//        
//        // Send test request
//        if (!socket.Write(buffer, size)) {
//            std::cout << "Failed to send test data" << std::endl;
//            return;
//        }
//    }
//    
//    // Read back [NOT USED]
//    {
//        TcpMessagePtr response = socket.Read();
//        if (response == nullptr) {
//            std::cout << "Failed to recieve response" << std::endl;
//            return;
//        }
//        
//        BFEMessage responseMessage;
//        responseMessage.ParseFromArray(response->Data, response->Count);
//        
//        if (responseMessage.HasExtension(BFEAuthResponse::BFEAuthResponse_ext)) {
//            BFEAuthResponse response = responseMessage.GetExtension(BFEAuthResponse::BFEAuthResponse_ext);
//            std::cout << "Got AUTH response: " << response.DebugString() << std::endl;
//            authToken = response.auth_token();
//        }
//    }
    
    // Test Assignment
    {
        BFEMessage message;
        BFELoadLevelRequest* request = message.MutableExtension(BFELoadLevelRequest::BFELoadLevelRequest_ext);
        request->set_auth_token(authToken);
        request->set_levelnumber(1);
        request->set_seed(1234);
        
        int size = message.ByteSize();
        void* buffer = malloc(size);
        message.SerializeToArray(buffer, size);
        
        if (!socket.Write(buffer, size)) {
            std::cout << "Failed to send load level request" << std::endl;
            return;
        }
    }
    
    // Get Level response
    {
        TcpMessagePtr response = socket.Read();
        if (response == nullptr) {
            std::cout << "Failed to get response from server for LoadLevelRequest" << std::endl;
            return;
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
                return;
            }
        }
        
        // Pull response
        {
            TcpMessagePtr response = socket.Read();
            if (response == nullptr) {
                std::cout << "Failed to get response from server for Spark Command" << std::endl;
                return;
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

void doHttpThingy() {
    sf::Http http ("http://localhost/", 3000);
    sf::Http::Request request;
    
    request.setMethod(sf::Http::Request::Get);
    request.setUri("/");
    request.setHttpVersion(1, 1);
    
    sf::Http::Response response = http.sendRequest(request);
    std::cout << "status: " << response.getStatus() << std::endl;
    std::cout << "HTTP version: " << response.getMajorHttpVersion() << "." << response.getMinorHttpVersion() << std::endl;
    std::cout << "Content-Type header:" << response.getField("Content-Type") << std::endl;
    std::cout << "body: " << response.getBody() << std::endl;
}

int main(int argc, const char * argv[]) {
    //doHttpThingy();
    doMinecraftThingy();
}