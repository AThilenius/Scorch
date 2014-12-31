//
//  Config.h
//  Anvil
//
//  Created by Alec Thilenius on 12/28/14.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#pragma once

#include <stdio.h>

namespace AnvilAPI {

class Config {
public:
    static inline std::string GetAuthToken() {
        return std::string("<AuthToken>");
    }
    static inline std::string GetBlazeIP() {
        // 127.0.0.0
        return std::string("54.67.38.67");
    }
    static inline int GetBlazePort() {
        return 5529;
    }
};

} // namespace AnvilAPI