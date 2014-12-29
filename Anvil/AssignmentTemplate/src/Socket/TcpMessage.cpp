//
//  TcpMessage.cpp
//  Anvil
//
//  Created by Alec Thilenius on 11/15/14.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#include "PrefixHeader.h"
#include "TcpMessage.h"


namespace Socket {


TcpMessage::TcpMessage(void* data, int count, TcpSocket* socket) :
    Data(data),
    Count(count),
    Socket(socket){
    
}

TcpMessage::~TcpMessage() {
    SAFE_FREE(Data);
}


} // namespace Socket