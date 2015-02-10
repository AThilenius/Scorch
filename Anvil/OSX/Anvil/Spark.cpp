//
//  Spark.cpp
//  Anvil
//
//  Created by Alec Thilenius on 12/1/14.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#include "PrefixHeader.h"
#include "Spark.h"

#include "BFEProtos.pb.h"
#include "Anvil.h"
#include "TcpSocket.h"
#include "TcpMessage.h"
#include "BFEProtos.pb.h"
#include "Config.h"


using Socket::TcpMessage;
using Thilenius::BFEProtos::BFEMessage;
using Thilenius::BFEProtos::BFESparkCommand;
using Thilenius::BFEProtos::BFESparkCommand_CommandType;
using Thilenius::BFEProtos::BFESparkResponse;
using Thilenius::BFEProtos::BFESparkResponse;


namespace AnvilAPI {
    

// C++ RunCommand for C Linkage
BFESparkResponse RunCommand (Socket::TcpSocket* socket, int sparkNumber, BFESparkCommand_CommandType connandType) {
    // dispatch
    {
        BFEMessage message;
        BFESparkCommand* request = message.MutableExtension(BFESparkCommand::BFESparkCommand_ext);
        request->set_command(connandType);
        request->set_spark_id(sparkNumber);
        request->set_auth_token(Config::GetAuthToken());
        
        int size = message.ByteSize();
        void* buffer = malloc(size);
        message.SerializeToArray(buffer, size);
        
        if (!socket->Write(buffer, size)) {
            std::cout << "A fatal error occurred while trying to communicate with Blaze. Ensure you have a stable "
            << "internet connection and try re-running your code." << std::endl;
            exit(EXIT_FAILURE);
        }
        
        free(buffer);
    }
    
    // Pull response
    {
        ::Socket::TcpMessagePtr response = socket->Read();
        if (response == nullptr) {
            std::cout << "A fatal error occurred while trying to communicate with Blaze. Ensure you have a stable "
            << "internet connection and try re-running your code." << std::endl;
            exit(EXIT_FAILURE);
        }
        
        BFEMessage message;
        if (!message.ParseFromArray(response->Data, response->Count)) {
            std::cout << "Failed to parse Protocol Buffer from Blaze." << std::endl;
            exit(EXIT_FAILURE);
        }
        
        if (message.HasExtension(BFESparkResponse::BFESparkResponse_ext)) {
            return message.GetExtension(BFESparkResponse::BFESparkResponse_ext);
        } else {
            std::cout << "Blaze returned unexpected data." << std::endl;
            exit(EXIT_FAILURE);
        }
    }
}
    

// C API
extern "C" {
    
    
// Extern access to Anvil globals
extern ::Socket::TcpSocket* g_socket;
extern int g_activeLevel;


bool SparkMoveForward(int level, int spark) {
    BFESparkResponse response = RunCommand(g_socket, spark,
                                           BFESparkCommand_CommandType::BFESparkCommand_CommandType_MOVE_FORWARD);
    if (!response.has_response_bool()) {
        std::cout << "Blaze returned unexpected data." << std::endl;
        exit(EXIT_FAILURE);
    } else {
        return response.response_bool();
    }
}

bool SparkMoveBackward(int level, int spark) {
    BFESparkResponse response = RunCommand(g_socket, spark,
                                           BFESparkCommand_CommandType::BFESparkCommand_CommandType_MOVE_FORWARD);
    if (!response.has_response_bool()) {
        std::cout << "Blaze returned unexpected data." << std::endl;
        exit(EXIT_FAILURE);
    } else {
        return response.response_bool();
    }
}

void SparkTurnLeft(int level, int spark) {
    RunCommand(g_socket, spark, BFESparkCommand_CommandType::BFESparkCommand_CommandType_TURN_LEFT);
}

void SparkTurnRight(int level, int spark) {
    RunCommand(g_socket, spark, BFESparkCommand_CommandType::BFESparkCommand_CommandType_TURN_RIGHT);
}
    
    
} // extern C


// C++ API
Spark::Spark(int levelNumber, int sparkNumber) :
    m_levelNumber(levelNumber),
    m_sparkNumber(sparkNumber) {
    
}

Spark::~Spark() {
    
}
    
bool Spark::MoveForward() {
    // Call through to C API
    return SparkMoveForward(m_levelNumber, m_sparkNumber);
}

bool Spark::MoveBackward() {
    // Call through to C API
    return SparkMoveBackward(m_levelNumber, m_sparkNumber);
}

void Spark::TurnLeft(){
    // Call through to C API
    SparkTurnLeft(m_levelNumber, m_sparkNumber);
}

void Spark::TurnRight() {
    // Call through to C API
    SparkTurnRight(m_levelNumber, m_sparkNumber);
}


    
} // namespace AnvilAPI