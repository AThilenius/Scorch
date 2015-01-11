//
//  Config.cpp
//  Anvil
//
//  Created by Alec Thilenius on 1/8/15.
//  Copyright (c) 2015 Alec Thilenius. All rights reserved.
//
#include "PrefixHeader.h"
#include "Config.h"

namespace AnvilAPI {
    
    std::string Config::GetAuthToken() {
        // Open relative config file
        std::ifstream t("../../../../../AuthToken.aat");
        std::string configStr((std::istreambuf_iterator<char>(t)),
                              std::istreambuf_iterator<char>());
        return configStr;
    }
    
    std::string Config::GetBlazeIP() {
        return "54.67.38.67";
        //return "127.0.0.0";
    }
    
    int Config::GetBlazePort() {
        return 5529;
    }
    
} // namespace AnvilAPI