//
//  TcpMessage.h
//  Anvil
//
//  Created by Alec Thilenius on 11/15/14.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#pragma once


namespace Socket {
    

class TcpSocket;
    
class TcpMessage {
public:
    ~TcpMessage();
    
public:
    void* Data;
    int Count;
    TcpSocket* Socket;
    
private:
    TcpMessage(void* data, int count, TcpSocket* socket);
    
private:
    friend class TcpSocket;
    
};


typedef std::shared_ptr<TcpMessage> TcpMessagePtr;


} // namespace Socket