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

using Thilenius::BFEProtos::BFEAuthRequest;

int main(int argc, const char * argv[]) {
    
    sf::TcpSocket socket;
    sf::IpAddress address = sf::IpAddress::LocalHost;
    unsigned short port = 5529;
    sf::Socket::Status status = socket.connect("127.0.0.0", port);
    
    if (status != sf::Socket::Done) {
        std::cout << "Failed to connect to localhost on port " << port << "." << std::endl;
        return 0;
    }
    
    std::cout << "Connected!" << std::endl;
    
    BFEAuthRequest request;
    request.set_username("Hello Username");
    request.set_password("Hello Password!");
    
    int size = request.ByteSize();
    void *buffer = malloc(size);
    request.SerializeToArray(buffer, size);
    
    // Send test data
    if (socket.send(buffer, size) != sf::Socket::Done) {
        std::cout << "Failed to send test data" << std::endl;
    }
    
    return 0;
}
