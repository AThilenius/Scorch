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
    
    // John Doe 1
    Config::OverrideAuthToken("3440552a-0791-4565-beb0-74ae48c5a17b");
    
    Anvil::SayHello();
    Anvil::LoadLevel(0);
    
}