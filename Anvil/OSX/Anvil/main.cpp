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
    Config::OverrideAuthToken("3440552a-0791-4565-beb0-74ae48c5a17b");
//    Util::Log::Suppress();
    
//    std::cout << "Running 'Say Hello' Performance analysis" << std::endl;
    
//    while (true) {
//        double startTime = Time::GetTime();
//        for (int i = 0; i < 10000; i++) {
//            Anvil::SayHello();
//        }
//        double endTime = Time::GetTime();
//        double singleTime = (endTime - startTime) / 10000.0;
//        double iops = 1.0 / singleTime;
//        std::cout << iops << " IOPS" << std::endl;
//    }
    
//    Anvil::SayHello();
//    Spark spark = Anvil::LoadLevel(1).GetSpark();
//    spark.MoveForward();
//    spark.MoveForward();
//    spark.MoveForward();
    
    Anvil::SayHello();
    Level level = Anvil::LoadLevel(1);
    Spark spark = level.GetSpark();
    spark.TurnLeft();
    
}