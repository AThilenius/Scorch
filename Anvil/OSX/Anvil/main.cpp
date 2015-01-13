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

using namespace AnvilAPI;

int main(int argc, const char * argv[]) {
//    Config::OverrideIp("0.0.0.0");
//    Config::OverrideAuthToken("ca41904d-91de-47c3-93a6-bbbd06282eeb");
//    Util::Log::Suppress();
//    
//    std::cout << "Running 'Say Hello' Performance analysis" << std::endl;
//    
//    while (true) {
//        double startTime = Time::GetTime();
//        for (int i = 0; i < 100; i++) {
//            Anvil::SayHello();
//        }
//        double endTime = Time::GetTime();
//        double singleTime = (endTime - startTime) / 100.0;
//        double iops = 1.0 / singleTime;
//        std::cout << iops << " IOPS" << std::endl;
//    }
    Anvil::SayHello();
}