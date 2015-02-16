//
//  Anvil.h
//  Anvil
//
//  Created by Alec Thilenius on 12/1/14.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#pragma once

#include <string>

#include "Level.h"
#include "Spark.h"


namespace Socket {
class TcpSocket;
} // namespace Socket


namespace AnvilAPI {

class Anvil {
public:
    static void SayHello();
    static Level LoadLevel(int levelNumber);
};
    
    
#ifdef ANVIL_EXPOSE_C_API
    
    
extern "C" {
    
    
void AnvilSayHello();
int AnvilLoadLevel(int levelNumber);
    
    
} // extern C
    
    
#endif
    
    
} // namespace AnvilAPI