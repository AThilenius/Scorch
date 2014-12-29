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

using Socket::TcpSocket;
using Socket::TcpMessage;
using Socket::TcpMessagePtr;
using Thilenius::BFEProtos::BFEMessage;
using Thilenius::BFEProtos::BFELoadLevelRequest;
using Thilenius::BFEProtos::BFELoadLevelResponse;

namespace AnvilAPI {
 
    
    ::Socket::TcpSocket* Anvil::m_socket = nullptr;
    int Anvil::m_activeLevel = -1;
    
    // Configuration
    //std::string Anvil::AuthToken = "c0dba92c-6449-40a6-95da-11d2b7d28ad6";
    //std::string Anvil::BlazeIP = "54.67.82.68";
    std::string Anvil::BlazeIP = "127.0.0.0";
    int Anvil::BlazePort = 5529;

    Level Anvil::LoadLevel(int levelNumber) {
        if (m_socket == nullptr) {
            m_socket = new TcpSocket();
            if (!m_socket->Connect(BlazeIP, BlazePort)) {
                std::cout << "Failed to connect to Blaze. Ensure you have an internet connection and try again. If "
                << "the problem persists, try re-downloading the project from Forge." << std::endl;
                exit(EXIT_FAILURE);
            }
        }
        
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
            
            if (!m_socket->Write(buffer, size)) {
                std::cout << "A fatal error accured while trying to communicate with Blaze. Ensure you have a stable "
                << "internet connection and try re-running your code." << std::endl;
                exit(EXIT_FAILURE);
            }
            
            free(buffer);
        }
        
        // Get LoadLevel response
        {
            TcpMessagePtr response = m_socket->Read();
            if (response == nullptr) {
                std::cout << "A fatal error accured while trying to communicate with Blaze. Ensure you have a stable "
                << "internet connection and try re-running your code." << std::endl;
                exit(EXIT_FAILURE);
            }
            
            BFEMessage message;
            if (!message.ParseFromArray(response->Data, response->Count)) {
                std::cout << "Failed to parse Protocol Buffer from Blaze." << std::endl;
                exit(EXIT_FAILURE);
            }
            
            if (message.HasExtension(BFELoadLevelResponse::BFELoadLevelResponse_ext)) {
                BFELoadLevelResponse response = message.GetExtension(BFELoadLevelResponse::BFELoadLevelResponse_ext);
                
                if (response.has_failure_reason()) {
                    std::cout << "Blaze retuned a fatal error while trying to load level " << levelNumber
                    << ". Given reason: " << response.failure_reason() << std::endl;
                    exit(EXIT_FAILURE);
                }
                
                // Everything went well, return it back to user
                Level level(levelNumber, response.spark_count());
                m_activeLevel = levelNumber;
                return level;
            } else {
                std::cout << "Blaze retuned unexpected data: " << message.DebugString() << std::endl;
                exit(EXIT_FAILURE);
            }
        }
    }
    
    
} // namespace AnvilAPI