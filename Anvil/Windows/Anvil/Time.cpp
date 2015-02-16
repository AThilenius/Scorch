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
double Time::m_compensation = 0.0;


double Time::GetTime()
{
    boost::posix_time::ptime currentTime = boost::posix_time::microsec_clock::local_time();
    boost::posix_time::time_duration delta = currentTime - Time::m_zeroTime;

    return (delta.total_microseconds() / 1000000.0) + m_compensation;
}

//void Time::SyncronizeTo(boost::shared_ptr<TProtocol>& protocol, int pingCount)
//{
//	std::cout << "Syncronizing Chronometer" << std::endl;
//	double totalLatency = 0.0;
//	double* latencies = new double[pingCount];
//	double* deltaTimes = new double[pingCount];
//	Chrono::ChronometerClient client(protocol);
//	
//	// Ping server 'pointCount' times
//	for (int i = 0; i < pingCount; i++)
//	{
//		// Ping server, record latency
//		double startTime = Time::GetTime();
//		double serverTime = client.GetServerTimeSeconds();
//		double endTime = Time::GetTime();
//		latencies[i] = endTime - startTime;
//		deltaTimes[i] = serverTime - endTime - (latencies[i] / 2.0);
//		totalLatency += latencies[i];
//	}
//
//	double latencyMean = totalLatency / (double) pingCount;
//	double standardDeviation, var, dev, sum = 0.0,
//		sdev = 0.0, cv;
//
//	for (int i = 1; i <= pingCount; i++)
//	{
//		dev = (latencies[i] - latencyMean) * (latencies[i] - latencyMean);
//		sdev = sdev + dev;
//	}
//
//	var = sdev / (pingCount - 1);
//	standardDeviation = sqrt(var);
//	cv = (standardDeviation / latencyMean) * 100;
//
//	// Compute average, ignoring anything GREATER THAN 1 standard deviant
//	int deltaTimeCount = 0;
//	double totalDeltaTime = 0.0;
//	for (int i = 0; i < pingCount; i++)
//	{
//		if (latencies[i] > (latencyMean + standardDeviation))
//			continue;
//
//		// Data is good, lets count it in
//		deltaTimeCount++;
//		totalDeltaTime += deltaTimes[i];
//	}
//
//	// Average delta time
//	double avgDeltaTime = totalDeltaTime / (double)deltaTimeCount;
//
//	// Offset Time by this much
//	m_compensation += avgDeltaTime;
//
//	std::setprecision(5);
//	std::cout << "Mean: " << latencyMean << std::endl;
//	std::cout << "Variance: " << var << std::endl;
//	std::cout << "Standard Deviation: " << standardDeviation << std::endl;
//	std::cout << "Coefficient of Variation: " << cv << "\%" << std::endl;
//	std::cout << "Kept samples: " << deltaTimeCount << std::endl;
//	std::cout << "Average Delta-Time: " << avgDeltaTime << std::endl << std::endl;
//}