//
//  SparkServiceImpl.cpp
//  Blaze
//
//  Created by Alec Thilenius on 10/9/2014.
//  Copyright (c) 2013 Thilenius. All rights reserved.
//
#include "SparkServiceImpl.h"

#include <gflags/gflags.h>
#include <glog/logging.h>

DEFINE_int32(port, 9898,
			 "The port for the Blaze RPC Server to use.");

namespace Blaze {
namespace Services {

SparkServiceImpl::SparkServiceImpl():
	m_server(nullptr)
{
}

SparkServiceImpl::~SparkServiceImpl()
{
	if (m_server != nullptr)
	{
		delete m_server;
		m_server = nullptr;
	}
}

void SparkServiceImpl::Start()
{
	LOG(INFO) << "Starting SparkServiceImpl RCF Server on port: " << FLAGS_port;
	m_server = new RCF::RcfServer( RCF::TcpEndpoint(FLAGS_port) );
	m_server->bind<IServiceSpark>(*this);
	m_server->start();

}

bool SparkServiceImpl::DispatchMovement(
	const Spark& spark,
	const MovementTypes& movementType)
{
	return false;
}

void SparkServiceImpl::DispatchOrientation(
	const Spark& spark,
	const OrientationTypes& orientationType)
{

}

} // namespace Services
} // namespace Blaze