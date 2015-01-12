//
//  Config.h
//  Anvil
//
//  Created by Alec Thilenius on 12/28/14.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#pragma once

namespace AnvilAPI {
    
class Config {
public:
    static std::string GetAuthToken();
    static std::string GetBlazeIP();
    static int GetBlazePort();
    static void OverrideIp(std::string blazeIp);
    static void OverrideAuthToken(std::string authToken);
    
private:
    static std::string* s_ipOverride;
    static std::string* s_authTokenOverride;
};
    
} // namespace AnvilAPI