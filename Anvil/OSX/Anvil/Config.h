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
        return std::string("c0dba92c-6449-40a6-95da-11d2b7d28ad6");
    }
};

} // namespace AnvilAPI