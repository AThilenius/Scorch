//
//  Time.cpp
//  VoxelCraftOSX
//
//  Created by Alec Thilenius on 8/20/13.
//  Copyright (c) 2013 Thilenius. All rights reserved.
//
#include "PrefixHeader.h"
#include "Time.h"


boost::posix_time::ptime Time::m_zeroTime = boost::posix_time::microsec_clock::local_time();


double Time::GetTime()
{
    boost::posix_time::ptime currentTime = boost::posix_time::microsec_clock::local_time();
    boost::posix_time::time_duration delta = currentTime - Time::m_zeroTime;

    return delta.total_microseconds() / 1000000.0;
}