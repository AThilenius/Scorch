////
////  SparkServiceImpl.cpp
////  Blaze
////
////  Created by Alec Thilenius on 10/9/2014.
////  Copyright (c) 2013 Thilenius. All rights reserved.
////
//#include "SparkServiceImpl.h"
//
//namespace Blaze {
//namespace Services {
//
//SparkServiceImpl::SparkServiceImpl( RCF::TcpEndpoint endPoint ):
//	m_tcpEndpoint(endPoint),
//	m_server(nullptr)
//{
//	
//}
//
//SparkServiceImpl::~SparkServiceImpl()
//{
//	if (m_server != nullptr)
//	{
//		delete m_server;
//		m_server = nullptr;
//	}
//}
//
//void SparkServiceImpl::Start()
//{
//	m_server = new RCF::RcfServer(m_tcpEndpoint);
//	m_server->bind<IServiceSpark>(this);
//	m_server->start();
//
//	std::cout << "Press Enter to exit..." << std::endl;
//	std::cin.get();
//}
//
//bool SparkServiceImpl::DispatchMovement(
//	const Spark& spark,
//	const MovementTypes& movementType)
//{
//
//}
//
//void SparkServiceImpl::DispatchOrientation(
//	const Spark& spark,
//	const OrientationTypes& orientationType)
//{
//
//}
//
//} // namespace Services
//} // namespace Blaze