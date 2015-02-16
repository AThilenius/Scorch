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
	//static void SyncronizeTo(boost::shared_ptr<TProtocol>& protocol, int pingCount = 1000);
	static inline void SleepForMs(UInt64 ms)
	{
		//std::this_thread::sleep_for(std::chrono::milliseconds(ms));
	}

private:
    static boost::posix_time::ptime m_zeroTime;
	static double m_compensation;

};

