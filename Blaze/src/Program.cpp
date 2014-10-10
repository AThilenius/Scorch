//
//  Program.cpp
//  Blaze
//
//  Created by Alec Thilenius on 10/1/2014.
//  Copyright (c) 2013 Thilenius. All rights reserved.
//
#include "PCH.h"

#include "FlameClient.h"
#include "SparkServiceImpl.h"

DEFINE_uint64(port, 9091,
			  "The port number to host the Blaze server on.");

int main(int argc, char* argv[]) {
	// Init Google Flags and Google Logging
	std::string usage = std::string(argv[0]) + " <args>";
	gflags::SetUsageMessage(usage);
	gflags::ParseCommandLineFlags(&argc, &argv, false);
	google::InitGoogleLogging(argv[0]);
	FLAGS_logtostderr = 1;
	FLAGS_colorlogtostderr = 1;

	Blaze::Services::FlameClient flameClient;
	flameClient.Connect();

	LOG(INFO) << "Initializing RCF";
	RCF::RcfInitDeinit rcfInit;

	Blaze::Services::SparkServiceImpl sparkServiceImpl(&flameClient);
	RCF::RcfServer server(RCF::TcpEndpoint((int) FLAGS_port));

	LOG(INFO) << "Binding IServiceSpark implementation to server.";
	server.bind<Blaze::Services::IServiceSpark>(sparkServiceImpl);

	LOG(INFO) << "Starting RCF Blaze service on port: " << FLAGS_port;
	server.start();

	LOG(INFO) << "Waiting for any-key to exit.";
	std::cin.ignore();
	return 0;
}