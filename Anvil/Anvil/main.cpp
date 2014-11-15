//
//  main.cpp
//  Anvil
//
//  Created by Alec Thilenius on 11/13/14.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//

#include <iostream>
#include <SFML/Network.hpp>
#include "BFEProtos.pb.h"

using Thilenius::BFEProtos::BFEMessage;
using Thilenius::BFEProtos::BFEAuthRequest;
using Thilenius::BFEProtos::BFEAuthResponse;

int main(int argc, const char * argv[]) {
    
    sf::TcpSocket socket;
    unsigned short port = 5529;
    sf::Socket::Status status = socket.connect("127.0.0.0", port);
    
    if (status != sf::Socket::Done) {
        std::cout << "Failed to connect to localhost on port " << port << "." << std::endl;
        return 0;
    }
    
    std::cout << "Connected!" << std::endl;
    
    BFEMessage message;
    BFEAuthRequest* request = message.MutableExtension(BFEAuthRequest::BFEAuthRequest_ext);
    request->set_username("athilenius");
    request->set_password("qwe123");
    message.PrintDebugString();
    request->PrintDebugString();
    
    
    int size = message.ByteSize();
    void *buffer = malloc(size);
    message.SerializeToArray(buffer, size);
    
    // Try deserializeing it?
    {
        BFEMessage deSerMessage;
        deSerMessage.ParseFromArray(buffer, (int)size);
        BFEAuthRequest deSerRequest = deSerMessage.GetExtension(BFEAuthRequest::BFEAuthRequest_ext);
        std::cout << "Deser" << std::endl;
        deSerMessage.PrintDebugString();
        deSerRequest.PrintDebugString();
    }
    
    // Send test data
    if (socket.send(buffer, size) != sf::Socket::Done) {
        std::cout << "Failed to send test data" << std::endl;
    }
    
    // Read back response
    char responseBuffer[1000];
    size_t read = 0;
    
    if (socket.receive(responseBuffer, 1000, read) == sf::Socket::Done) {
        BFEMessage message;
        message.ParseFromArray(responseBuffer, (int)read);
        
        if (message.HasExtension(BFEAuthResponse::BFEAuthResponse_ext)) {
            BFEAuthResponse response = message.GetExtension(BFEAuthResponse::BFEAuthResponse_ext);
            std::cout << "Got AUTH response: " << response.DebugString() << std::endl;
        }
    } else {
        std::cout << "Failed to get response data" << std::endl;
    }
    
    return 0;
}
