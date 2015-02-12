//
//  AnvilAPI.h
//  AnvilDyn
//
//  Created by Alec Thilenius on 2/10/15.
//  Copyright (c) 2015 Scorch. All rights reserved.
//
#pragma once

extern "C" {
    
    
void DynAnvilSayHello();
int DynAnvilLoadLevel(int levelNumber);
bool DynSparkMoveForward(int level, int spark);
bool DynSparkMoveBackward(int level, int spark);
void DynSparkTurnLeft(int level, int spark);
void DynSparkTurnRight(int level, int spark);
    
    
}