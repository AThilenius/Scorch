//
//  FlameClient.cpp
//  Blaze
//
//  Created by Alec Thilenius on 10/9/2014.
//  Copyright (c) 2013 Thilenius. All rights reserved.
//
#include "PCH.h"
#include "FlameClient.h"

#include "FlameService.h"

DEFINE_string(flame_ip, "localhost",
			  "The IP address for the Flame server.");
DEFINE_uint64(flame_port, 9090,
			  "The port number for the Flame server.");

namespace Blaze {
namespace Services {

using namespace apache::thrift;
using namespace apache::thrift::protocol;
using namespace apache::thrift::transport;

FlameClient::FlameClient() :
	m_client(nullptr) {
}

FlameClient::~FlameClient() {
	SAFE_DELETE(m_client);
}

void FlameClient::Connect() {
	LOG(INFO) << "Connecting to Flame on IP: " << FLAGS_flame_ip << ". Port: " << FLAGS_flame_port;
	boost::shared_ptr<TTransport> socket(new TSocket(FLAGS_flame_ip, FLAGS_flame_port));
	boost::shared_ptr<TTransport> transport(new TBufferedTransport(socket));
	boost::shared_ptr<TProtocol> protocol(new TBinaryProtocol(transport));
	m_client = new Flame::FlameServiceClient(protocol);

	try {
		transport->open();
		LOG(INFO) << "Connected to Flame on IP: " << FLAGS_flame_ip
			<< ". Port: " << FLAGS_flame_port;
	} catch (TException& tx) {
		LOG(ERROR) << "Failed to open a connection with Flame instance on IP: " << FLAGS_flame_ip
			<< ", Port:" << FLAGS_flame_port << ". Error: " << tx.what();
	}
}

Flame::FlameServiceClient* FlameClient::GetServiceClient() {
	CHECK_NOTNULL(m_client);
	return m_client;
}

} // namespace Thilenius
} // namespace Blaze
