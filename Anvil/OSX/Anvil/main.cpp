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

using namespace AnvilAPI;

int main(int argc, const char * argv[]) {
    Config::OverrideIp("0.0.0.0");
    Config::OverrideAuthToken("abcd15cb-c3a8-4ffb-92c5-97eba1e4601c");
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
    
    Anvil::SayHello();
    Anvil::LoadLevel(0);
}