//
//  PCH.h
//  Blaze
//
//  Created by Alec Thilenius on 10/9/2014.
//  Copyright (c) 2013 Thilenius. All rights reserved.
//
#pragma once 

#define SAFE_DELETE(thing) if(thing!=nullptr){delete thing;thing=nullptr;}

// Workaround for RCF include order conflict with glog
#ifdef WIN32
#include <WinSock2.h>
#endif

#include <RCF/RCF.hpp>
#include <gflags/gflags.h>
#include <glog/logging.h>

#include <thrift/protocol/TBinaryProtocol.h>
#include <thrift/transport/TSocket.h>
#include <thrift/transport/TTransportUtils.h>