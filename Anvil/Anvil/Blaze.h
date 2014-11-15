//
//  Anvil.h
//  Anvil
//
//  Created by Alec Thilenius on 11/14/14.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#pragma once


namespace Anvil {

    
class Level;

class Blaze {
public:
    Blaze(std::string username, std::string password);
    ~Blaze();
    
    Level LoadLevel (int levelNumber);
};


} // namespace Anvil