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
    };
    
} // namespace AnvilAPI