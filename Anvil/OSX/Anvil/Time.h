//
//  Time.h
//  VoxelCraftOSX
//
//  Created by Alec Thilenius on 8/20/13.
//  Copyright (c) 2013 Thilenius. All rights reserved.
//

#pragma once

#include "boost/date_time/posix_time/posix_time.hpp"

class Time
{
public:
    static double GetTime();
    
private:
    static boost::posix_time::ptime m_zeroTime;

};

