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
    
    
} // namespace AnvilAPI