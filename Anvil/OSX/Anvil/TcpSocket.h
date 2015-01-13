//
//  TcpSocket.h
//  Anvil
//
//  Created by Alec Thilenius on 11/14/14.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#pragma once


namespace Socket {

class TcpMessage;
typedef std::shared_ptr<TcpMessage> TcpMessagePtr;
    
class TcpSocket {
public:
    TcpSocket();
    ~TcpSocket();

    bool Connect(std::string ipAddress, UInt16 port);
    TcpMessagePtr Read();
    bool Write(void* data, int count);

private:
    bool ReadCompleate(char* buffer, int socket, UInt32 count);
    bool WriteComplete(char* buffer, int socket, UInt32 count);
    
private:
	int m_socketHandle;
};

    
} // namespace Socket