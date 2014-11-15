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

int main(int argc, const char * argv[]) {
    

    TcpSocket socket;
    if (!socket.Connect("127.0.0.0", 5529)) {
        std::cout << "Failed to connect to localhost on port 5529." << std::endl;
        return 0;
    }
    
    
    std::cout << "Connected!" << std::endl;
    
    {
        BFEMessage message;
        BFEAuthRequest* request = message.MutableExtension(BFEAuthRequest::BFEAuthRequest_ext);
        request->set_username("athilenius");
        request->set_password("qwe123");
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
    
    TcpMessagePtr response = socket.Read();
    if (response == nullptr) {
        std::cout << "Failed to recieve response" << std::endl;
        return 0;
    }
    
    std::cout << "Got [" << response->Count << "]: ";
    for (int i = 0; i < response->Count; i++) {
        std::cout << (int)((char*)response->Data)[i] << " ";
    }
    std::cout << std::endl;
    
    BFEMessage responseMessage;
    responseMessage.ParseFromArray(response->Data, response->Count);

    if (responseMessage.HasExtension(BFEAuthResponse::BFEAuthResponse_ext)) {
        BFEAuthResponse response = responseMessage.GetExtension(BFEAuthResponse::BFEAuthResponse_ext);
        std::cout << "Got AUTH response: " << response.DebugString() << std::endl;
    }
    
    return 0;
}
