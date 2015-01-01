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

	
	// Messages
	std::string ConnectionError (
		std::string("Failed to connect to Blaze. Ensure you have an internet connection and try again. If ") +
		std::string("the problem persists, try re-downloading the project from Forge."));
	std::string CommunicationError (
		std::string("A fatal error occurred while communicating with Blaze. Ensure you have a stable internet ") +
		std::string("connection and try re-running your code."));
	std::string DataError (
		std::string("Blaze returned garbage data."));


    ::Socket::TcpSocket* Anvil::m_socket = nullptr;
    int Anvil::m_activeLevel = -1;
    
    // Configuration
    std::string Anvil::AuthToken = "D6089AA2-CB1C-4FFA-8990-4297A38376C2";
    std::string Anvil::BlazeIP = "54.67.38.67";
    int Anvil::BlazePort = 5529;

    Level Anvil::LoadLevel(int levelNumber) {
        if (m_socket == nullptr) {
            m_socket = new TcpSocket();
            if (!m_socket->Connect(BlazeIP, BlazePort)) {
                Util::Log::Error(ConnectionError);
            }
        }
        
        // Send LoadLevelRequst
        {
            BFEMessage message;
            BFELoadLevelRequest* request = message.MutableExtension(BFELoadLevelRequest::BFELoadLevelRequest_ext);
            request->set_auth_token(AuthToken);
            request->set_levelnumber(levelNumber);
            request->set_seed(1234);
            
            int size = message.ByteSize();
            void* buffer = malloc(size);
            message.SerializeToArray(buffer, size);
            
			if (!m_socket->Write(buffer, size)) {
				Util::Log::Error(ConnectionError);
            }
            
            free(buffer);
        }
        
        // Get LoadLevel response
        {
            TcpMessagePtr response = m_socket->Read();
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
                    Util::Log::Error("Blaze returned a fatal error while trying to load level " + std::to_string(levelNumber) +
						". Given reason: " + response.failure_reason());
                }
                
                // Everything went well, return it back to user
                Level level(levelNumber, response.spark_count());
                m_activeLevel = levelNumber;
                return level;
            } else {
                Util::Log::Error(CommunicationError);
            }
        }
        
        return Level(-1, 0);
    }
    
    
} // namespace AnvilAPI