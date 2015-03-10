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
    

std::string* Config::s_ipOverride = nullptr;
std::string* Config::s_authTokenOverride = nullptr;
    
    
std::string Config::GetAuthToken() {
    if (s_authTokenOverride != nullptr) {
        return *s_authTokenOverride;
    }
    
    // Open relative config file
#ifdef _WIN32
    std::ifstream t("../AuthToken.aat");
#else
    // Build rule on OSX to copy to Products Directory
    std::ifstream t("./AuthToken.aat");
#endif
    std::string configStr((std::istreambuf_iterator<char>(t)),
                          std::istreambuf_iterator<char>());
    return configStr;
}

std::string Config::GetBlazeIP() {
    if (s_ipOverride != nullptr) {
        return *s_ipOverride;
    }
    
    return "54.67.9.61";
}

int Config::GetBlazePort() {
    return 5529;
}
    
void Config::OverrideIp(std::string blazeIp)
{
    s_ipOverride = new std::string(blazeIp);
}

void Config::OverrideAuthToken(std::string authToken)
{
    s_authTokenOverride = new std::string(authToken);
}
    
} // namespace AnvilAPI