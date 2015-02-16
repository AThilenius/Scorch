//
//  main.cpp
//  Anvil
//
//  Created by Alec Thilenius on 11/13/14.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#include "PrefixHeader.h"


#include "Anvil.h"
#include "Config.h"
#include "Log.h"
#include "Time.h"
#include "BFEProtos.pb.h"
#include "TextFile.h"

using Thilenius::BFEProtos::BFEMessage;
using Thilenius::BFEProtos::BFELoadLevelRequest;
using Thilenius::BFEProtos::BFELoadLevelResponse;
using Thilenius::BFEProtos::BFEInfoQueryRequest;
using Thilenius::BFEProtos::BFEInfoQueryResponse;

using namespace AnvilAPI;

int main(int argc, const char * argv[]) {
    Config::OverrideIp("0.0.0.0");
    
    // Alec Thilenius 1
    // Config::OverrideAuthToken("3440552a-0791-4565-beb0-74ae48c5a17b");
    
    // John Doe 1 AWS
    //Config::OverrideAuthToken("3440552a-0791-4565-beb0-74ae48c5a17b");
    
    // John Doe 2 Local
    Config::OverrideAuthToken("5e36b753-b897-4dce-a0f7-631106661a9b");
    
    
//    Spark spark = Anvil::LoadLevel(0).GetSpark();
//    spark.MoveForward();
//    spark.TurnRight();
    
    // Forward Forward Backward TurnLeft TurnLeft TurnRight Forward Forward
//    Level level = Anvil::LoadLevel(1);
//    Spark spark = level.GetSpark();
//    
//    spark.MoveForward();
//    spark.MoveForward();
//    spark.MoveBackward();
//    spark.TurnLeft();
//    spark.TurnLeft();
//    spark.TurnRight();
//    spark.MoveForward();
//    spark.MoveForward();
    
//    // Backward Backward TurnRight Forward TurnLeft Forward Forward TurnLeft Backward
    Level level = Anvil::LoadLevel(2);
    Spark spark = level.GetSpark();
    
    spark.MoveBackward();
    spark.MoveBackward();
    spark.TurnRight();
    spark.MoveForward();
    spark.TurnLeft();
    spark.MoveForward();
    spark.MoveForward();
    spark.TurnLeft();
    spark.MoveBackward();

}