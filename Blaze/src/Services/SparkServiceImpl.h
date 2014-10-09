////
////  SparkServiceImpl.h
////  Blaze
////
////  Created by Alec Thilenius on 10/9/2014.
////  Copyright (c) 2013 Thilenius. All rights reserved.
////
//#pragma  once
//
//#include <RCF\RCF.hpp>
//#include "Blaze.h"
//
//namespace Blaze {
//namespace Services {
//
//class SparkServiceImpl
//{
//public:
//	SparkServiceImpl(RCF::TcpEndpoint endPoint );
//	~SparkServiceImpl();
//
//	void Start();
//
//	bool DispatchMovement( const Spark& spark, const MovementTypes& movementType );
//	void DispatchOrientation( const Spark& spark, const OrientationTypes& orientationType );
//
//private:
//	RCF::TcpEndpoint m_tcpEndpoint;
//	RCF::RcfServer* m_server;
//};
//
//} // namespace Services
//} // namespace Blaze