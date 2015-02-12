//
//  AnvilAPI.cpp
//  AnvilDyn
//
//  Created by Alec Thilenius on 2/10/15.
//  Copyright (c) 2015 Scorch. All rights reserved.
//
#include "AnvilAPI.h"


#define ANVIL_EXPOSE_C_API
#include "Anvil.h"


extern "C" {
    
    
    void DynAnvilSayHello() {
        AnvilAPI::AnvilSayHello();
    }
    
    int DynAnvilLoadLevel(int levelNumber) {
        return AnvilAPI::AnvilLoadLevel(levelNumber);
    }
    
    bool DynSparkMoveForward(int level, int spark) {
        return AnvilAPI::SparkMoveForward(level, spark);
    }
    
    bool DynSparkMoveBackward(int level, int spark) {
        return AnvilAPI::SparkMoveBackward(level, spark);
    }
    
    void DynSparkTurnLeft(int level, int spark) {
        AnvilAPI::SparkTurnLeft(level, spark);
    }
    
    void DynSparkTurnRight(int level, int spark) {
        AnvilAPI::SparkTurnRight(level, spark);
    }
    
    
}