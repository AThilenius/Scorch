//
//  Anvil.cpp
//  Anvil
//
//  Created by Alec Thilenius on 12/1/14.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#include "PrefixHeader.h"
#include "Anvil.h"

#include "BFEProtos.pb.h"
#include "TcpSocket.h"
#include "TcpMessage.h"
#include "Level.h"
#include "Config.h"
#include "TextFile.h"

using Socket::TcpSocket;
using Socket::TcpMessage;
using Socket::TcpMessagePtr;
using Thilenius::BFEProtos::BFEMessage;
using Thilenius::BFEProtos::BFELoadLevelRequest;
using Thilenius::BFEProtos::BFELoadLevelResponse;
using Thilenius::BFEProtos::BFEInfoQueryRequest;
using Thilenius::BFEProtos::BFEInfoQueryResponse;
using Thilenius::BFEProtos::BFECodeSubmitRequest;
using Thilenius::BFEProtos::BFECodeSubmitResponse;
using Thilenius::BFEProtos::BFETextFile;

namespace AnvilAPI {

	
// Messages
std::string ConnectionError (
    std::string("Failed to connect to Blaze. Ensure you have an internet connection and try again. If ") +
    std::string("the problem persists, try re-downloading the project from Forge."));
std::string CommunicationError (
    std::string("A fatal error occurred while communicating with Blaze. Ensure you have a stable internet ") +
    std::string("connection and try re-running your code."));
std::string DataError (
    std::string("Blaze returned garbage data."));
    
    
// C API
extern "C" {
    
    
// Globals
::Socket::TcpSocket* g_socket = nullptr;
int g_activeLevel = -1;
    
    
void AnvilEnsureConnected() {
    if (g_socket == nullptr) {
        g_socket = new TcpSocket();
        if (!g_socket->Connect(Config::GetBlazeIP(), Config::GetBlazePort())) {
            Util::Log::Error(ConnectionError);
        } else {
            Util::Log::Info(std::string("Sucessfully connected to Blaze"));
            
//            // Send all source files to bein the Blaze Run record
//            std::vector<Util::TextFile> allFiles = Util::TextFile::GlobDirectory("/src");
//            
//            // Send BFECodeSubmitRequest
//            {
//                BFEMessage message;
//                BFECodeSubmitRequest* request = message.MutableExtension(BFECodeSubmitRequest::BFECodeSubmitRequest_ext);
//                request->set_auth_token(Config::GetAuthToken());
//                
//                for (Util::TextFile file : allFiles) {
//                    BFETextFile* textFileProto = request->add_code_files();
//                    textFileProto->set_name(file.Name);
//                    textFileProto->set_extension(file.Extension);
//                    textFileProto->set_modify_date(file.ModifyDate);
//                    textFileProto->set_contents(file.Contents);
//                }
//                
//                // Send it out
//                int size = message.ByteSize();
//                void* buffer = malloc(size);
//                message.SerializeToArray(buffer, size);
//                
//                if (!g_socket->Write(buffer, size)) {
//                    Util::Log::Error(CommunicationError);
//                }
//                
//                free(buffer);
//            }
//            
//            // Wait for submit confirmation
//            {
//                TcpMessagePtr response = g_socket->Read();
//                if (response == nullptr) {
//                    Util::Log::Error(CommunicationError);
//                }
//                
//                BFEMessage message;
//                if (!message.ParseFromArray(response->Data, response->Count)) {
//                    Util::Log::Error(CommunicationError);
//                }
//                
//                if (message.HasExtension(BFECodeSubmitResponse::BFECodeSubmitResponse_ext)) {
//                    BFECodeSubmitResponse response = message.GetExtension(BFECodeSubmitResponse::BFECodeSubmitResponse_ext);
//                    
//                    if (response.has_failure_reason()) {
//                        Util::Log::Error("Blaze returned a fatal error while trying submit code files: "
//                                         + response.failure_reason());
//                    }
//                } else {
//                    Util::Log::Error(CommunicationError);
//                }
//                
//            }
        }
    }
}


void AnvilSayHello() {
    std::string authToken = Config::GetAuthToken();
    Util::Log::Info("Saying hello to blaze with the token: [" + authToken + "]");
    
    AnvilEnsureConnected();
    
    // Send InfoQueryRequst
    {
        BFEMessage message;
        BFEInfoQueryRequest* request = message.MutableExtension(BFEInfoQueryRequest::BFEInfoQueryRequest_ext);
        request->set_auth_token(Config::GetAuthToken());
        
        int size = message.ByteSize();
        void* buffer = malloc(size);
        message.SerializeToArray(buffer, size);
        
        if (!g_socket->Write(buffer, size)) {
            Util::Log::Error(ConnectionError);
        }
        
        free(buffer);
    }
    
    // Get InfoQueryResponse
    {
        TcpMessagePtr response = g_socket->Read();
        if (response == nullptr) {
            Util::Log::Error(CommunicationError);
        }
        
        BFEMessage message;
        if (!message.ParseFromArray(response->Data, response->Count)) {
            Util::Log::Error(CommunicationError);
        }
        
        if (message.HasExtension(BFEInfoQueryResponse::BFEInfoQueryResponse_ext)) {
            BFEInfoQueryResponse response = message.GetExtension(BFEInfoQueryResponse::BFEInfoQueryResponse_ext);
            
            if (response.has_failure_reason()) {
                Util::Log::Error("Blaze returned a fatal error while trying to say hello: "
                                 + response.failure_reason());
            }
            
            // Everything went well, print out the response
            Util::Log::Info(response.blaze_response());
        } else {
            Util::Log::Error(CommunicationError);
        }
    }
}

int AnvilLoadLevel(int levelNumber) {
    if (g_activeLevel != -1) {
        Util::Log::Error(std::string("You cannot load multiple levels during a single Anvil run (Aka you are calling") +
                         std::string(" LoadLevel more than once)."));
    }
    
    AnvilEnsureConnected();
    
    // Send LoadLevelRequst
    {
        BFEMessage message;
        BFELoadLevelRequest* request = message.MutableExtension(BFELoadLevelRequest::BFELoadLevelRequest_ext);
        request->set_auth_token(Config::GetAuthToken());
        request->set_levelnumber(levelNumber);
        request->set_seed(1234);
        
        int size = message.ByteSize();
        void* buffer = malloc(size);
        message.SerializeToArray(buffer, size);
        
        if (!g_socket->Write(buffer, size)) {
            Util::Log::Error(ConnectionError);
        }
        
        free(buffer);
    }
    
    // Get LoadLevel response
    {
        TcpMessagePtr response = g_socket->Read();
        if (response == nullptr) {
            Util::Log::Error(CommunicationError);
        }
        
        BFEMessage message;
        if (!message.ParseFromArray(response->Data, response->Count)) {
            Util::Log::Error(CommunicationError);
        }
        
        if (message.HasExtension(BFELoadLevelResponse::BFELoadLevelResponse_ext)) {
            BFELoadLevelResponse response = message.GetExtension(BFELoadLevelResponse::BFELoadLevelResponse_ext);
            
            if (response.has_failure_reason()) {
                Util::Log::Error("Blaze returned a fatal error while trying to load level " +
                                 std::to_string(levelNumber) + ". Given reason: " + response.failure_reason());
            }
            
            // Everything went well, return the spark count back to user
            Util::Log::Info("Level " + std::to_string(levelNumber) + " loaded.");
            g_activeLevel = levelNumber;
            return response.spark_count();
        } else {
            Util::Log::Error(CommunicationError);
        }
    }
    
    return -1;
}
   
    
} // extern C
    

// C++ API
void Anvil::SayHello() {
    // Call C API
    AnvilSayHello();
}

Level Anvil::LoadLevel(int levelNumber) {
    // Call C API
    int sparkCount = AnvilLoadLevel(levelNumber);
    if (sparkCount < 0) {
        return Level(-1, 0);
    } else {
        return Level(levelNumber, sparkCount);
    }
}
    
    
} // namespace AnvilAPI