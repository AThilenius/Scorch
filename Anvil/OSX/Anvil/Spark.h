//
//  Spark.h
//  Anvil
//
//  Created by Alec Thilenius on 12/1/14.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#pragma once


namespace AnvilAPI {

    
class Spark {
public:
    Spark(int levelNumber, int sparkNumber);
    ~Spark();
    
    bool MoveForward();
    bool MoveBackward();
    void TurnLeft();
    void TurnRight();
    
private:
    int m_levelNumber;
    int m_sparkNumber;
};
    
    
#ifdef ANVIL_EXPOSE_C_API
    
    
extern "C" {
    
    
bool SparkMoveForward(int level, int spark);
bool SparkMoveBackward(int level, int spark);
void SparkTurnLeft(int level, int spark);
void SparkTurnRight(int level, int spark);
    
    
} // extern C
    
    
#endif
    
    
} // namespace AnvilAPI