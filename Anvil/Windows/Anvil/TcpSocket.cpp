//
//  TcpSocket.cpp
//  Anvil
//
//  Created by Alec Thilenius on 11/14/14.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#include "PrefixHeader.h"
#include "TcpSocket.h"
#include "TcpMessage.h"

#ifdef _WIN32
	#define WIN32_LEAN_AND_MEAN
	#include <winsock2.h>
	#include <ws2tcpip.h>
	#include <stdio.h>
	#include <stdlib.h>
	#pragma comment(lib,"Ws2_32.lib")
#else
    #include <unistd.h>
    #include <arpa/inet.h>
    #include <netdb.h>
    #include <netinet/in.h>
    #include <sys/socket.h>
    #include <sys/types.h>
#endif

namespace Socket {
    
    
TcpSocket::TcpSocket() :
    m_socketHandle(-1) {
    
}

TcpSocket::~TcpSocket() {
    
}

bool TcpSocket::Connect(std::string ipAddressStr, UInt16 port) {
    
#ifdef _WIN32
	// Fucking Windows...
	WSADATA wsaData;
	int iResult;

	iResult = WSAStartup(MAKEWORD(2,2), &wsaData);
	if (iResult != 0) {
		Util::Log::Error("Winsock isn't supported on your system.");
		return false;
	}
#endif

    // Setup address
    sockaddr_in serverAddress;
    inet_pton(AF_INET, ipAddressStr.c_str(), &(serverAddress.sin_addr));
    serverAddress.sin_port = htons(port);
    serverAddress.sin_family = AF_INET;
    
    m_socketHandle = socket(AF_INET, SOCK_STREAM, 0);
    if (m_socketHandle < 0) {
        return false;
    }
    
    if (connect(m_socketHandle, (sockaddr*) &serverAddress, sizeof(serverAddress)) < 0) {
        return false;
    }
    
    return true;
}

TcpMessagePtr TcpSocket::Read() {
    
    // First read in the 4 byte size (in network endian)
    UInt32 size = 0;
    
    if (!ReadCompleate((char*)&size, m_socketHandle, 4)) {
        return TcpMessagePtr(nullptr);
    }
    
    // Convert to host endian
    size = ntohl(size);
    
    // Read in that size
    void* buffer = malloc(size);
    
    if (!ReadCompleate((char*)buffer, m_socketHandle, size)) {
        return TcpMessagePtr(nullptr);
    }
    
    // Wrap it in a shared ptr
    TcpMessage* message = new TcpMessage(buffer, size, this);
    return TcpMessagePtr(message);
}

bool TcpSocket::Write(void* data, int count) {
    
    // First write out the size (in network endian)
    UInt32 size = htonl(count);
    if (!WriteComplete((char*)&size, m_socketHandle, 4)) {
        return false;
    }
    
    // Next write out the message
    if (!WriteComplete((char*)data, m_socketHandle, count)) {
        return false;
    }
    
    return true;
}

bool TcpSocket::ReadCompleate(char* buffer, int socket, UInt32 count) {
    assert(count != 0);
    
    UInt32 readCount = 0;
    while (readCount < count) {
#ifdef _WIN32
		Int64 n = recv(socket, &buffer[readCount], count - readCount, 0);
#else
        Int64 n = read(socket, &buffer[readCount], count - readCount);
#endif
        readCount += n;
        if (n < 0) {
            // Socket was forcibly closed
            return false;
        }
    }
    return true;
}

bool TcpSocket::WriteComplete(char* buffer, int socket, UInt32 count) {
    assert(count != 0);

    UInt32 writeCount = 0;
    while (writeCount < count) {
#ifdef _WIN32
		Int64 n = send(socket, &buffer[writeCount], count - writeCount, 0);
#else
        Int64 n = write(socket, &buffer[writeCount], count - writeCount);
#endif
        writeCount += n;
        if (n < 0) {
            // Socket was forcibly closed
            return false;
        }
    }
    return true;
}
    
    
} // namespace Socket