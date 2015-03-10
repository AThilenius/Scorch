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
    //Config::OverrideIp("54.67.38.67");
    
    // Alec Thilenius 2 AWS
    Config::OverrideAuthToken("ca527581-3319-4aa8-8a6b-4de8cccd118e");
    
    // John Doe 1 AWS
    // Config::OverrideAuthToken("3440552a-0791-4565-beb0-74ae48c5a17b");
    
    // John Doe 2 Local
    // Config::OverrideAuthToken("ca527581-3319-4aa8-8a6b-4de8cccd118e");
    
    Anvil::LoadLevel(0);
}