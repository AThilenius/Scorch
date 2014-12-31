//
//  AnvilLevel.cpp
//  Anvil
//
//  Created by Alec Thilenius on 11/14/14.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#include "PrefixHeader.h"
#include "Level.h"

#include "Spark.h"


namespace AnvilAPI {


Level::Level(int levelNumber, int sparkCount) :
    m_levelNumber(levelNumber),
    m_sparkCount(sparkCount) {
    
}

Level::~Level() {
    
}
    
Spark Level::GetSpark(int sparkNumber) {
    if (sparkNumber >= m_sparkCount) {
        std::cout << "The loaded level does not have a spark " << sparkNumber << std::endl;
        exit(EXIT_FAILURE);
    }
    
    Spark spark(m_levelNumber, sparkNumber);
    return spark;
}
    
    
} // namespace AnvilAPI