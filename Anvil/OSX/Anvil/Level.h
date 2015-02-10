//
//  AnvilLevel.h
//  Anvil
//
//  Created by Alec Thilenius on 11/14/14.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#pragma once


namespace AnvilAPI {
 
    
class Spark;
    

class Level {
public:
    Level(int levelNumber, int sparkCount);
    ~Level();
    
    Spark GetSpark(int sparkNumber = 0);
    
private:
    int m_levelNumber;
    int m_sparkCount;
    
};
    
    
} // namespace AnvilAPI